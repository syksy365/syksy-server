package site.syksy.qingzhou.security.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import site.syksy.qingzhou.security.authentication.AuthenticationDsl;
import site.syksy.qingzhou.security.authentication.ServletUtil;
import site.syksy.qingzhou.security.properties.CaptchaProperties;
import site.syksy.qingzhou.security.properties.SecurityProperties;
import site.syksy.qingzhou.security.properties.WeChatProperties;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author Raspberry
 * Web安全配置
 */
@EnableConfigurationProperties({SecurityProperties.class, WeChatProperties.class, CaptchaProperties.class})
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    SecurityProperties securityProperties;
    @Resource
    List<AuthenticationDsl> authenticationDslList;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String logoutUrl = securityProperties.getPathPrefix() + "/" + securityProperties.getLogoutUrl();
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().logoutUrl(ServletUtil.removeRedundantSlash(logoutUrl)).logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()).and()
                .sessionManagement().maximumSessions(securityProperties.getMaximumSessions()).maxSessionsPreventsLogin(securityProperties.isMaxSessionsPreventsLogin());
        for (AuthenticationDsl authenticationDsl : authenticationDslList) {
            http.apply(authenticationDsl.getAuthenticationDsl());
        }
    }

    @Override
    public void configure(WebSecurity web) {
        Set<String> webIgnoring = securityProperties.getWebIgnoring();
        if (webIgnoring != null) {
            web.ignoring().antMatchers(webIgnoring.toArray(new String[0]));
        }
    }

}
