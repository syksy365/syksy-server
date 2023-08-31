package site.syksy.rose.upms.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import site.syksy.rose.upms.properties.UpmsProperties;
import site.syksy.rose.upms.service.RouteService;

import javax.annotation.Resource;

/**
 * @author Raspberry
 */
@Component
public class InitRoute implements CommandLineRunner {

    @Resource
    RouteService routeService;

    @Resource
    UpmsProperties upmsProperties;

    @Override
    public void run(String... args) {
        if (upmsProperties.getSecurity().getApiInit()) {
            routeService.registeredRoute();
        }
    }
}
