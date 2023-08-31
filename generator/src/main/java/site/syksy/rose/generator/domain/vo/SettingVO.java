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
package site.syksy.rose.generator.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * @author Raspberry
 */
public class SettingVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(name = "作者", example = "Raspberry")
    private String author;

    @Schema(name = "聚合模块", example = "syksy-boot-alpha")
    private String moduleName;

    @Schema(name = "组id", example = "site.syksy.boot.alpha")
    private String groupId;

    @Schema(name = "组下包")
    private String groupPackage;

    @Schema(name = "时间类型", example = "TIME_PACK")
    private String dateType;

    @Schema(name = "主键类型", example = "ASSIGN_ID")
    private String idType;

    @Schema(name = "表前缀")
    private String tablePrefix;

    @Schema(name = "生成文件类型")
    private String fileType;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupPackage() {
        return groupPackage;
    }

    public void setGroupPackage(String groupPackage) {
        this.groupPackage = groupPackage;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
