/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package site.syksy.qingzhou.generator.converts;

import site.syksy.qingzhou.generator.domain.ConfigurationDO;
import site.syksy.qingzhou.generator.domain.dto.TableFieldDTO;
import site.syksy.qingzhou.generator.rules.IColumnType;

/**
 * 数据库字段类型转换
 *
 * @author hubin
 * @since 2017-01-20
 */
public interface ITypeConvert {


    /**
     * 执行类型转换
     *
     * @param configurationDO 全局配置
     * @param tableFieldDTO   字段列信息
     * @return ignore
     */
    default IColumnType processTypeConvert(ConfigurationDO configurationDO, TableFieldDTO tableFieldDTO) {
        // 该方法提供重写
        return processTypeConvert(configurationDO, tableFieldDTO.getType());
    }


    /**
     * 执行类型转换
     *
     * @param configurationDO 全局配置
     * @param fieldType    字段类型
     * @return ignore
     */
    IColumnType processTypeConvert(ConfigurationDO configurationDO, String fieldType);
}
