package site.syksy.qingzhou.web.filter;



import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import site.syksy.qingzhou.common.AppContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Raspberry
 * 过滤请求中的用户信息，放入系统全局上下文中
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        Object userId = servletRequest.getAttribute("userId");

        if (userId != null) {
            AppContext.getContext().setUserId(Long.valueOf(String.valueOf(userId)));
        }
        chain.doFilter(request, response);
        AppContext.removeContext();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
