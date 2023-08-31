package site.syksy.rose.security.authentication.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import site.syksy.rose.security.authentication.AuthenticationDsl;
import site.syksy.rose.security.authentication.AuthenticationHandler;
import site.syksy.rose.security.authentication.ServletUtil;
import site.syksy.rose.security.properties.CaptchaProperties;
import site.syksy.rose.security.properties.SecurityProperties;
import site.syksy.rose.upms.service.UserService;

import javax.annotation.Resource;

/**
 * @author Raspberry
 * 通用身份认证配置
 */
@Component
public class GeneralAuthenticationDsl extends AbstractHttpConfigurer<GeneralAuthenticationDsl, HttpSecurity> implements AuthenticationDsl {

    @Resource
    ObjectMapper objectMapper;

    @Resource
    SecurityProperties securityProperties;

    @Resource
    UserService userService;

    @Resource
    CaptchaProperties captchaProperties;

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
    }

    /**
     * 配置通用身份认证过滤器：登录URL、身份验证提供者、验证成功处理器、验证失败处理器
     *
     * @param http
     */
    @Override
    public void configure(HttpSecurity http) {
        String loginUrl = securityProperties.getPathPrefix() + "/" + securityProperties.getLoginUrl();
        GeneralAuthenticationFilter generalAuthenticationFilter = new GeneralAuthenticationFilter(ServletUtil.removeRedundantSlash(loginUrl), captchaProperties, objectMapper);
        AuthenticationHandler authenticationHandler = new AuthenticationHandler();
        ProviderManager providerManager = new ProviderManager(new GeneralAuthenticationProvider(userService, captchaProperties, new BCryptPasswordEncoder()));
        generalAuthenticationFilter.setAuthenticationManager(providerManager);
        generalAuthenticationFilter.setAuthenticationSuccessHandler(authenticationHandler);
        generalAuthenticationFilter.setAuthenticationFailureHandler(authenticationHandler);
        http.addFilterBefore(generalAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public GeneralAuthenticationDsl getAuthenticationDsl() {
        return this;
    }
}
