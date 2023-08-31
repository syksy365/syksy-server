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
package site.syksy.rose.database.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.syksy.rose.database.domain.TablesDO;
import site.syksy.rose.database.domain.vo.TablesVO;
import site.syksy.rose.database.service.TablesService;

import java.util.List;

/**
 * @author Raspberry
 */
@RestController
@Tag(name = "schema")
@RequestMapping("database/tables")
public class TablesController {

    @Autowired
    TablesService tablesService;

    @Operation(summary = "获取全部表")
    @GetMapping
    public List<TablesDO> getAllTable() {
        return tablesService.selectTable();
    }

    @Operation(summary = "获取表")
    @GetMapping("{tableName}")
    public TablesVO getTable(@PathVariable String tableName) {
        return tablesService.selectTable(tableName);
    }


}
