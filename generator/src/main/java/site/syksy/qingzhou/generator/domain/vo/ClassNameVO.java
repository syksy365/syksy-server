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
package site.syksy.qingzhou.generator.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Raspberry
 */
public class ClassNameVO {

    @Schema(name = "表名称", example = "SYKSY_CODE_GENERATOR_TEMPLATE")
    private String tableName;

    @Schema(name = "控制类名称", example = "TemplateController")
    private String controllerName;

    @Schema(name = "服务类名称", example = "TemplateService")
    private String serviceName;

    @Schema(name = "模型类名称", example = "TemplateDO")
    private String domainName;

    @Schema(name = "映射类名称", example = "TemplateMapper")
    private String mapperName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }
}
