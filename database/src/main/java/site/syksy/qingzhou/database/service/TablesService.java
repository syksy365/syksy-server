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
package site.syksy.qingzhou.database.service;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import site.syksy.qingzhou.common.properties.QingZhouProperties;
import site.syksy.qingzhou.database.domain.ColumnsDO;
import site.syksy.qingzhou.database.domain.IndexesDO;
import site.syksy.qingzhou.database.domain.TablesDO;
import site.syksy.qingzhou.database.domain.vo.TablesVO;
import site.syksy.qingzhou.database.mapper.ColumnsMapper;
import site.syksy.qingzhou.database.mapper.ExecuteMapper;
import site.syksy.qingzhou.database.mapper.IndexesMapper;
import site.syksy.qingzhou.database.mapper.TablesMapper;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Raspberry
 */
@Service
public class TablesService extends ServiceImpl<TablesMapper, TablesDO> {

    private static final Logger log = LoggerFactory.getLogger(TablesService.class);

    @Autowired
    ColumnsMapper columnsMapper;

    @Autowired
    IndexesMapper indexesMapper;

    @Autowired
    ExecuteMapper executeMapper;

    @Resource
    QingZhouProperties qingZhouProperties;

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    final String H2 = "h2";

    public List<TablesDO> selectTable() {
        return baseMapper.selectTable(null);
    }

    public TablesVO selectTable(String tableName) {
        TablesVO tablesVO = new TablesVO();
        List<TablesDO> tablesDOList = baseMapper.selectTable(tableName);
        if (tablesDOList.size() > 0) {
            TablesDO tablesDO = tablesDOList.get(0);
            tablesVO.setTablesDO(tablesDO);

            List<ColumnsDO> columnsDOList = columnsMapper.selectColumns(tableName);
            tablesVO.setColumnsDOList(columnsDOList);

            if (H2.equals(sqlSessionTemplate.getConfiguration().getDatabaseId())) {
                List<IndexesDO> indexesDOList = indexesMapper.selectIndexes(tableName);
                tablesVO.setIndexesDOList(indexesDOList);
            } else {
                List<IndexesDO> indexesDOList = new ArrayList<>();
                for (ColumnsDO columnsDO : columnsDOList) {
                    String columnKey = columnsDO.getColumnKey();
                    if (StringUtils.isNotBlank(columnKey) && "PRI".equals(columnKey.toUpperCase())) {
                        IndexesDO indexesDO = new IndexesDO();
                        indexesDO.setColumnName(columnsDO.getColumnName());
                        indexesDO.setPrimaryKey(true);
                        indexesDOList.add(indexesDO);
                    }
                }
                tablesVO.setIndexesDOList(indexesDOList);
            }
        } else {
            return null;
        }
        return tablesVO;
    }

    public DbType getDbType() {
        String str = sqlSessionTemplate.getConfiguration().getDatabaseId();
        if (str.contains("mysql")) {
            return DbType.MYSQL;
        } else if (str.contains("oracle")) {
            return DbType.ORACLE;
        } else if (str.contains("postgresql")) {
            return DbType.POSTGRE_SQL;
        } else if (str.contains("sqlserver")) {
            return DbType.SQL_SERVER;
        } else if (str.contains("db2")) {
            return DbType.DB2;
        } else if (str.contains("mariadb")) {
            return DbType.MARIADB;
        } else if (str.contains("sqlite")) {
            return DbType.SQLITE;
        } else if (str.contains("h2")) {
            return DbType.H2;
        } else if (str.contains("kingbase") || str.contains("kingbase8")) {
            return DbType.KINGBASE_ES;
        } else if (str.contains("dm")) {
            return DbType.DM;
        } else if (str.contains("zenith")) {
            return DbType.GAUSS;
        } else {
            return DbType.OTHER;
        }
    }


    /**
     * 初始化表和数据
     *
     * @param cls
     */
    public void initData(Class cls) {
        if (qingZhouProperties.getDbInit()) {
            List<String> tableNames = new ArrayList<>();
            try {
                Field[] fs = cls.getDeclaredFields();
                for (Field f : fs) {
                    f.setAccessible(true);
                    tableNames.add(f.get(cls).toString());
                }
            } catch (Exception e) {
                log.error("获取表名配置发生错误：{}", e.getMessage());
            }
            initData(tableNames);
        }
    }


    /**
     * 初始化表和数据
     *
     * @param tableNames
     */
    public void initData(List<String> tableNames) {
        String dbName = getDbType().getDb();
        for (String tableName : tableNames) {
            try {
                TablesVO tablesVO = selectTable(tableName);
                if (tablesVO != null) {
                    continue;
                }
                executeMapper.execute(readSql("sql/" + dbName + "/structure/" + tableName + ".sql"));
                executeMapper.execute(readSql("sql/" + dbName + "/data/" + tableName + ".sql"));
                log.info("创建表并导入数据成功：{}", tableName);
            } catch (Exception e) {
                log.error("创建表{}并导入数据发生错误：{}", tableName, e.getMessage());
            }
        }
    }

    /**
     * 读取sql文件
     *
     * @param path
     * @return
     */
    private String readSql(String path) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        InputStream inputStream = null;
        try {
            inputStream = classPathResource.getInputStream();
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String str = sb.toString();
            return str;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

}
