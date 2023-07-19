package site.syksy.qingzhou.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.syksy.qingzhou.web.resolver.UserIdArgumentResolver;

import java.util.List;

/**
 * @author Raspberry
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new UserIdArgumentResolver());
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        /**
         * 给指定的包路径下的 http 接口统一增加前缀
         */
        configurer.addPathPrefix("qz/api", c -> c.getName().startsWith("site.syksy.qingzhou"));
    }
}
