# 轻舟 ![](https://img.shields.io/badge/license-Apache%202-blue)


#### 介绍
轻舟是一个基于 Spring Boot 开发的 Java Web 基础支撑框架。
其包含辅助开发、身份认证、权限校验、字典设置、任务调度、消息中心、文件管理、工作流等一系列基础功能。基于【轻舟】可快速打造一个JAVA后台系统，非常适合独立开发者、小微企业使用（Apache License 2.0 开源许可）。  
![输入图片说明](https://images.gitee.com/uploads/images/2021/1118/224004_4e432788_339062.jpeg "截屏2021-11-18 下午10.27.47.jpg")

#### 接口文档导出
使用[Swagger Fly](https://swagger-fly.com/)导出精美PDF文档（支持 Swagger 2.0 和 OpenAPI 3.0规范）
![输入图片说明](https://images.gitee.com/uploads/images/2021/1008/141908_f6908382_339062.png "屏幕截图 2021-10-08 141657.png")

#### 软件架构
轻舟采用前后端代码分离架构。前端使用 React + Ant Design pro v5，后端使用 Spring Boot + Spring Security + Mybatis-Plus，其中会话状态使用 Server Side Session 方案，即 Cookie + Session。

![输入图片说明](https://images.gitee.com/uploads/images/2021/0722/150604_5d54dfbc_339062.png "技术选型架构1.png")
#### 安装教程
 **下载jar包抢先试用** 
|文件名|链接|提取码|
|---|---|---|
|qingzhou-2021-03-17.jar|https://pan.baidu.com/s/1xYMY-aQkt-ryVkEfd6ps8g|d7t8|

只需 jdk8+，一行命令启动程序
```
java -jar qingzhou-2021-03-17.jar
```
启动成功后浏览器访问：http://localhost:8849/qz-ui/index.html  
账号：admin  
密码：syksy

-----------
克隆或下载源码后需注意，前后端代码放在一个仓库中(dev最新)，分别是 qingzhou-boot-web 和 qingzhou-boot。  
后端源码启动请查看 [后端开发快速开始](https://gitee.com/syksy/qingzhou/wikis/%E5%90%8E%E7%AB%AF%E5%BC%80%E5%8F%91%E5%BF%AB%E9%80%9F%E5%BC%80%E5%A7%8B?sort_id=3436538)

前端源码启动请查看 [前端开发快速开始](https://gitee.com/syksy/qingzhou/wikis/%E5%89%8D%E7%AB%AF%E5%BC%80%E5%8F%91%E5%BF%AB%E9%80%9F%E5%BC%80%E5%A7%8B?sort_id=3436528)

#### 使用说明( **正在实现中** )
只需创建一个基础 Spring Boot 工程，引入轻舟的 Maven 坐标，即可拥有轻舟的基础功能。

```xml
<dependency>
    <groupId>site.syksy.qingzhou</groupId>
    <artifactId>qingzhou-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

