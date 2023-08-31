package site.syksy.rose.security.authentication.wechat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import site.syksy.rose.security.authentication.AuthenticationDsl;
import site.syksy.rose.security.authentication.AuthenticationHandler;
import site.syksy.rose.security.authentication.ServletUtil;
import site.syksy.rose.security.properties.SecurityProperties;
import site.syksy.rose.security.properties.WeChatProperties;
import site.syksy.rose.upms.service.UserService;

import javax.annotation.Resource;

/**
 * @author Raspberry
 * 微信小程序身份认证配置
 */
@Component
public class MiniProgramAuthenticationDsl extends AbstractHttpConfigurer<MiniProgramAuthenticationDsl, HttpSecurity> implements AuthenticationDsl {
    @Resource
    ObjectMapper objectMapper;

    @Resource
    SecurityProperties securityProperties;
    @Resource
    UserService userService;

    @Resource
    WeChatProperties weChatProperties;

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
    }

    /**
     * 配置微信小程序身份认证过滤器：登录URL、身份验证提供者、验证成功处理器、验证失败处理器
     *
     * @param http
     */
    @Override
    public void configure(HttpSecurity http) {
        String loginUrl = securityProperties.getPathPrefix() + "/" + weChatProperties.getLoginUrl();
        MiniProgramAuthenticationFilter weChatAuthenticationFilter = new MiniProgramAuthenticationFilter(ServletUtil.removeRedundantSlash(loginUrl), objectMapper);
        AuthenticationHandler authenticationHandler = new AuthenticationHandler();
        ProviderManager providerManager = new ProviderManager(new MiniProgramAuthenticationProvider(userService));
        weChatAuthenticationFilter.setAuthenticationManager(providerManager);
        weChatAuthenticationFilter.setAuthenticationSuccessHandler(authenticationHandler);
        weChatAuthenticationFilter.setAuthenticationFailureHandler(authenticationHandler);
        http.addFilterBefore(weChatAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public AbstractHttpConfigurer getAuthenticationDsl() {
        return this;
    }
}
