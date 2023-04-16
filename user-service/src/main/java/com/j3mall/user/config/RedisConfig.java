package com.j3mall.user.config;

import io.lettuce.core.ReadFrom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisConfig {

    @Autowired
    RedisProperties redisProperties;

    // 读取pool配置
    @Bean
    public GenericObjectPoolConfig poolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        config.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        config.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
        config.setMaxWait(redisProperties.getLettuce().getPool().getMaxWait());
        return config;
    }

    /**
     * 将哨兵信息放到配置中
     */
    @Bean
    public RedisSentinelConfiguration configuration() {
        RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration();
        redisConfig.setMaster(redisProperties.getSentinel().getMaster());
        redisConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));

        List<String> sentinelNodes = redisProperties.getSentinel().getNodes();
        if(sentinelNodes != null) {
            log.info("初始化Redis哨兵模式配置, {}", sentinelNodes);
            Optional.ofNullable(redisProperties.getSentinel().getPassword()).ifPresent(pass -> {
                redisConfig.setSentinelPassword(RedisPassword.of(pass));
            });

            List<RedisNode> sentinelNode = new ArrayList<>();
            for(String sen : sentinelNodes) {
                String[] arr = sen.split(":");
                RedisNode redisNode = new RedisNode(arr[0], Integer.parseInt(arr[1]));
                sentinelNode.add(redisNode);
            }
            redisConfig.setSentinels(sentinelNode);
        }
        return redisConfig;
    }

    // 注意传入的对象名和类型RedisSentinelConfiguration
    @Bean("lettuceConnectionFactory")
    public LettuceConnectionFactory lettuceConnectionFactory(@Qualifier("poolConfig") GenericObjectPoolConfig config,
        @Qualifier("configuration") RedisSentinelConfiguration redisConfig) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisConfig, clientConfiguration);
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String,?> redisTemplate(@Qualifier("lettuceConnectionFactory")LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String,?> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean("jsonRedisTemplate")
    public RedisTemplate<String, Object> jsonRedisTemplate(@Qualifier("lettuceConnectionFactory")LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisSerializer jsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        //设置序列化器
        redisTemplate.setDefaultSerializer(stringRedisSerializer);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    // @Bean
    // public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
    //     // Read from replica nodes
    //     return builder -> builder.readFrom(ReadFrom.REPLICA);
    // }

}
