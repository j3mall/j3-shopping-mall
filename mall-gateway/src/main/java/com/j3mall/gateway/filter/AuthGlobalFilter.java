package com.j3mall.gateway.filter;

import com.j3mall.j3.framework.constants.KeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 自定义全局认证网关过滤器（GlobalFilter）
 */
@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private static int reqCount = 0;

    public static String getName() {
        return "网关" + reqCount + "th";
    }

    @Override
    public int getOrder() {
        //过滤器的顺序，0 表示第一个
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        reqCount += 1;
        String traceId = UUID.randomUUID().toString();
        ServerHttpRequest.Builder reqBuilder = exchange.getRequest().mutate()
                .header(KeyConstants.KEY_TRACE_ID, traceId);
        String authToken = exchange.getRequest().getHeaders().getFirst(KeyConstants.KEY_J3_TOKEN);

        if (ObjectUtils.isEmpty(authToken)) {
            log.warn("{} 请求头token为空, {}, {}", getName(), exchange.getRequest().getURI(), exchange.getRequest().getQueryParams());
        } else {
            String[] userRange = authToken.split("-");
            String userId = userRange[0];

            //设置用户信息到请求，注意，这里是追加头部信息，token信息已经有了
            reqBuilder.header(KeyConstants.KEY_J3_USERID, userId);
            log.info("{} 添加请求头 {}, {}", getName(), reqBuilder.build().getHeaders(), exchange.getRequest().getURI());
        }

        ServerWebExchange mutableExchange = exchange.mutate().request(reqBuilder.build()).build();
        return chain.filter(mutableExchange);
    }

}

