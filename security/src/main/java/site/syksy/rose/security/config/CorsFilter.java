package site.syksy.rose.security.config;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Raspberry
 * 后端允许浏览器跨域请求
 * 生产环境下为了安全，有两个方案：
 * 第一是缩小Access-Control-Allow-Origin和Access-Control-Expose-Headers的范围，即不要使用用*或任意来源请求。
 * 第二是不允许浏览器跨域请求，对于多个服务，如前端后端，可以使用Nginx等反向反向代理软件，配置URL采用不同尾缀进行服务区分。
 */
@Order(-9999)
public class CorsFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        String origin = req.getHeader(HttpHeaders.ORIGIN);

        if (StringUtils.hasLength(origin)){
            res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
            res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
            //res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Origin, x-requested-with, Content-Type, Accept, Authorization");
            res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, OPTIONS, DELETE");
            res.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Cache-Control, Content-Language, Content-Type, Expires, Last-Modified, Pragma");
            res.addHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "60");
        }
        super.doFilter(req, res, chain);
    }

}
