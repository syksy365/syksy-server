# 轻舟 ![](https://img.shields.io/badge/license-Apache%202-blue)


### 介绍
轻舟是一个基于 Spring Boot 开发的 Java Web 基础支撑框架。
其包含辅助开发、身份认证、权限校验、字典设置、任务调度、消息中心、文件管理、工作流等一系列基础功能。基于【轻舟】可快速打造一个JAVA后台系统，非常适合独立开发者、小微企业使用（Apache License 2.0 开源许可）。  

### 软件架构
轻舟采用前后端代码分离架构。前端使用 React + Ant Design pro v5，后端使用 Spring Boot + Spring Security + Mybatis-Plus，其中会话状态使用 Server Side Session 方案，即 Cookie + Session。

### 依赖
- JDK 8+ (实测 8、11、17 都可以运行)
- MySQL 8+

### 源码启动

启动类位于 alpha 模块下，注意配置文件 application.yaml 中的数据库配置，改成你能连接的数据库。
创建一个空白 MySQL Schema，flyway 将自动创建表和插入初始化数据。默认数据库版本为 MySQL 8. 

#### 系统默认账户密码

账户: admin  
密码：qz  

-----------

### 创建一个基于轻舟的 Spring Boot 工程
使用 Maven 的 install 功能，将轻舟编译打包安装到本地 Maven 仓库。
再创建一个空白 Spring Boot 工程，引入轻舟的 Maven 坐标，即可拥有轻舟的功能。

```xml
<dependency>
    <groupId>site.syksy.qingzhou</groupId>
    <artifactId>qingzhou-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

