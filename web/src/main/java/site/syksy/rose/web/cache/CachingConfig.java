package site.syksy.rose.web.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;

/**
 * @author Raspberry
 * 缓存配置
 */
@Configuration
public class CachingConfig extends CachingConfigurerSupport {
    private static final Logger log = LoggerFactory.getLogger(CachingConfig.class);

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 定义缓存key生成规则
     *
     * @return
     */
    @Bean
    public KeyGenerator cacheKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(":");
            sb.append(method.getName());
            sb.append(":");
            try {
                sb.append(DigestUtils.md5DigestAsHex(objectMapper.writeValueAsBytes(params)));
            } catch (JsonProcessingException e) {
                log.error("缓存Key生成错误", e);
                throw new RuntimeException(e.getMessage());
            }
            return sb.toString();
        };
    }
}
