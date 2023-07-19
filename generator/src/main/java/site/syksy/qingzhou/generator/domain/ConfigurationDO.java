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
package site.syksy.qingzhou.generator.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.util.ObjectUtils;
import site.syksy.qingzhou.generator.constant.TableNameConstant;
import site.syksy.qingzhou.generator.rules.DateType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Raspberry
 */
@TableName(TableNameConstant.CONFIGURATION)
public class ConfigurationDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String userId;

    @Schema(title = "时间类型对应策略")
    private String dateType;

    @Schema(title = "主键类型")
    private String idType;

    @Schema(title = "作者")
    private String author;

    @Schema(title = "Maven聚合模块名称")
    private String moduleName;

    @Schema(title = "组id")
    private String groupId;

    @Schema(title = "组下包名称")
    private String groupPackage;

    @Schema(title = "表前缀")
    private String tablePrefix;

    @Schema(title = "生成文件类型")
    private String fileType;

    @Schema(title = "公开")
    private Boolean open;

    public String convertPackagePath() {
        if (groupPackage == null || groupPackage.trim().equals("")) {
            return groupId;
        } else {
            return groupId + "." + groupPackage;
        }
    }

    public DateType convertDateTypeEnum() {
        for (DateType e : DateType.values()) {
            if (e.toString().equals(dateType)) {
                return e;
            }
        }
        return DateType.TIME_PACK;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public boolean getTheFileType(String genre) {
        if (!ObjectUtils.isEmpty(fileType)) {
            String[] fileTypes = fileType.split(",");
            List<String> newFileTypeList = Arrays.asList(fileTypes);
            for (String fileType : newFileTypeList) {
                if (fileType.equals(genre)) {
                    return true;
                }
            }
        }
        return false;
    }
}
