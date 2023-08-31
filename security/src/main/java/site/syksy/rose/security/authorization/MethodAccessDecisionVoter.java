package site.syksy.rose.security.authorization;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import site.syksy.rose.security.constant.GeneralConstant;
import site.syksy.rose.security.constant.SecurityConstant;
import site.syksy.rose.security.properties.SecurityProperties;
import site.syksy.rose.upms.domain.RouteDO;
import site.syksy.rose.upms.service.RouteService;
import site.syksy.rose.web.exception.UnauthorizedException;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Raspberry
 * 方法权限验证（授权投票）
 */
@Component
public class MethodAccessDecisionVoter implements AccessDecisionVoter<MethodInvocation> {

    @Autowired
    @Lazy
    RouteService routeService;

    @Resource
    SecurityProperties securityProperties;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return (attribute.getAttribute() != null) && attribute.getAttribute().equals(SecurityConstant.ROLE);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, MethodInvocation methodInvocation, Collection<ConfigAttribute> attributes) {
        String handlerMethod = initDescription(methodInvocation.getThis().getClass().getName(), methodInvocation.getMethod());
        String md5HandlerMethod = DigestUtils.md5DigestAsHex(handlerMethod.getBytes(StandardCharsets.UTF_8));

        /**
         * 验证免登录的方法
         */
        Map<String, RouteDO> noLoginMap = routeService.selectByNoLogin();
        RouteDO noLoginRouteDO = noLoginMap.get(md5HandlerMethod);
        if (noLoginRouteDO != null) {
            return ACCESS_GRANTED;
        }

        /**
         * 验证仅需登录的方法
         */
        Object principal = authentication.getPrincipal();
        if (SecurityConstant.ANONYMOUS_USER.equals(principal)) {
            throw new UnauthorizedException("未登录,或登录已失效，请重新登录。");
        } else {
            Map<String, RouteDO> loginMap = routeService.selectByLogin();
            RouteDO loginRouteDO = loginMap.get(md5HandlerMethod);
            if (loginRouteDO != null) {
                return ACCESS_GRANTED;
            }
        }

        /**
         * 验证是否超级管理员
         */
        Map<String, Object> map = (Map<String, Object>) principal;
        if (securityProperties.getAdmin().equals(map.get(GeneralConstant.USERNAME))) {
            return ACCESS_GRANTED;
        }

        /**
         * 验证需授权的方法
         */
        Map<String, Object> principalMap = (Map<String, Object>) principal;
        Map<String, RouteDO> authorizationMap = routeService.selectRouteByUserId(principalMap.get(SecurityConstant.USERNAME).toString());
        RouteDO authorizationRouteDO = authorizationMap.get(md5HandlerMethod);
        if (authorizationRouteDO != null) {
            return ACCESS_GRANTED;
        }
        return ACCESS_DENIED;
    }

    private static String initDescription(String className, Method method) {
        StringJoiner joiner = new StringJoiner(", ", "(", ")");
        for (Class<?> paramType : method.getParameterTypes()) {
            joiner.add(paramType.getSimpleName());
        }
        return className + "#" + method.getName() + joiner;
    }
}
