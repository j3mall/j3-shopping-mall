package com.j3mall.gateway.filter;

import com.j3mall.framework.cache.utils.RedisLock;
import com.j3mall.gateway.constants.GatewayConstants;
import com.j3mall.j3.framework.constants.KeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * 自定义网关全局认证过滤器（GlobalFilter）
 */
@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate strRedisTemplate;

    private Long reqCount;

    public String getName() {
        return "网关认证" + reqCount + "th";
    }

    @Override
    public int getOrder() {
        //过滤器的顺序，0 表示第一个
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        increaseReqCount();
        String traceId = UUID.randomUUID().toString();
        ServerHttpRequest.Builder reqBuilder = exchange.getRequest().mutate()
                .header(KeyConstants.KEY_TRACE_ID, traceId);
        String authToken = exchange.getRequest().getHeaders().getFirst(KeyConstants.KEY_J3_TOKEN);

        if (ObjectUtils.isEmpty(authToken)) {
            log.warn(getName()+"请求头token为空, {}, {}", exchange.getRequest().getURI(), exchange.getRequest().getQueryParams());
        } else {
            String[] userRange = authToken.split("-");
            Integer userId = Integer.valueOf(userRange[0]);
            if (userRange.length >= 2) {
                // 随机用户模式，用于测试并发性能
                Integer maxUserId = Integer.parseInt(userRange[1]);
                userId += new Random().nextInt(maxUserId - userId + 1);
                log.info(getName()+"随机用户{} -> {}", userRange, userId);
            }
            //设置用户信息到请求，注意，这里是追加头部信息，token信息已经有了
            reqBuilder.header(KeyConstants.KEY_J3_USERID, String.valueOf(userId));
            log.debug(getName()+"添加请求头 {}, {}", reqBuilder.build().getHeaders(), exchange.getRequest().getURI());
        }

        ServerWebExchange mutableExchange = exchange.mutate().request(reqBuilder.build()).build();
        return chain.filter(mutableExchange);
    }

    private Long getReqCount() {
        return Optional.ofNullable(strRedisTemplate.opsForValue().get(GatewayConstants.KEY_GATEWAY_REQ_COUNT))
                .map(Long::valueOf).orElse(0L);
    }

    // 分布式锁使用示范
    private void increaseReqCount() {
        String lockKey = GatewayConstants.KEY_GATEWAY_REQ_COUNT + ":" + (getReqCount() + 1);
        RedisLock lock = new RedisLock(strRedisTemplate, lockKey, 1000, 2*1000);
        try {
            if (lock.lockWithoutWait()) {
                reqCount = strRedisTemplate.opsForValue().increment(GatewayConstants.KEY_GATEWAY_REQ_COUNT);
                log.debug(getName()+"获取锁成功, {}", lock.getLockKey());
            } else {
                log.error(getName()+"获取锁失败, currReqCount {}", reqCount);
            }
        } finally {
            lock.unlock();
        }
    }

}

