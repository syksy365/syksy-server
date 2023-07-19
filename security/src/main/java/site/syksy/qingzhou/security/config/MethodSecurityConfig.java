package site.syksy.qingzhou.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import site.syksy.qingzhou.security.authorization.MethodAccessDecisionVoter;
import site.syksy.qingzhou.upms.service.ApiService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raspberry
 * 方法权限配置
 */
@Configuration
@DependsOn({"flyway", "flywayInitializer"})
@EnableGlobalMethodSecurity
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    MethodAccessDecisionVoter methodAccessDecisionVoter;

    @Autowired
    ApiService apiService;

    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        MapBasedMethodSecurityMetadataSource metadataSource = new MapBasedMethodSecurityMetadataSource(apiService.selectAll());
        return metadataSource;
    }

    @Override
    protected AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> voters = new ArrayList<>();
        voters.add(methodAccessDecisionVoter);
        AccessDecisionManager accessDecisionManager = new UnanimousBased(voters);
        return accessDecisionManager;
    }

}
