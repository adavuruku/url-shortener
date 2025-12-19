package com.example.url_shortener.Config;

import com.example.url_shortener.Utils.Base62CodeGenerator;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.concurrent.Executor;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 8:56 PM
 **/
@Configuration
public class AppConfig {

    private final ShortUrlProperties shortUrlProperties;

    public AppConfig(ShortUrlProperties shortUrlProperties) {
        this.shortUrlProperties = shortUrlProperties;
    }

    /**
     * Initialize Base62CodeGenerator instance
     *
     * @return
     */
    @Bean
    public Base62CodeGenerator getBase62CodeGenerator(){
        return new Base62CodeGenerator();
    }

    /**
     * Initialize RedisCacheConfiguration config
     *
     * @return
     */

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues();
    }


    /**
     * initialize taskExecutor
     * @return
     */

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize((int)shortUrlProperties.getThreadConfig().getCorePoolSize());
        executor.setMaxPoolSize((int) shortUrlProperties.getThreadConfig().getMaxPoolSize());
        executor.setQueueCapacity((int)shortUrlProperties.getThreadConfig().getQueueCapacity());
        executor.setThreadNamePrefix("async-thread-");
        executor.initialize();
        return executor;
    }

    /**
     * OpenApi config
     * @return
     */

    @Bean
    public OpenAPI urlShortenerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortener API")
                        .description("Simple URL Shortener service")
                        .version("v1.0")
                        .contact(
                                new Contact()
                                        .url("https://github.com/adavuruku")
                                        .name("Sherif Abdulraheem Adavuruku")
                                        .email("aabdulraheemsherif@gmail.com")
                        )
                );
    }
}
