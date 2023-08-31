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
package site.syksy.rose.generator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.syksy.rose.database.domain.ColumnsDO;
import site.syksy.rose.database.domain.IndexesDO;
import site.syksy.rose.database.domain.TablesDO;
import site.syksy.rose.database.domain.vo.TablesVO;
import site.syksy.rose.generator.converts.ITypeConvert;
import site.syksy.rose.generator.domain.ConfigurationDO;
import site.syksy.rose.generator.domain.dto.TableFieldDTO;
import site.syksy.rose.generator.domain.dto.TableInfoDTO;
import site.syksy.rose.generator.rules.IColumnType;
import site.syksy.rose.generator.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raspberry
 */
@Service
public class DomainService {

    @Autowired
    ITypeConvert iTypeConvert;


    /**
     * 表字段转换
     *
     * @param tablesVO
     * @param configurationDO
     * @return
     */
    public TableInfoDTO convertTableFields(TablesVO tablesVO, ConfigurationDO configurationDO) {
        TableInfoDTO tableInfoDTO = new TableInfoDTO();
        List<TableFieldDTO> tableFieldDTOList = new ArrayList<>();
        List<ColumnsDO> columnsDOList = tablesVO.getColumnsDOList();
        List<IndexesDO> indexesDOList = tablesVO.getIndexesDOList();
        TablesDO tablesDO = tablesVO.getTablesDO();
        tableInfoDTO.setName(tablesDO.getTableName());
        tableInfoDTO.setComment(tablesDO.getTableComment());

        columnsDOList.forEach(columnsDO -> {
            TableFieldDTO tableFieldDTO = new TableFieldDTO();
            tableFieldDTO.setPropertyName(processName(columnsDO.getColumnName(), NamingStrategy.underline_to_camel, null));
            IColumnType iColumnType = iTypeConvert.processTypeConvert(configurationDO, columnsDO.getTypeName());
            tableFieldDTO.setPropertyType(iColumnType);
            tableFieldDTO.setComment(columnsDO.getRemarks());
            tableFieldDTO.setIsNullable(columnsDO.getIsNullable());
            tableFieldDTO.setCharacterMaximumLength(columnsDO.getCharacterMaximumLength());

            long count = indexesDOList.stream().filter(indexesDO -> indexesDO.getColumnName().equals(columnsDO.getColumnName())).count();
            if (count > 0) {
                tableFieldDTO.setKeyFlag(true);
            } else {
                tableFieldDTO.setKeyFlag(false);
            }
            tableFieldDTOList.add(tableFieldDTO);
        });


        tableInfoDTO.setTableFieldDTOList(tableFieldDTOList);
        return tableInfoDTO;
    }

    /**
     * 处理表/字段名称
     *
     * @param name     ignore
     * @param strategy ignore
     * @param prefix   ignore
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy, String[] prefix) {
        boolean removePrefix = false;
        if (prefix != null && prefix.length != 0) {
            removePrefix = true;
        }
        String propertyName;
        if (removePrefix) {
            if (strategy == NamingStrategy.underline_to_camel) {
                // 删除前缀、下划线转驼峰
                propertyName = NamingStrategy.removePrefixAndCamel(name, prefix);
            } else {
                // 删除前缀
                propertyName = NamingStrategy.removePrefix(name, prefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            // 下划线转驼峰
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            // 不处理
            propertyName = name;
        }
        return propertyName;
    }
}
