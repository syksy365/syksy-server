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
package site.syksy.rose.generator.domain.dto;

import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Raspberry
 */
public class TableInfoDTO {

    private Set<String> importPackages;

    private List<TableFieldDTO> tableFieldDTOList;

    private String name;

    private String comment;

    public Set<String> getImportPackages() {
        return importPackages;
    }

    public List<TableFieldDTO> getTableFieldDTOList() {
        return tableFieldDTOList;
    }

    public void setTableFieldDTOList(List<TableFieldDTO> tableFieldDTOList) {
        if (ObjectUtils.isEmpty(importPackages)) {
            importPackages = new HashSet<>();
        }
        if (!ObjectUtils.isEmpty(tableFieldDTOList)) {
            this.tableFieldDTOList = tableFieldDTOList;
            for (TableFieldDTO tableFieldDTO : tableFieldDTOList) {
                if (!ObjectUtils.isEmpty(tableFieldDTO.getPropertyType()) && !ObjectUtils.isEmpty(tableFieldDTO.getPropertyType().getPkg())) {
                    importPackages.add(tableFieldDTO.getPropertyType().getPkg());
                }
                if (tableFieldDTO.getKeyFlag()) {
                    importPackages.add(com.baomidou.mybatisplus.annotation.TableId.class.getCanonicalName());
                    importPackages.add(com.baomidou.mybatisplus.annotation.IdType.class.getCanonicalName());
                }else {
                    importPackages.add(com.baomidou.mybatisplus.annotation.TableField.class.getCanonicalName());
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TableInfoDTO setImportPackages(String pkg) {
        if (importPackages.contains(pkg)) {
            return this;
        } else {
            importPackages.add(pkg);
            return this;
        }
    }

    @Override
    public String toString() {
        return "TableInfoDTO{" +
                "importPackages=" + importPackages +
                ", tableFieldDTOList=" + tableFieldDTOList +
                '}';
    }
}
