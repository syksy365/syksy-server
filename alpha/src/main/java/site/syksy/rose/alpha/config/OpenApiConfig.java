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
package site.syksy.rose.alpha.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.syksy.rose.security.properties.CaptchaProperties;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Raspberry
 */
@Configuration
public class OpenApiConfig implements WebMvcConfigurer {

    @Resource
    CaptchaProperties captchaProperties;

    @Bean
    public GroupedOpenApi createSecurityApi() {
        return GroupedOpenApi.builder()
                .group("安全")
                .packagesToScan("site.syksy.rose.security")
                .build();
    }

    @Bean
    public GroupedOpenApi createGeneratorApi() {
        return GroupedOpenApi.builder()
                .group("辅助开发")
                .packagesToScan("site.syksy.rose.generator")
                .build();
    }

    @Bean
    public GroupedOpenApi createDatabaseApi() {
        return GroupedOpenApi.builder()
                .group("数据库")
                .packagesToScan("site.syksy.rose.database")
                .build();
    }

    @Bean
    public GroupedOpenApi createConfigApi() {
        return GroupedOpenApi.builder()
                .group("设置")
                .packagesToScan("site.syksy.rose.setting")
                .build();
    }

    @Bean
    public GroupedOpenApi createUpmsApi() {
        return GroupedOpenApi.builder()
                .group("账户权限")
                .packagesToScan("site.syksy.rose.upms")
                .build();
    }

    @Bean
    public GroupedOpenApi createFileApi() {
        return GroupedOpenApi.builder()
                .group("文件管理")
                .packagesToScan("site.syksy.rose.file")
                .build();
    }


    @Bean
    public GroupedOpenApi createGlobalFileApi() {
        return GroupedOpenApi.builder()
                .group("全局文件管理")
                .packagesToScan("site.syksy.rose.global.file")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        io.swagger.v3.oas.models.media.Schema userSchema = new io.swagger.v3.oas.models.media.Schema<Map<String, Object>>()
                .addProperty("username", new StringSchema().example("admin"))
                .addProperty("password", new StringSchema().example("1111"));
        if (captchaProperties.getEnable()) {
            userSchema.addProperty("captcha", new StringSchema().example("9999"));
        }

        return new OpenAPI()
                .info(new Info().title("海棠支撑平台")
                        .description("为Java Web工程提供基础支撑")
                        .version("1.0.0")
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("参考文档")
                        .url("https://syksy.site/"))
                .components(new Components().addSchemas("UserSchema", userSchema));
    }
}
