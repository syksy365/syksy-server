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
package site.syksy.qingzhou.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author Raspberry
 * 安全配置
 */
@ConfigurationProperties(prefix = "qz.security")
public class SecurityProperties {

    /**
     * 全局免登录URL(ANT规则匹配)
     */
    private Set<String> webIgnoring;

    /**
     * API初始化
     */
    private Boolean apiInit = true;

    /**
     * 轻舟API前缀
     */
    private String pathPrefix = "qz/api";

    /**
     * 轻舟通用登录URL
     */
    private String loginUrl = "login/account";

    /**
     * 轻舟通用登出URL
     */
    private String logoutUrl = "login/out-login";

    /**
     * 用户最大会话数(-1为不限制，不允许为0)
     */
    private int maximumSessions = -1;

    /**
     * 超过用户最大会话数是否阻止登录
     */
    private boolean maxSessionsPreventsLogin = false;

    /**
     * 超级管理员账户名
     */
    private String admin = "admin";

    public Set<String> getWebIgnoring() {
        return webIgnoring;
    }

    public void setWebIgnoring(Set<String> webIgnoring) {
        this.webIgnoring = webIgnoring;
    }

    public Boolean getApiInit() {
        return apiInit;
    }

    public void setApiInit(Boolean apiInit) {
        this.apiInit = apiInit;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public int getMaximumSessions() {
        return maximumSessions;
    }

    public void setMaximumSessions(int maximumSessions) {
        this.maximumSessions = maximumSessions;
    }

    public boolean isMaxSessionsPreventsLogin() {
        return maxSessionsPreventsLogin;
    }

    public void setMaxSessionsPreventsLogin(boolean maxSessionsPreventsLogin) {
        this.maxSessionsPreventsLogin = maxSessionsPreventsLogin;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
