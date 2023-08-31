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
package site.syksy.rose.database.domain.vo;

import site.syksy.rose.database.domain.ColumnsDO;
import site.syksy.rose.database.domain.IndexesDO;
import site.syksy.rose.database.domain.TablesDO;

import java.io.Serializable;
import java.util.List;

/**
 * @author Raspberry
 */
public class TablesVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private TablesDO tablesDO;

    private List<ColumnsDO> columnsDOList;

    private List<IndexesDO> indexesDOList;

    public TablesDO getTablesDO() {
        return tablesDO;
    }

    public void setTablesDO(TablesDO tablesDO) {
        this.tablesDO = tablesDO;
    }

    public List<ColumnsDO> getColumnsDOList() {
        return columnsDOList;
    }

    public void setColumnsDOList(List<ColumnsDO> columnsDOList) {
        this.columnsDOList = columnsDOList;
    }

    public List<IndexesDO> getIndexesDOList() {
        return indexesDOList;
    }

    public void setIndexesDOList(List<IndexesDO> indexesDOList) {
        this.indexesDOList = indexesDOList;
    }
}
