/*
 * Copyright 2020-2030 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package site.syksy.qingzhou.alpha.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Raspberry
 */
@Component
public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {

    private static final Logger log = LoggerFactory.getLogger(ServerConfig.class);

    @Autowired
    WebApplicationContext webApplicationConnect;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        }
        int port = webServerInitializedEvent.getWebServer().getPort();
        String path = webApplicationConnect.getServletContext().getContextPath();
        //"h2-console: http://localhost:" + port + path + "/h2-console\n\t" +

        System.out.println("----------------------------------------------------------------\n" +
                "轻舟启动成功！资源路径:\n" +
                "-----------------------------本地访问----------------------------\n" +
                "index: \t\thttp://localhost:" + port + path + "/qz-ui/index.html\n" +
                "swagger-ui: http://localhost:" + port + path + "/swagger-ui.html\n" +
                "-----------------------------IP 访问-----------------------------\n" +
                "index: \t\thttp://" + ip + ":" + port + path + "/qz-ui/index.html\n" +
                "swagger-ui: http://" + ip + ":" + port + path + "/swagger-ui.html\n" +
                "----------------------------------------------------------------");
    }
}
