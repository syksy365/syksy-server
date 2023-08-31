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
package site.syksy.rose.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Set;

/**
 * @author Raspberry
 * 海棠总配置
 */
@ConfigurationProperties(prefix = "qz")
public class QingZhouProperties {

    /**
     * 数据库初始化
     */
    private Boolean dbInit = false;

    /**
     * web api 包路径，用于统一进行返回值包装
     */
    private Set<String> webApiPackagePath;

    /**
     * 上传文件目录
     */
    @NestedConfigurationProperty
    private FileDirectoryProperties fileDirectory;

    public Boolean getDbInit() {
        return dbInit;
    }

    public void setDbInit(Boolean dbInit) {
        this.dbInit = dbInit;
    }

    public FileDirectoryProperties getFileDirectory() {
        return fileDirectory;
    }

    public void setFileDirectory(FileDirectoryProperties fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    public Set<String> getWebApiPackagePath() {
        return webApiPackagePath;
    }

    public void setWebApiPackagePath(Set<String> webApiPackagePath) {
        this.webApiPackagePath = webApiPackagePath;
    }
}
