package site.syksy.qingzhou.security.authentication;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * @author Raspberry
 * 身份认证DSL配置接口
 */
public interface AuthenticationDsl {

    /**
     * 获取身份认证配置实例
     * @return
     */
    AbstractHttpConfigurer getAuthenticationDsl();
}
