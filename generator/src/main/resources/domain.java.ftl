package ${packagePath}.domain;

<#list table.importPackages as pkg>
import ${pkg};
</#list>
import site.syksy.qingzhou.database.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author ${author}
 */
@Data
@TableName("${table.name}")
@Schema(title = "${table.comment!}")
public class ${domainName} extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.tableFieldDTOList as field>
    <#if field.propertyName == "createTime" || field.propertyName == "updateTime" || field.propertyName == "updateUserId" || field.propertyName == "updateUserName" >
        <#continue>
    </#if>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>
    <#if field.keyFlag>
        <#-- 主键 -->
    @Schema(hidden = true)
        <#if field.keyFlag>
    @TableId(value = "${field.propertyName}", type = IdType.AUTO)
        <#elseif idType??>
    @TableId(value = "${field.propertyName}", type = IdType.${idType})
        <#elseif field.convert>
    @TableId("${field.propertyName}")
        </#if>
    <#else>
        <#-- 普通字段 -->
        <#if field.propertyType.type == "String" && field.characterMaximumLength??>
    @Size(max = ${field.characterMaximumLength})
        </#if>
        <#if field.isNullable == "NO">
            <#if field.propertyType.type == "String">
    @NotBlank
            <#else>
    @NotNull
            </#if>
        </#if>
        <#if field.comment!?length gt 0>
    @Schema(title = "${field.comment}")
        </#if>
        <#-- -----   存在字段填充设置   ----->
    </#if>
    private ${field.propertyType.type} ${field.propertyName};

</#list>
<#------------  END 字段循环遍历  ---------->
}
