package site.syksy.rose.web.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import site.syksy.rose.web.exception.UnauthorizedException;

/**
 * @author Raspberry
 * 方法参数解析器，解析request请求中的用户ID,注入@UserId注解的参数
 */
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        UserId userIdAnnotation = parameter.getParameterAnnotation(UserId.class);
        Object userId = webRequest.getAttribute("userId", 1);
        if (userIdAnnotation == null) {
            return null;
        } else if (userId == null && userIdAnnotation.required()) {
            throw new UnauthorizedException("未登录,或登录已失效，请重新登录");
        } else {
            return userId;
        }
    }
}
