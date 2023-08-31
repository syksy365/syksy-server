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
package site.syksy.rose.generator.config;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.syksy.rose.database.service.TablesService;
import site.syksy.rose.generator.converts.ITypeConvert;
import site.syksy.rose.generator.converts.TypeConvertRegistry;

import java.util.Optional;

/**
 * @author Raspberry
 */
@Configuration
public class GeneratorConfig {

    @Autowired
    TablesService tablesService;

    @Bean
    public ITypeConvert getTypeConvert() {
        DbType dbType = tablesService.getDbType();
        TypeConvertRegistry typeConvertRegistry = new TypeConvertRegistry();
        ITypeConvert typeConvert = Optional.ofNullable(typeConvertRegistry.getTypeConvert(dbType)).orElseGet(() -> typeConvertRegistry.getTypeConvert(DbType.MYSQL));
        return typeConvert;
    }


}
