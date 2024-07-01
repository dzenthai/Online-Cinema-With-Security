/**
 * stores information such as user authentication details for 7 days.
 */

package org.online.cinema.security.configuration.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 604800)
public class RedisSessionConfig {

    @Value("${session.redis.host}")
    private String sessionHost;

    @Value("${session.redis.port}")
    private int sessionPort;

    @Value("${session.redis.database}")
    private int database;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory =
                new LettuceConnectionFactory(sessionHost, sessionPort);
        lettuceConnectionFactory.setDatabase(database);
        return lettuceConnectionFactory;
    }
}
