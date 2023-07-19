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
package site.syksy.qingzhou.generator.domain.dto;

import site.syksy.qingzhou.generator.rules.IColumnType;

/**
 * @author Raspberry
 */
public class  TableFieldDTO {

    /**
     * 是否为主键
     */
    private Boolean keyFlag;

    private String type;

    /**
     * 属性名
     */
    private String propertyName;

    /**
     * 属性类型
     */
    private IColumnType propertyType;

    /**
     * 描述
     */
    private String comment;

    /**
     * 是否允许为空
     */
    private String isNullable;

    /**
     * 字符型最大长度
     */
    private Integer characterMaximumLength;



    public Boolean getKeyFlag() {
        return keyFlag;
    }

    public void setKeyFlag(Boolean keyFlag) {
        this.keyFlag = keyFlag;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public IColumnType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(IColumnType propertyType) {
        this.propertyType = propertyType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public Integer getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    public void setCharacterMaximumLength(Integer characterMaximumLength) {
        this.characterMaximumLength = characterMaximumLength;
    }

    @Override
    public String toString() {
        return "TableFieldDTO{" +
                "keyFlag=" + keyFlag +
                ", type='" + type + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", propertyType=" + propertyType +
                '}';
    }
}
