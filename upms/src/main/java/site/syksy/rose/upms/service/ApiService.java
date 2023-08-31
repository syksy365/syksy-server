package site.syksy.rose.upms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import site.syksy.rose.upms.domain.RouteDO;
import site.syksy.rose.upms.mapper.RouteMapper;
import site.syksy.rose.upms.properties.UpmsProperties;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Raspberry
 */
@Service
public class ApiService {

    @Autowired
    RouteMapper routeMapper;

    @Resource
    UpmsProperties upmsProperties;

    private static final String ROLE = "QZ_ROLE";

    /**
     * 获取全部在线的接口路由,并且校验类型为登录和授权
     *
     * @return
     */
    public Map<String, List<ConfigAttribute>> selectAll() {
        QueryWrapper<RouteDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RouteDO::getOnline, true);
        List<RouteDO> routeDOList = routeMapper.selectList(queryWrapper);

        AntPathMatcher matcher = new AntPathMatcher();
        Map<String, List<ConfigAttribute>> methodMap = new HashMap<>();
        for (RouteDO routeDO : routeDOList) {
            if (!webIgnoringAntMatchers(matcher, routeDO.getPattern())) {
                String handlerMethod = routeDO.getHandlerMethod();
                handlerMethod = handlerMethod.replace("#", ".");
                int leftParenthesisIndex = handlerMethod.indexOf("(");
                handlerMethod = handlerMethod.substring(0, leftParenthesisIndex);

                String className = handlerMethod.substring(0, handlerMethod.lastIndexOf("."));
                //判断类是否存在
                if (!ClassUtils.isPresent(className, null)) {
                    continue;
                }
                //判断方法是否存在,不区分参数重载
                String methodName = handlerMethod.substring(handlerMethod.lastIndexOf(".") + 1);
                if (!hasMethod(className, methodName)) {
                    continue;
                }
                methodMap.put(handlerMethod, SecurityConfig.createList(ROLE));
            }
        }
        return methodMap;
    }

    /**
     * 全局免登录配置匹配
     *
     * @param matcher
     * @param pattern
     * @return
     */
    public boolean webIgnoringAntMatchers(AntPathMatcher matcher, String pattern) {
        Set<String> webIgnoring = upmsProperties.getSecurity().getWebIgnoring();
        if (webIgnoring != null && webIgnoring.size() > 0) {
            for (String w : webIgnoring) {
                if (matcher.match(w, pattern)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断类中是否包含某个方法
     *
     * @param className  类名（完整路径）
     * @param methodName 方法名
     * @return 方法是否存在类中
     */
    public boolean hasMethod(String className, String methodName) {
        try {
            Method method = ReflectionUtils.findMethod(Class.forName(className), methodName, null);
            if (null != method) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            return false;
        }
        return false;
    }
}
