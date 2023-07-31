-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: syksy
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `s_code_generator_config`
--

DROP TABLE IF EXISTS `s_code_generator_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_code_generator_config` (
  `user_id` varchar(19) NOT NULL,
  `date_type` varchar(45) DEFAULT NULL,
  `id_type` varchar(45) DEFAULT NULL,
  `author` varchar(45) DEFAULT NULL,
  `module_name` varchar(45) DEFAULT NULL,
  `group_id` varchar(100) DEFAULT NULL,
  `group_package` varchar(45) DEFAULT NULL,
  `table_prefix` varchar(45) DEFAULT NULL,
  `file_type` varchar(100) DEFAULT NULL,
  `open` tinyint(1) DEFAULT '1' COMMENT '是否公开配置',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_code_generator_config`
--

LOCK TABLES `s_code_generator_config` WRITE;
/*!40000 ALTER TABLE `s_code_generator_config` DISABLE KEYS */;
INSERT INTO `s_code_generator_config` VALUES ('1322014259407925249','TIME_PACK','ASSIGN_ID','管理员','alpha','site.syksy.qingzhou.alpha',NULL,'s_','controller,domain,service,mapper',1);
/*!40000 ALTER TABLE `s_code_generator_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_code_generator_template`
--

DROP TABLE IF EXISTS `s_code_generator_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_code_generator_template` (
  `id` varchar(19) NOT NULL,
  `content` text,
  `genre` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码模板表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_code_generator_template`
--

LOCK TABLES `s_code_generator_template` WRITE;
/*!40000 ALTER TABLE `s_code_generator_template` DISABLE KEYS */;
INSERT INTO `s_code_generator_template` VALUES ('1297099430799458306','package ${packagePath}.domain;\n\n<#list table.importPackages as pkg>\nimport ${pkg};\n</#list>\nimport io.swagger.v3.oas.annotations.media.Schema;\nimport lombok.Data;\n\nimport javax.validation.constraints.NotBlank;\nimport javax.validation.constraints.NotNull;\nimport javax.validation.constraints.Size;\n\n/**\n * @author ${author}\n * @since ${date}\n */\n@Data\n@TableName(\"${table.name}\")\n@Schema(title = \"${table.comment!}\")\npublic class ${domainName} implements Serializable {\n\n    private static final long serialVersionUID = 1L;\n\n<#-- ----------  BEGIN 字段循环遍历  ---------->\n<#list table.tableFieldDTOList as field>\n    <#if field.keyFlag>\n        <#assign keyPropertyName=\"${field.propertyName}\"/>\n    </#if>\n    <#if field.keyFlag>\n    <#-- 主键 -->\n    @Schema(hidden = true)\n    <#if field.keyFlag>\n    @TableId(value = \"${field.propertyName}\", type = IdType.AUTO)\n        <#elseif idType??>\n    @TableId(value = \"${field.propertyName}\", type = IdType.${idType})\n        <#elseif field.convert>\n    @TableId(\"${field.propertyName}\")\n        </#if>\n    <#-- 普通字段 -->\n    <#else>\n    <#if field.propertyType.type == \"String\" && field.characterMaximumLength??>\n    @Size(max = ${field.characterMaximumLength?c})\n        </#if>\n        <#if field.isNullable == \"NO\">\n            <#if field.propertyType.type == \"String\">\n    @NotBlank\n            <#else>\n    @NotNull\n            </#if>\n        </#if>\n        <#if field.comment!?length gt 0>\n    @Schema(title = \"${field.comment}\")\n        </#if>\n    <#-- -----   存在字段填充设置   ----->\n    </#if>\n    private ${field.propertyType.type} ${field.propertyName};\n\n</#list>\n<#------------  END 字段循环遍历  ---------->\n}\n','domain'),('1297099467986157570','package ${packagePath}.service;\n\nimport ${packagePath}.domain.${domainName};\nimport ${packagePath}.mapper.${mapperName};\nimport com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\nimport org.springframework.stereotype.Service;\n\n/**\n * @author ${author}\n * @since ${date}\n */\n@Service\npublic class ${serviceName} extends ServiceImpl<${mapperName}, ${domainName}> {\n\n}\n','service'),('1297099520280739842','package ${packagePath}.mapper;\n\nimport ${packagePath}.domain.${domainName};\nimport com.baomidou.mybatisplus.core.mapper.BaseMapper;\n\n/**\n * @author ${author}\n * @since ${date}\n */\npublic interface ${mapperName} extends BaseMapper<${domainName}> {\n\n}\n','mapper'),('1297099546776158209','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n<mapper namespace=\"${packagePath}.mapper.${mapperName}\">\n\n</mapper> \n','xml'),('1297099731694632962','package ${packagePath}.controller;\n\nimport io.swagger.v3.oas.annotations.Operation;\nimport io.swagger.v3.oas.annotations.Parameter;\nimport io.swagger.v3.oas.annotations.tags.Tag;\nimport javax.annotation.Resource;\n\nimport org.springframework.web.bind.annotation.*;\nimport org.springframework.validation.annotation.Validated;\n\nimport site.syksy.qingzhou.web.exception.PreconditionFailedException;\nimport site.syksy.qingzhou.web.page.MyPage;\nimport site.syksy.qingzhou.web.page.Pageable;\nimport site.syksy.qingzhou.web.page.PageableAsQueryParam;\n\nimport ${packagePath}.service.${serviceName};\nimport ${packagePath}.domain.${domainName};\n\n/**\n * @author ${author}\n * @since ${date}\n */\n@RestController\n@Tag(name = \"\")\n@Validated\n@RequestMapping(\"syksy/\")\npublic class ${controllerName} {\n\n	@Resource\n    ${serviceName} ${serviceName?uncap_first};\n\n    @PostMapping\n    @Operation(summary = \"新增\")\n    public void post(@RequestBody ${domainName} ${domainName?uncap_first}) {\n       	${serviceName?uncap_first}.save(${domainName?uncap_first});\n    }\n\n    @DeleteMapping(\"{id}\")\n    @Operation(summary = \"删除\")\n    public void delete(@PathVariable String id) {\n        ${domainName} ${domainName?uncap_first} = ${serviceName?uncap_first}.getById(id);\n        if(${domainName?uncap_first} == null){\n            throw new PreconditionFailedException(\"数据不存在\");\n        }\n        ${serviceName?uncap_first}.removeById(id);\n    }\n\n    @PutMapping(\"{id}\")\n    @Operation(summary = \"修改\")\n    public void put(@PathVariable String id,@RequestBody ${domainName} ${domainName?uncap_first}) {\n    	${domainName?uncap_first}.setId(id);\n        ${serviceName?uncap_first}.updateById(${domainName?uncap_first});\n    }\n\n    @GetMapping(\"{id}\")\n    @Operation(summary = \"获取\")\n    public ${domainName} get(@PathVariable String id) {\n        ${domainName} ${domainName?uncap_first} = ${serviceName?uncap_first}.getById(id);\n        return ${domainName?uncap_first};\n    }\n    \n    @GetMapping(\"page\")\n    @Operation(summary = \"分页\")\n    @PageableAsQueryParam\n    public MyPage<${domainName}> page(@Parameter(hidden = true) Pageable pageable) {\n        MyPage myPage = new MyPage<>(pageable);\n        return ${serviceName?uncap_first}.page(myPage);\n    }\n\n}','controller');
/*!40000 ALTER TABLE `s_code_generator_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_config`
--

DROP TABLE IF EXISTS `s_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_config` (
  `id` varchar(32) NOT NULL,
  `title` varchar(45) DEFAULT NULL,
  `description` text,
  `ui_schema` text,
  `configuration` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='全局配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_config`
--

LOCK TABLES `s_config` WRITE;
/*!40000 ALTER TABLE `s_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_setting_dic`
--

DROP TABLE IF EXISTS `s_setting_dic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_setting_dic` (
  `id` varchar(19) NOT NULL,
  `name` varchar(45) DEFAULT NULL COMMENT '名字',
  `genre` varchar(10) DEFAULT NULL COMMENT '类型（list或tree）',
  `remark` varchar(200) DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_setting_dic`
--

LOCK TABLES `s_setting_dic` WRITE;
/*!40000 ALTER TABLE `s_setting_dic` DISABLE KEYS */;
INSERT INTO `s_setting_dic` VALUES ('1354026717925912578','性别','list',NULL);
/*!40000 ALTER TABLE `s_setting_dic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_setting_dic_item_list`
--

DROP TABLE IF EXISTS `s_setting_dic_item_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_setting_dic_item_list` (
  `id` varchar(19) NOT NULL,
  `next_id` varchar(19) DEFAULT NULL COMMENT '下一项ID(排序)',
  `dic_id` varchar(19) DEFAULT NULL COMMENT '字典ID',
  `name` varchar(45) DEFAULT NULL COMMENT '名字',
  `code` varchar(45) DEFAULT NULL COMMENT '编码',
  `customize` varchar(200) DEFAULT NULL COMMENT '自定义',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='列表型字典项表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_setting_dic_item_list`
--

LOCK TABLES `s_setting_dic_item_list` WRITE;
/*!40000 ALTER TABLE `s_setting_dic_item_list` DISABLE KEYS */;
INSERT INTO `s_setting_dic_item_list` VALUES ('1354018617735049217',NULL,'1354026717925912578','男','man',NULL),('1354019336529702913',NULL,'1354026717925912578','女','woman',NULL);
/*!40000 ALTER TABLE `s_setting_dic_item_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_setting_dic_item_tree`
--

DROP TABLE IF EXISTS `s_setting_dic_item_tree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_setting_dic_item_tree` (
  `id` varchar(19) NOT NULL,
  `next_id` varchar(19) DEFAULT NULL COMMENT '下一项ID(排序)',
  `parent_id` varchar(19) DEFAULT NULL COMMENT '父ID',
  `dic_id` varchar(19) DEFAULT NULL COMMENT '字典ID',
  `name` varchar(45) DEFAULT NULL COMMENT '名字',
  `code` varchar(45) DEFAULT NULL COMMENT '字典ID',
  `customize` varchar(200) DEFAULT NULL COMMENT '自定义',
  `is_expand` tinyint(1) DEFAULT NULL COMMENT '是否展开',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='树型字典项表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_setting_dic_item_tree`
--

LOCK TABLES `s_setting_dic_item_tree` WRITE;
/*!40000 ALTER TABLE `s_setting_dic_item_tree` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_setting_dic_item_tree` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_upms_menu`
--

DROP TABLE IF EXISTS `s_upms_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_upms_menu` (
  `id` char(19) NOT NULL COMMENT '菜单id',
  `name` varchar(50) NOT NULL COMMENT '菜单名',
  `genre` varchar(10) NOT NULL COMMENT '类型',
  `path` varchar(100) DEFAULT NULL COMMENT '前端路由路径',
  `parent_id` char(19) DEFAULT NULL COMMENT '父ID',
  `next_id` char(19) DEFAULT NULL COMMENT '单链-下一个节点ID',
  `remark` varchar(140) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `is_expand` tinyint(1) DEFAULT NULL COMMENT '是否展开',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_upms_menu`
--

LOCK TABLES `s_upms_menu` WRITE;
/*!40000 ALTER TABLE `s_upms_menu` DISABLE KEYS */;
INSERT INTO `s_upms_menu` VALUES ('1322095098963574786','账户权限','directory','/upms','1356133625122975746','1322095699340443649',NULL,'2020-10-30 03:36:50','2021-02-01 14:55:04',0),('1322095285719154689','角色菜单','directory','/upms/role-menu','1322095098963574786','1343893269800570882','角色菜单角色菜单角色菜单角色菜单','2020-10-30 03:37:34','2021-01-09 15:21:44',0),('1322095699340443649','全局设置','directory','/setting','1356133625122975746','1322095736279678978',NULL,'2020-10-30 03:39:13','2021-02-01 14:55:39',0),('1322095736279678978','辅助开发','directory','/dev','1356133625122975746','1685842279438946305',NULL,'2020-10-30 03:39:21','2021-02-01 14:55:29',0),('1322096257514225665','组织账户','directory','/upms/org-user','1322095098963574786','1322095285719154689',NULL,'2020-10-30 03:41:26','2020-12-29 22:00:22',0),('1323878656298770434','组织-删除','action',NULL,'1322096257514225665','1323879047648305153','','2020-11-04 00:44:03','2020-12-11 14:55:59',1),('1323879047648305153','组织-新建','action',NULL,'1322096257514225665','1323879206390128641','','2020-11-04 00:45:36','2020-12-11 14:56:09',1),('1323879206390128641','组织-重命名','action',NULL,'1322096257514225665','1334693942190821378','','2020-11-04 00:46:14','2020-12-11 14:56:24',1),('1324985894321049601','代码模板','directory','/dev/template','1322095736279678978','1324986312258277377','','2020-11-07 02:03:49','2021-12-10 10:40:35',0),('1324986312258277377','代码生成','directory','/dev/generator','1322095736279678978',NULL,'string','2020-11-07 02:05:29','2020-12-08 15:04:31',0),('1334331723942895617','字典','directory','/setting/dictionary','1322095699340443649','1339138849547808769','树形字典和键值对字典','2020-12-03 11:00:48','2020-12-21 22:38:58',0),('1334692692753797122','角色-新建','action',NULL,'1322095285719154689','1334692758440792065',NULL,'2020-12-04 10:55:10','2021-01-08 23:34:30',0),('1334692758440792065','角色-编辑','action',NULL,'1322095285719154689','1334692806335549442',NULL,'2020-12-04 10:55:26','2020-12-11 13:14:50',NULL),('1334692806335549442','角色-删除','action',NULL,'1322095285719154689','1337236523532251138',NULL,'2020-12-04 10:55:37','2020-12-11 13:15:06',NULL),('1334693047336062977','菜单-新建','action',NULL,'1322095285719154689','1334693105175515137',NULL,'2020-12-04 10:56:35','2020-12-11 13:15:26',0),('1334693105175515137','菜单-编辑','action',NULL,'1322095285719154689','1334693156589293569',NULL,'2020-12-04 10:56:48','2020-12-11 13:15:41',NULL),('1334693156589293569','菜单-删除','action',NULL,'1322095285719154689','1337236416741076994',NULL,'2020-12-04 10:57:01','2020-12-11 13:15:52',NULL),('1334693942190821378','账户-新建','action',NULL,'1322096257514225665','1334694019781251074',NULL,'2020-12-04 11:00:08','2020-12-11 14:56:39',NULL),('1334694019781251074','账户-编辑','action',NULL,'1322096257514225665','1334694073380261890',NULL,'2020-12-04 11:00:26','2020-12-11 14:56:48',NULL),('1334694073380261890','账户-删除','action',NULL,'1322096257514225665','1334694420739936258',NULL,'2020-12-04 11:00:39','2020-12-11 14:57:02',NULL),('1334694420739936258','账户-重置密码','action',NULL,'1322096257514225665',NULL,NULL,'2020-12-04 11:02:02','2020-12-31 18:04:54',NULL),('1337236416741076994','菜单-详情','action',NULL,'1322095285719154689','1345231419229507585',NULL,'2020-12-11 11:23:01','2020-12-11 13:39:53',0),('1337236523532251138','角色-详情','action',NULL,'1322095285719154689','1337270262303944706',NULL,'2020-12-11 11:23:26','2020-12-11 13:40:00',0),('1337266902347673601','角色-菜单-勾选关联','action',NULL,'1322095285719154689','1334693047336062977',NULL,'2020-12-11 13:24:09','2020-12-11 13:24:09',NULL),('1337270262303944706','角色-详情-接口-添加','action',NULL,'1322095285719154689','1337270315844235266',NULL,'2020-12-11 13:37:30','2021-06-03 16:42:31',NULL),('1337270315844235266','角色-详情-接口-移除','action',NULL,'1322095285719154689','1400374216167079938',NULL,'2020-12-11 13:37:43','2021-06-03 16:42:45',NULL),('1339138849547808769','配置','directory','/setting/config','1322095699340443649',NULL,NULL,'2020-12-16 17:22:36','2020-12-21 22:39:43',0),('1339410151148507138','设置','action',NULL,'1324986312258277377','1344576626769235970',NULL,'2020-12-17 11:20:40','2020-12-17 11:20:40',NULL),('1343893269800570882','API资源','directory','/upms/http','1322095098963574786',NULL,NULL,'2020-12-29 20:14:58','2021-08-07 09:25:31',0),('1344576626769235970','预览','action',NULL,'1324986312258277377','1344576831895867393',NULL,'2020-12-31 17:30:23','2020-12-31 17:30:23',NULL),('1344576831895867393','保存','action',NULL,'1324986312258277377','1346095177659334657',NULL,'2020-12-31 17:31:12','2021-01-09 10:17:53',NULL),('1345231419229507585','菜单-关系图','action',NULL,'1322095285719154689','1356239756861009921',NULL,'2021-01-02 12:52:18','2021-01-02 12:52:18',0),('1346095177659334657','设置-更新','action',NULL,'1324986312258277377',NULL,NULL,'2021-01-04 22:04:34','2021-01-04 22:04:34',NULL),('1348566955279347714','类目-新建','action',NULL,'1334331723942895617','1348567307370196993',NULL,'2021-01-11 17:46:32','2021-01-11 17:46:32',NULL),('1348567307370196993','类目-编辑','action',NULL,'1334331723942895617','1353524811325931522',NULL,'2021-01-11 17:47:56','2021-01-25 15:22:29',NULL),('1353524811325931522','类目-删除','action',NULL,'1334331723942895617','1353603917385912322',NULL,'2021-01-25 10:07:17','2021-01-25 10:07:17',NULL),('1353603917385912322','项-列表-新建','action',NULL,'1334331723942895617','1353604070893244417',NULL,'2021-01-25 15:21:37','2021-01-25 15:21:49',NULL),('1353604070893244417','项-列表-编辑','action',NULL,'1334331723942895617','1353606297162698754',NULL,'2021-01-25 15:22:14','2021-01-25 15:30:47',NULL),('1353606297162698754','项-列表-删除','action',NULL,'1334331723942895617','1354989543692075009',NULL,'2021-01-25 15:31:05','2021-01-25 15:31:05',NULL),('1354989543692075009','项-树-新建','action',NULL,'1334331723942895617','1354993638070976514',NULL,'2021-01-29 11:07:36','2021-01-29 11:07:36',NULL),('1354993638070976514','项-树-编辑','action',NULL,'1334331723942895617','1354993706257776641',NULL,'2021-01-29 11:23:52','2021-01-29 11:23:52',NULL),('1354993706257776641','项-树-删除','action',NULL,'1334331723942895617',NULL,NULL,'2021-01-29 11:24:09','2021-01-29 11:24:09',NULL),('1356133625122975746','轻舟','directory',NULL,NULL,NULL,NULL,'2021-02-01 14:53:47','2021-02-01 14:53:47',1),('1356239756861009921','菜单-排序','action',NULL,'1322095285719154689','1423806050502533122',NULL,'2021-02-01 21:55:30','2021-02-01 21:55:30',0),('1360589035206103042','配置-清除全局缓存','action',NULL,'1339138849547808769',NULL,NULL,'2021-02-13 21:57:59','2021-08-06 23:16:22',1),('1400374216167079938','角色-详情-用户-添加','action',NULL,'1322095285719154689','1400374287591882754',NULL,'2021-06-03 16:50:05','2021-06-03 16:50:05',NULL),('1400374287591882754','角色-详情-用户-移除','action',NULL,'1322095285719154689','1337266902347673601',NULL,'2021-06-03 16:50:23','2021-06-03 16:50:23',NULL),('1423806050502533122','菜单-详情-接口-添加','action',NULL,'1322095285719154689','1423806608110084098',NULL,'2021-08-07 08:39:50','2021-08-07 08:39:50',NULL),('1423806608110084098','菜单-详情-接口-移除','action',NULL,'1322095285719154689',NULL,NULL,'2021-08-07 08:42:03','2021-08-07 08:42:03',NULL),('1423818257076412418','全局API资源扫描','action',NULL,'1343893269800570882','1423819205395963906',NULL,'2021-08-07 09:28:20','2021-08-07 09:28:20',NULL),('1423819205395963906','API资源-编辑','action',NULL,'1343893269800570882','1423819334656024578',NULL,'2021-08-07 09:32:07','2021-08-07 09:32:07',NULL),('1423819334656024578','API资源-删除','action',NULL,'1343893269800570882',NULL,NULL,'2021-08-07 09:32:37','2021-08-07 09:32:37',NULL),('1685842279438946305','文件管理','directory','/file','1356133625122975746',NULL,NULL,'2023-07-31 10:38:15','2023-07-31 10:38:15',NULL);
/*!40000 ALTER TABLE `s_upms_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_upms_org`
--

DROP TABLE IF EXISTS `s_upms_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_upms_org` (
  `id` varchar(19) NOT NULL COMMENT '机构id',
  `name` varchar(50) NOT NULL COMMENT '机构名称',
  `parent_id` varchar(19) DEFAULT NULL COMMENT '父机构',
  `next_id` varchar(19) DEFAULT NULL COMMENT '尾指针',
  `is_expand` tinyint(1) DEFAULT NULL COMMENT '是否展开',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织机构';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_upms_org`
--

LOCK TABLES `s_upms_org` WRITE;
/*!40000 ALTER TABLE `s_upms_org` DISABLE KEYS */;
INSERT INTO `s_upms_org` VALUES ('1334687421788737520','SYKSY',NULL,NULL,1,'2021-08-14 17:11:30','2020-09-22 22:47:47');
/*!40000 ALTER TABLE `s_upms_org` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_upms_role`
--

DROP TABLE IF EXISTS `s_upms_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_upms_role` (
  `id` char(19) NOT NULL COMMENT '权限id',
  `name` varchar(20) NOT NULL COMMENT '角色名称',
  `remark` varchar(140) DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_upms_role`
--

LOCK TABLES `s_upms_role` WRITE;
/*!40000 ALTER TABLE `s_upms_role` DISABLE KEYS */;
INSERT INTO `s_upms_role` VALUES ('1322045219860078594','管理员','全部权限',0,'2020-10-30 00:18:37','2021-12-09 19:17:44'),('1323603508056358914','贵族','',1,'2020-11-03 06:30:42','2020-12-04 10:50:16'),('1323604048010084353','骑士','',1,'2020-11-03 06:32:51','2020-12-04 10:50:24'),('1347183137167286273','平民','11',1,'2021-01-07 22:07:44','2021-03-04 18:25:01'),('1422173059992903681','ayuyuuyuyuyuyuyuyu',NULL,1,'2021-08-02 20:30:55','2021-08-06 23:48:00'),('1426015314206367746','管理员','备注',1,'2021-08-13 10:58:40','2021-08-13 11:10:58');
/*!40000 ALTER TABLE `s_upms_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_upms_role_menu`
--

DROP TABLE IF EXISTS `s_upms_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_upms_role_menu` (
  `id` varchar(19) NOT NULL,
  `role_id` varchar(19) NOT NULL COMMENT '角色id',
  `menu_id` varchar(19) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_upms_role_menu`
--

LOCK TABLES `s_upms_role_menu` WRITE;
/*!40000 ALTER TABLE `s_upms_role_menu` DISABLE KEYS */;
INSERT INTO `s_upms_role_menu` VALUES ('1348566955728138241','1322045219860078594','1348566955279347714'),('1353524812559056897','1322045219860078594','1353524811325931522'),('1353604071694356481','1322045219860078594','1353604070893244417'),('1353606297896701954','1322045219860078594','1353606297162698754'),('1356239757460795394','1322045219860078594','1356239756861009921'),('1360589035944300546','1322045219860078594','1360589035206103042'),('1400374217005940737','1322045219860078594','1400374216167079938'),('1422565894931886081','1322045219860078594','1323878656298770434'),('1422939676284620801','1322045219860078594','1337236523532251138'),('1423641070050304001','1322045219860078594','1337270262303944706'),('1423661993612632066','1322045219860078594','1334692806335549442'),('1423675827953950722','1322045219860078594','1334693047336062977'),('1423675835151376386','1322045219860078594','1400374287591882754'),('1423806051223953410','1322045219860078594','1423806050502533122'),('1423807053352230914','1322045219860078594','1423806608110084098'),('1423807482727325697','1322045219860078594','1356133625122975746'),('1423818257466482690','1322045219860078594','1423818257076412418'),('1423819206020915201','1322045219860078594','1423819205395963906'),('1423819335406804994','1322045219860078594','1423819334656024578'),('1423975306888306690','1322045219860078594','1323879047648305153'),('1423987161396502530','1322045219860078594','1334694073380261890'),('1424296114739257346','1322045219860078594','1334694420739936258'),('1424296245639290881','1322045219860078594','1345231419229507585'),('1424297500256280578','1322045219860078594','1348567307370196993'),('1424300207255887873','1322045219860078594','1354989543692075009'),('1424306994000470017','1322045219860078594','1353603917385912322'),('1424307001688629249','1322045219860078594','1354993638070976514'),('1424307006231060482','1322045219860078594','1354993706257776641'),('1427254037945905154','1322045219860078594','1334693942190821378'),('1430890134684954626','1322045219860078594','1334694019781251074'),('1468118961759637505','1322045219860078594','1334692692753797122'),('1685842279539609602','1322045219860078594','1685842279438946305'),('26994355004768430','1322045219860078594','1322095098963574786'),('26994355004768431','1322045219860078594','1322095285719154689'),('26994355004768432','1322045219860078594','1322095699340443649'),('26994355004768433','1322045219860078594','1322095736279678978'),('26994355004768434','1322045219860078594','1322096257514225665'),('26994355004768437','1322045219860078594','1323879206390128641'),('26994355004768438','1322045219860078594','1324985894321049601'),('26994355004768439','1322045219860078594','1324986312258277377'),('26994355004768440','1322045219860078594','1334331723942895617'),('26994355004768442','1322045219860078594','1334692758440792065'),('26994355004768445','1322045219860078594','1334693105175515137'),('26994355004768446','1322045219860078594','1334693156589293569'),('26994355004768454','1322045219860078594','1337236416741076994'),('26994355004768456','1322045219860078594','1337266902347673601'),('26994355004768458','1322045219860078594','1337270315844235266'),('26994355004768460','1322045219860078594','1339138849547808769'),('26994355004768461','1322045219860078594','1339410151148507138'),('26994355004768462','1322045219860078594','1343893269800570882'),('26994355004768463','1322045219860078594','1344576626769235970'),('26994355004768464','1322045219860078594','1344576831895867393'),('26994355004768466','1322045219860078594','1346095177659334657');
/*!40000 ALTER TABLE `s_upms_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_upms_route`
--

DROP TABLE IF EXISTS `s_upms_route`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_upms_route` (
  `id` varchar(19) NOT NULL,
  `pattern` varchar(200) NOT NULL COMMENT '路径模板',
  `request_method` varchar(7) NOT NULL COMMENT '请求方法（GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE,*）',
  `handler_method` varchar(200) DEFAULT NULL COMMENT '处理方法',
  `online` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否在线',
  `check_type` tinyint(1) DEFAULT '1' COMMENT '校验类型',
  `on_off` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否打开',
  `start_date` datetime DEFAULT NULL COMMENT '有效期开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '有效期结束时间',
  `remark` varchar(140) DEFAULT NULL COMMENT '备注',
  `handler_method_hash` char(128) DEFAULT NULL COMMENT '处理方法md5',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='请求路由表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_upms_route`
--

LOCK TABLES `s_upms_route` WRITE;
/*!40000 ALTER TABLE `s_upms_route` DISABLE KEYS */;
INSERT INTO `s_upms_route` VALUES ('1323876646849667074','/qz/api/generator/code','PUT','site.syksy.qingzhou.generator.controller.GeneratorController#putCode(List)',1,0,1,NULL,NULL,NULL,'689eb7b8b2554337acbd2cda2d1cca15'),('1323876647508172802','/qz/api/generator/code/record','GET','site.syksy.qingzhou.generator.controller.GeneratorController#getCodeGeneratorRecord(String)',1,1,1,NULL,NULL,NULL,'64965d7d8c9c3efa5e2e5d372c55957e'),('1323876647831134209','/qz/api/generator/code/record','DELETE','site.syksy.qingzhou.generator.controller.GeneratorController#deleteCodeGeneratorRecord(String)',1,1,1,NULL,NULL,NULL,'058adb29c57f198a9ca99c8482a438ae'),('1323876648154095618','/qz/api/generator/template/{genre}','PUT','site.syksy.qingzhou.generator.controller.TemplateController#putCodeTemplate(String, String)',1,1,1,NULL,NULL,NULL,'3431661a6e46e85d7fbf2f5fa2687067'),('1323876648544165890','/qz/api/generator/template/{genre}','GET','site.syksy.qingzhou.generator.controller.TemplateController#getCodeTemplate(String)',1,1,1,NULL,NULL,NULL,'ccdd0cbbfc2fbb40a1e302ceb679eaf5'),('1323876648871321602','/qz/api/database/tables/{tableName}','GET','site.syksy.qingzhou.database.controller.TablesController#getTable(String)',1,1,1,NULL,NULL,NULL,'3c63cc2e2362b014c99bb68fa1068647'),('1323876649198477314','/qz/api/database/tables','GET','site.syksy.qingzhou.database.controller.TablesController#getAllTable()',1,1,1,NULL,NULL,NULL,'f8ce2f404e862287b26d36615264dc18'),('1323876649525633025','/qz/api/upms/menu/{id}','GET','site.syksy.qingzhou.upms.controller.MenuController#get(String)',1,1,1,NULL,NULL,NULL,'6620f6d21da239698b2a544b0baacdb0'),('1323876650242859009','/qz/api/upms/menu/{id}','DELETE','site.syksy.qingzhou.upms.controller.MenuController#delete(String)',1,1,1,NULL,NULL,NULL,'500cca51bc225525b147dc1a831b5d11'),('1323876651857666050','/qz/api/upms/org/{id}','GET','site.syksy.qingzhou.upms.controller.OrgController#get(String)',1,1,1,NULL,NULL,NULL,'f8b5d301ae698e07e95d0b2d3ade622b'),('1323876652247736322','/qz/api/upms/org/{id}','PUT','site.syksy.qingzhou.upms.controller.OrgController#put(String, String)',1,1,1,NULL,NULL,NULL,'af76c716137611d6a43da7532d08007f'),('1323876652574892034','/qz/api/upms/org/{id}','DELETE','site.syksy.qingzhou.upms.controller.OrgController#delete(String)',1,1,1,NULL,NULL,NULL,'a661bf0739746554130b29a1e04334c2'),('1323876652897853442','/qz/api/upms/org','POST','site.syksy.qingzhou.upms.controller.OrgController#post(OrgAddVO)',1,1,1,NULL,NULL,NULL,'738d853a52d593ad4cbf1ffa3019549d'),('1323876653560553474','/qz/api/upms/org/position','PUT','site.syksy.qingzhou.upms.controller.OrgController#drag(String, String, Boolean, Boolean)',1,1,1,NULL,NULL,NULL,'cf7342287ceb582f84168035b1077f6b'),('1323876654286168065','/qz/api/upms/role/{id}','GET','site.syksy.qingzhou.upms.controller.RoleController#get(String)',1,1,1,NULL,NULL,NULL,'4a71567208ba411319dc8676aca76889'),('1323876654672044033','/qz/api/upms/role/{id}','PUT','site.syksy.qingzhou.upms.controller.RoleController#put(String, RoleEdit)',1,1,1,NULL,NULL,NULL,'a30283da96591098a3c77780092d1009'),('1323876654999199745','/qz/api/upms/role/{id}','DELETE','site.syksy.qingzhou.upms.controller.RoleController#delete(String)',1,1,1,NULL,NULL,NULL,'79984552e6e73db5687c681312698919'),('1323876655330549762','/qz/api/upms/role','POST','site.syksy.qingzhou.upms.controller.RoleController#post(RoleEdit)',1,1,1,NULL,NULL,NULL,'0f76b540c4924f3890bf3dfeff23a369'),('1323876655729008642','/qz/api/upms/role','GET','site.syksy.qingzhou.upms.controller.RoleController#getList()',1,1,1,NULL,NULL,NULL,'fd513ea6e938c7f28f1b1b3749bc2c29'),('1323876656374931457','/qz/api/upms/role/menu','DELETE','site.syksy.qingzhou.upms.controller.RoleMenuController#delete(String, String)',1,1,1,NULL,NULL,NULL,'a64a817d75809ecf50ebf2bf37762da5'),('1323876656702087170','/qz/api/upms/role/menu','POST','site.syksy.qingzhou.upms.controller.RoleMenuController#post(String, String)',1,1,1,NULL,NULL,NULL,'c4e0f028aad16ff0b9d4e7930145fc9b'),('1323876657356398593','/qz/api/upms/route/{id}','GET','site.syksy.qingzhou.upms.controller.RouteController#getRoute(String)',1,1,1,NULL,NULL,NULL,'143653d353ebefed000677d3a50de2df'),('1323876657675165697','/qz/api/upms/route/{id}','DELETE','site.syksy.qingzhou.upms.controller.RouteController#deleteRoute(String)',1,1,1,NULL,NULL,NULL,'c964d1c467f0b3618880f8c15b00038c'),('1323876658002321409','/qz/api/upms/route/{id}','PUT','site.syksy.qingzhou.upms.controller.RouteController#putRoute(String, RouteEditVO)',1,1,1,NULL,NULL,NULL,'612ee78023fec25c3caf2d72dcb82d34'),('1323876658388197377','/qz/api/upms/route/link','GET','site.syksy.qingzhou.upms.controller.RouteLinkController#get(String, String)',1,1,1,NULL,NULL,NULL,'62b185a4e592c3aac19be347b9048e62'),('1323876658711158785','/qz/api/upms/route/link','DELETE','site.syksy.qingzhou.upms.controller.RouteLinkController#delete(String, String, String)',1,1,1,NULL,NULL,NULL,'eb81e74f421299aa18248ca55fba8014'),('1323876659034120193','/qz/api/upms/route/link','POST','site.syksy.qingzhou.upms.controller.RouteLinkController#post(String, String, String)',1,1,1,NULL,NULL,NULL,'f1a4a139dc55a402acc611a8a9ce6859'),('1323876659357081602','/qz/api/upms/user/{id}','GET','site.syksy.qingzhou.upms.controller.UserController#get(String)',1,1,1,NULL,NULL,NULL,'1f2b25d5bcc9d54138e001eb9aa52749'),('1334716358870867969','/qz/api/upms/user','POST','site.syksy.qingzhou.upms.controller.UserController#post(UserVO)',1,1,1,NULL,NULL,NULL,'3762024893054fcaecb27866be003cae'),('1334716359198023682','/qz/api/upms/menu/expand/{id}','PUT','site.syksy.qingzhou.upms.controller.MenuController#expand(String, Boolean)',1,1,1,NULL,NULL,NULL,'a29ef4cc4781fbfbffadce1c4f26e3f0'),('1334716359843946498','/qz/api/upms/org/expand/{id}','PUT','site.syksy.qingzhou.upms.controller.OrgController#expand(String, Boolean)',1,1,1,NULL,NULL,NULL,'f181a581ed3b214884c7f00e26179192'),('1334716360238211073','/qz/api/upms/role/menu/{roleId}','GET','site.syksy.qingzhou.upms.controller.RoleMenuController#get(String)',1,1,1,NULL,NULL,NULL,'6260f32af12d6628434cf740d2c5474f'),('1334716360892522498','/qz/api/upms/menu/current','GET','site.syksy.qingzhou.upms.controller.MenuController#getCurrentMenu(String)',1,0,1,NULL,NULL,NULL,'cf2ceae9fc830d9fd8ac23ca4e77424e'),('1334716361999818754','/qz/api/upms/route/overload','POST','site.syksy.qingzhou.upms.controller.RouteController#overload()',1,1,1,NULL,NULL,NULL,'c707e875f79f66a489dd3968c9dac482'),('1334716362326974466','/qz/api/upms/route/list','GET','site.syksy.qingzhou.upms.controller.RouteController#listToList(Pageable, String, String, String)',1,1,1,NULL,NULL,NULL,'e801e2a810c826645af1db882a44b4b7'),('1334716363044200450','/qz/api/upms/user/{id}','PUT','site.syksy.qingzhou.upms.controller.UserController#put(String, UserVO)',1,1,1,NULL,NULL,NULL,'3aa83c5a3aa66635dc31090a2dc20106'),('1336506305286299650','/qz/api/upms/org/list','GET','site.syksy.qingzhou.upms.controller.OrgController#list()',1,1,1,NULL,NULL,NULL,'15992c68557ceb70cabc9e0142af7bd0'),('1338707808813551617','/qz/api/upms/user/current','GET','site.syksy.qingzhou.upms.controller.UserController#getCurrentUser(String)',1,0,1,NULL,NULL,NULL,'02f8f1d5d05cb4d177ad12f3e9e43107'),('1338707809182650369','/qz/api/generator/template','GET','site.syksy.qingzhou.generator.controller.TemplateController#getCodeTemplateAll()',1,1,1,NULL,NULL,NULL,'2136b1df0a5d7f78c61ed0ef29d4617b'),('1339410326743044097','/qz/api/generator/configuration/list','GET','site.syksy.qingzhou.generator.controller.ConfigurationController#listToList(Pageable, String)',1,1,1,NULL,NULL,NULL,'f36a116a1bf110565b1b15e8f6d441c0'),('1339410327502213121','/qz/api/generator/configuration','GET','site.syksy.qingzhou.generator.controller.ConfigurationController#getCodeConfiguration(String)',1,1,1,NULL,NULL,NULL,'ff2e69c4aed148b395831c5b489daebf'),('1342115648490270722','/qz/api/generator/code/preview','POST','site.syksy.qingzhou.generator.controller.GeneratorController#postPreview(ClassNameVO, String)',1,1,1,NULL,NULL,NULL,'bf97791ad27fbe1f25a0d6828960da47'),('1342115648905506818','/qz/api/setting/config/{id}','PUT','site.syksy.qingzhou.setting.controller.ConfigController#put(String, ConfigDO)',1,1,1,NULL,NULL,NULL,'f82ca77691ad6471c718ceea80f2692e'),('1342115649236856834','/qz/api/generator/code','POST','site.syksy.qingzhou.generator.controller.GeneratorController#postCode(ClassNameVO, String)',1,1,1,NULL,NULL,NULL,'bed3e55d89b060b8befda381275ea547'),('1342115650063134721','/qz/api/setting/config/{id}','DELETE','site.syksy.qingzhou.setting.controller.ConfigController#delete(String)',1,1,1,NULL,NULL,NULL,'0bba7e0398c84afc40bf0b63d1c4a4fd'),('1342115650524508161','/qz/api/setting/config/{id}','GET','site.syksy.qingzhou.setting.controller.ConfigController#get(String)',1,1,1,NULL,NULL,NULL,'38b3ad6224b93bc8e8370e7358e7bf9b'),('1342115650864246785','/qz/api/setting/config','POST','site.syksy.qingzhou.setting.controller.ConfigController#post(ConfigDO)',1,1,1,NULL,NULL,NULL,'b879a751db701ec6c3c4463c6b04ae7e'),('1344585156685492225','/qz/api/upms/user/password/{id}','PUT','site.syksy.qingzhou.upms.controller.UserController#resetPassword(String)',1,1,1,NULL,NULL,NULL,'65c8a3bf634bfc3e9c49b5841d22c17d'),('1344996756009144322','/qz/api/upms/relation/menu/{id}','GET','site.syksy.qingzhou.upms.controller.RelationController#menu(String)',1,1,1,NULL,NULL,NULL,'4a89947f2d4bb75c5d940120df49ce02'),('1344996756361465858','/qz/api/upms/relation/role/{id}','GET','site.syksy.qingzhou.upms.controller.RelationController#role(String)',1,1,1,NULL,NULL,NULL,'e0d263ba2b9ec1d10d6ce6725c9f02c3'),('1344996756726370305','/qz/api/upms/relation/user/{id}','GET','site.syksy.qingzhou.upms.controller.RelationController#user(String)',1,1,1,NULL,NULL,NULL,'a46474d9c832e9823b76797144262458'),('1346100318672289793','/qz/api/setting/dic/{id}','PUT','site.syksy.qingzhou.setting.controller.DicController#put(String, DicDO)',1,1,1,NULL,NULL,NULL,'bbba33a74df5bf7960279ca867773740'),('1346100318957502465','/qz/api/setting/dic/item/list/{id}','PUT','site.syksy.qingzhou.setting.controller.DicItemListController#put(String, DicItemListDO)',1,1,1,NULL,NULL,NULL,'b6040580919a88f3980b6bd2ce210433'),('1346100319255298050','/qz/api/setting/dic/item/tree/{id}','GET','site.syksy.qingzhou.setting.controller.DicItemTreeController#get(String)',1,1,1,NULL,NULL,NULL,'6983a9d28a3a31e3361380437a9c398d'),('1346100319532122114','/qz/api/setting/dic/{id}','DELETE','site.syksy.qingzhou.setting.controller.DicController#delete(String)',1,1,1,NULL,NULL,NULL,'b4a3456e720cbc758ead0e9b0fe25e03'),('1346100319796363265','/qz/api/setting/dic/item/list/{id}','GET','site.syksy.qingzhou.setting.controller.DicItemListController#get(String)',1,1,1,NULL,NULL,NULL,'06d135802f193111b5a91d92420cc87b'),('1346100320073187329','/qz/api/setting/dic/item/list/{id}','DELETE','site.syksy.qingzhou.setting.controller.DicItemListController#delete(String)',1,1,1,NULL,NULL,NULL,'5a2cc87f42deac6c2db67e3e45d46e5d'),('1346100320341622786','/qz/api/setting/dic/{id}','GET','site.syksy.qingzhou.setting.controller.DicController#get(String)',1,1,1,NULL,NULL,NULL,'df3d1cf41124b618b5e19d57a7de60e3'),('1346100320618446849','/qz/api/setting/config/list','GET','site.syksy.qingzhou.setting.controller.ConfigController#list()',1,1,1,NULL,NULL,NULL,'9dee404b80f914330886713c87396218'),('1346100320882688002','/qz/api/setting/dic/item/tree/{id}','DELETE','site.syksy.qingzhou.setting.controller.DicItemTreeController#delete(String)',1,1,1,NULL,NULL,NULL,'d56892f7d6f2d582970b0dfe1489b244'),('1346100321142734850','/qz/api/setting/dic/item/list','POST','site.syksy.qingzhou.setting.controller.DicItemListController#post(DicItemListDO)',1,1,1,NULL,NULL,NULL,'f09da7e7283e909808f1b7b5dc5912c2'),('1346100321406976002','/qz/api/setting/dic/item/tree','POST','site.syksy.qingzhou.setting.controller.DicItemTreeController#post(DicItemTreeDO)',1,1,1,NULL,NULL,NULL,'88b2cb7b8f360c16ca8acf7f9805b5ae'),('1346100321675411458','/qz/api/setting/dic/item/tree/{id}','PUT','site.syksy.qingzhou.setting.controller.DicItemTreeController#put(String, DicItemTreeDO)',1,1,1,NULL,NULL,NULL,'98863b0395867decc00a80605b12d0e0'),('1346100321939652609','/qz/api/setting/dic','POST','site.syksy.qingzhou.setting.controller.DicController#post(DicDO)',1,1,1,NULL,NULL,NULL,'4642a37a2c90b3a6268a24f9f02787ae'),('1347140146591039489','/qz/api/setting/dic/item/list/list','GET','site.syksy.qingzhou.setting.controller.DicItemListController#list(Pageable, String)',1,1,1,NULL,NULL,NULL,'3ddc28aaca880ba8ab156cff73c61d18'),('1348561032435560450','/qz/api/upms/menu/{id}','PUT','site.syksy.qingzhou.upms.controller.MenuController#put(String, MenuAddVO)',1,1,1,NULL,NULL,NULL,'e79479b9edb7c5330090cb07fe4f3610'),('1353635612013166593','/qz/api/setting/dic/page','GET','site.syksy.qingzhou.setting.controller.DicController#page(Pageable, String, String)',1,1,1,NULL,NULL,NULL,'3bdda2ec44462af3729a01bf19285b3f'),('1353635612378071042','/qz/api/setting/dic','GET','site.syksy.qingzhou.setting.controller.DicController#list(String)',1,1,1,NULL,NULL,NULL,'112527f0550f52146c60c2fa994be4f5'),('1354684417772953602','/qz/api/setting/dic/item/tree','GET','site.syksy.qingzhou.setting.controller.DicItemTreeController#list(String)',1,1,1,NULL,NULL,NULL,'084af79901bf50048ed4663fab9c9cba'),('1355067668714201090','/qz/api/setting/dic/item/tree/level/{id}','GET','site.syksy.qingzhou.setting.controller.DicItemTreeController#getLevel(String, String)',1,1,1,NULL,NULL,NULL,'f8b65c71745b05904d6f298ccfdc03c2'),('1355075202887884801','/qz/api/setting/dic/item/tree/expand/{id}','PUT','site.syksy.qingzhou.setting.controller.DicItemTreeController#expand(String, Boolean)',1,1,1,NULL,NULL,NULL,'e00150806a99505788b5b8552bdbef64'),('1355429246129291265','/qz/api/upms/org/level/{id}','GET','site.syksy.qingzhou.upms.controller.OrgController#getLevel(String)',1,1,1,NULL,NULL,NULL,'ff0523c50c2362fb0328ecfb1b00ceab'),('1355847571485380610','/qz/api/upms/menu','POST','site.syksy.qingzhou.upms.controller.MenuController#post(MenuAddVO)',1,1,1,NULL,NULL,NULL,'0c282becb4608096d6c50b5be0a5b5cf'),('1355847571749621761','/qz/api/upms/menu/level/{id}','GET','site.syksy.qingzhou.upms.controller.MenuController#getLevel(String)',1,1,1,NULL,NULL,NULL,'acfd89576c54495b491412ef364e7c77'),('1355847572018057218','/qz/api/upms/menu','GET','site.syksy.qingzhou.upms.controller.MenuController#list()',1,1,1,NULL,NULL,NULL,'8953e288ef7c1b80f492ad91f6c2eef5'),('1356239395962122242','/qz/api/upms/menu/position/{id}','PUT','site.syksy.qingzhou.upms.controller.MenuController#move(String, String)',1,1,1,NULL,NULL,NULL,'ed6ebbcea5fc3e2e6443a7e0a3626298'),('1357219386651762689','/qz/api/upms/menu/directory','GET','site.syksy.qingzhou.upms.controller.MenuController#listDirectory(String)',1,1,1,NULL,NULL,NULL,'dc9e0ac4747e132a8ff289ec614356be'),('1360588677536829441','/qz/api/generator/configuration','PUT','site.syksy.qingzhou.generator.controller.ConfigurationController#putCodeConfiguration(ConfigurationVO, String)',1,1,1,NULL,NULL,NULL,'b4f5efd7cb90e8c5a54d9a851e69c030'),('1360588678581211138','/qz/api/setting/cache','DELETE','site.syksy.qingzhou.setting.controller.CacheController#delete()',1,1,1,NULL,NULL,NULL,'6afba47c87c14df42cb8713d70df75e4'),('1380794138391334913','/qz/api/upms/user/password','PUT','site.syksy.qingzhou.upms.controller.UserController#editPassword(String, PasswordVO)',1,0,1,NULL,NULL,NULL,'b2593626cea7e591298857d1dafbcd87'),('1400371875640590337','/qz/api/upms/role/{id}/{userId}','DELETE','site.syksy.qingzhou.upms.controller.RoleController#deleteUser(String, String)',1,1,1,NULL,NULL,NULL,'8bbf7cb8384bb0d7e1d66716a57d6e9e'),('1408072652513345538','/qz/api/upms/user/list','GET','site.syksy.qingzhou.upms.controller.UserController#listToList(Pageable, String, String, String, String, String, String)',1,1,1,NULL,NULL,NULL,'e2c3625ca69475750d1e55504422fa33'),('1408072917727686657','/qz/api/upms/role/{id}','POST','site.syksy.qingzhou.upms.controller.RoleController#addUser(String, List)',1,1,1,NULL,NULL,NULL,'5f306fae3bad3bc4390dbe290e53aff0'),('1465317886754086913','/v3/api-docs/{group}','GET','org.springdoc.webmvc.api.MultipleOpenApiWebMvcResource#openapiJson(HttpServletRequest, String, String, Locale)',1,-1,1,NULL,NULL,NULL,'ae2be9b567df97edb83adef8f759bed6'),('1465317887039299586','/v3/api-docs','GET','org.springdoc.webmvc.api.OpenApiWebMvcResource#openapiJson(HttpServletRequest, String, Locale)',1,-1,1,NULL,NULL,NULL,'9fedd9189d6e430e113024b44284a103'),('1465317887303540738','/swagger-ui.html','GET','org.springdoc.webmvc.ui.SwaggerWelcomeWebMvc#redirectToUi(HttpServletRequest)',1,-1,1,NULL,NULL,NULL,'f4a972263025d1fa203bf3b9502e8d4d'),('1465317887567781889','/v3/api-docs.yaml/{group}','GET','org.springdoc.webmvc.api.MultipleOpenApiWebMvcResource#openapiYaml(HttpServletRequest, String, String, Locale)',1,-1,1,NULL,NULL,NULL,'3fd0ae5d14454a76e0e0e2972a9fdb70'),('1465317887815245825','/qz/api/error','*','site.syksy.qingzhou.web.exception.ExceptionController#error(HttpServletRequest)',1,-1,1,NULL,NULL,NULL,'67341bb16833495e7622f8e76d94f7bd'),('1465317888071098370','/v3/api-docs/swagger-config','GET','org.springdoc.webmvc.ui.SwaggerConfigResource#openapiJson(HttpServletRequest)',1,-1,1,NULL,NULL,NULL,'01f1ea271f7ec08540dba803fb317d97'),('1465317888322756609','/v3/api-docs.yaml','GET','org.springdoc.webmvc.api.OpenApiWebMvcResource#openapiYaml(HttpServletRequest, String, Locale)',1,-1,1,NULL,NULL,NULL,'ce516c29b1b6efb990a9aca30c2ed34e'),('1468908235673886722','/qz/api/upms/user/{id}','DELETE','site.syksy.qingzhou.upms.controller.UserController#delete(String, String)',1,1,1,NULL,NULL,NULL,'149cd04c992c1eddb8700c6956e3e7b7');
/*!40000 ALTER TABLE `s_upms_route` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_upms_route_link`
--

DROP TABLE IF EXISTS `s_upms_route_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_upms_route_link` (
  `id` varchar(19) NOT NULL,
  `route_id` varchar(19) NOT NULL COMMENT '路由id',
  `target_id` varchar(19) NOT NULL COMMENT '链接id',
  `target_type` varchar(20) NOT NULL COMMENT '链接类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色菜单操作路由关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_upms_route_link`
--

LOCK TABLES `s_upms_route_link` WRITE;
/*!40000 ALTER TABLE `s_upms_route_link` DISABLE KEYS */;
INSERT INTO `s_upms_route_link` VALUES ('100','1355847572018057218','1322095285719154689','menu'),('1324985895046664194','1323876648154095618','1324985894321049601','menu'),('1324985895516426242','1323876648544165890','1324985894321049601','menu'),('1324986313583677441','1323876646849667074','1324986312258277377','menu'),('1324986314380595201','1323876647508172802','1324986312258277377','menu'),('1324986314779054082','1323876647831134209','1324986312258277377','menu'),('1333690585535918081','1323876646849667074','1322095699340443649','menu'),('1333690585795964929','1323876647508172802','1322095699340443649','menu'),('1333690586064400385','1323876647831134209','1322095699340443649','menu'),('1334696895362519041','1323876655330549762','1334692692753797122','menu'),('1334697007111360513','1323876654672044033','1334692758440792065','menu'),('1334697068918624257','1323876654999199745','1334692806335549442','menu'),('1334717043129622530','1323876655729008642','1322095285719154689','menu'),('1337235556246056962','1334716360238211073','1322095285719154689','menu'),('1337237218998185985','1323876658388197377','1337236416741076994','menu'),('1337237334358323201','1323876658388197377','1337236523532251138','menu'),('1337239424560394241','1323876649525633025','1337236416741076994','menu'),('1337239646833340418','1323876654286168065','1337236523532251138','menu'),('1337262948834471938','1336506305286299650','1322096257514225665','menu'),('1337263512657981442','1334716359198023682','1322095285719154689','menu'),('1337266062077587458','1323876650242859009','1334693156589293569','menu'),('1337267262294454273','1323876656702087170','1337266902347673601','menu'),('1337267264265777153','1323876656374931457','1337266902347673601','menu'),('1337270635076907009','1323876659034120193','1337270262303944706','menu'),('1337270694573109249','1323876658711158785','1337270315844235266','menu'),('1337271271231188993','1334716362326974466','1337270262303944706','menu'),('1337290745728491522','1334716359843946498','1322096257514225665','menu'),('1337290917975973890','1334716359843946498','1322096257514225665','menu'),('1337291038792900609','1334716359843946498','1322096257514225665','menu'),('1338320182277238786','1323876659357081602','1334694019781251074','menu'),('1338322220260192257','1334716358870867969','1334693942190821378','menu'),('1338358344143736833','1323876652247736322','1323879206390128641','menu'),('1338358429011283969','1323876652897853442','1323879047648305153','menu'),('1338358483579179009','1323876652574892034','1323878656298770434','menu'),('1338707935611555842','1338707809182650369','1324985894321049601','menu'),('1338796784832397314','1323876649198477314','1324986312258277377','menu'),('1338796787378339841','1323876648871321602','1324986312258277377','menu'),('1339410436956770306','1339410326743044097','1339410151148507138','menu'),('1340617858205925378','1334716363044200450','1334694019781251074','menu'),('1343920375582445570','1334716362326974466','1343893269800570882','menu'),('1343920376496803842','1323876657675165697','1343893269800570882','menu'),('1343920377562157058','1323876657356398593','1343893269800570882','menu'),('1343920378568790017','1323876658002321409','1343893269800570882','menu'),('1343920379437010946','1334716361999818754','1343893269800570882','menu'),('1344189525007503362','1339410327502213121','1324986312258277377','menu'),('1344576754158637058','1342115648490270722','1344576626769235970','menu'),('1344577077174571010','1342115649236856834','1344576831895867393','menu'),('1344585351745794050','1344585156685492225','1334694420739936258','menu'),('1345231537076867074','1344996756009144322','1345231419229507585','menu'),('1346095703419535362','1339410327502213121','1339410151148507138','menu'),('1347140601840795649','1347140146591039489','1334331723942895617','menu'),('1347167722487705601','1323876648154095618','1322095699340443649','menu'),('1348562035134263297','1348561032435560450','1334693105175515137','menu'),('1348567199438172161','1346100321939652609','1348566955279347714','menu'),('1348567431899082753','1346100318672289793','1348567307370196993','menu'),('1353524962232795138','1346100319532122114','1353524811325931522','menu'),('1353525314319450114','1346100320341622786','1334331723942895617','menu'),('1353606583935651842','1346100321142734850','1353603917385912322','menu'),('1353606707403378689','1346100319796363265','1353604070893244417','menu'),('1353606708678447105','1346100318957502465','1353604070893244417','menu'),('1353606785480347650','1346100320073187329','1353606297162698754','menu'),('1353636270674083841','1353635612013166593','1334331723942895617','menu'),('1353639557448138753','1353635612378071042','1334331723942895617','menu'),('1354990108916482050','1346100321406976002','1354989543692075009','menu'),('1354990327343251458','1354684417772953602','1334331723942895617','menu'),('1354994008583208962','1346100321675411458','1354993638070976514','menu'),('1354994011842183170','1346100319255298050','1354993638070976514','menu'),('1354994067458654210','1346100320882688002','1354993706257776641','menu'),('1355067830136184834','1355067668714201090','1354989543692075009','menu'),('1355075312476659713','1355075202887884801','1334331723942895617','menu'),('1355429432192811009','1355429246129291265','1334693942190821378','menu'),('1355851655718281218','1355847571749621761','1322095285719154689','menu'),('1356074854879309826','1355847571485380610','1334693047336062977','menu'),('1356076090240249858','1355847571749621761','1334693047336062977','menu'),('1356239844937199617','1356239395962122242','1356239756861009921','menu'),('1360589110892318721','1360588678581211138','1360589035206103042','menu'),('1400374925423886337','1400371875640590337','1400374287591882754','menu'),('1408073236524150786','1408072917727686657','1400374216167079938','menu'),('1460621285464809474','1357219386651762689','1334693047336062977','menu'),('1465252007814995970','1408072652513345538','1322096257514225665','menu'),('1468908418360991745','1468908235673886722','1334694073380261890','menu'),('1468919954123096065','1360588677536829441','1346095177659334657','menu');
/*!40000 ALTER TABLE `s_upms_route_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_upms_user`
--

DROP TABLE IF EXISTS `s_upms_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_upms_user` (
  `id` varchar(19) NOT NULL COMMENT '用户id',
  `username` varchar(45) NOT NULL COMMENT '用户名',
  `password` varchar(60) NOT NULL COMMENT '登录名',
  `email` varchar(45) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(15) DEFAULT NULL COMMENT '电话',
  `on_off` tinyint(1) DEFAULT NULL COMMENT '账户启用或停用',
  `avatar` varchar(19) DEFAULT NULL COMMENT '头像',
  `org_id` varchar(19) DEFAULT NULL COMMENT '机构id',
  `remark` varchar(150) DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `name` varchar(45) DEFAULT NULL COMMENT '名字',
  `openid` varchar(128) DEFAULT NULL COMMENT '微信小程序openid',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_upms_user`
--

LOCK TABLES `s_upms_user` WRITE;
/*!40000 ALTER TABLE `s_upms_user` DISABLE KEYS */;
INSERT INTO `s_upms_user` VALUES ('1322014259407925249','admin','$2a$10$QCipxSAvSjKKc0YDgzYmau7veYyKaG9mBrpKDiZR/NvcInMb2wxUC','admin@syksy.site','13888489966',1,NULL,'1334687421788737520','管理员拥有全部权限',0,'2020-10-29 13:01:05','2021-12-09 20:32:33','全境守护者',NULL),('1468921595744321538','qqq','$2a$10$wZ.Sf0ScAtMPtvuuE/1E1ukPigHgc6fa3V7d1hwYwIR1uwo1svpEq',NULL,NULL,1,NULL,'1334688228747661313',NULL,1,'2021-12-09 20:32:54','2021-12-09 20:32:54',NULL,NULL);
/*!40000 ALTER TABLE `s_upms_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_upms_user_role`
--

DROP TABLE IF EXISTS `s_upms_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_upms_user_role` (
  `id` varchar(19) NOT NULL,
  `user_id` varchar(19) NOT NULL COMMENT '用户id',
  `role_id` varchar(19) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_upms_user_role`
--

LOCK TABLES `s_upms_user_role` WRITE;
/*!40000 ALTER TABLE `s_upms_user_role` DISABLE KEYS */;
INSERT INTO `s_upms_user_role` VALUES ('1322045219860078599','1322014259407925249','1322045219860078594');
/*!40000 ALTER TABLE `s_upms_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-31 12:38:36
