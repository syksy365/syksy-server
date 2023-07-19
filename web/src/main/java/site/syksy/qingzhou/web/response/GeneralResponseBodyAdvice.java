package site.syksy.qingzhou.web.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import site.syksy.qingzhou.common.properties.QingZhouProperties;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author Raspberry
 */
@RestControllerAdvice
public class GeneralResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    QingZhouProperties qingZhouProperties;

    private final static String PACKAGE_PATH = "site.syksy.qingzhou";

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        String className = methodParameter.getMethod().getDeclaringClass().getName();
        if (className.startsWith(PACKAGE_PATH)) {
            return true;
        }
        Set<String> webApiPackagePath = qingZhouProperties.getWebApiPackagePath();
        if (webApiPackagePath != null) {
            for (String w : webApiPackagePath) {
                if (className.startsWith(w)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof ResponseMessage) {
            return o;
        }
        if (o instanceof String) {
            return ResponseMessage.success(o).toJSON();
        }
        return ResponseMessage.success(o);
    }
}
