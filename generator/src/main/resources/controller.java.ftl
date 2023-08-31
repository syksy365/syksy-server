package ${packagePath}.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import exception.site.syksy.rose.web.NotFoundException;
import page.site.syksy.rose.web.MyPage;
import page.site.syksy.rose.web.Pageable;
import page.site.syksy.rose.web.PageableAsQueryParam;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ${packagePath}.service.${serviceName};
import ${packagePath}.domain.${domainName};

import javax.annotation.Resource;

/**
* @author ${author}
*/
@RestController
@Tag(name = "")
@Validated
@RequestMapping
public class ${controllerName} {

    @Resource
    ${serviceName} ${serviceName?uncap_first};

    @PostMapping
    @Operation(summary = "新建")
    public void post(@Validated @RequestBody ${domainName} ${domainName?uncap_first}) {
        ${domainName?uncap_first}.setId(null);
        ${serviceName?uncap_first}.save(${domainName?uncap_first});
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除")
    public void delete(@PathVariable Long id) {
        ${domainName} ${domainName?uncap_first} = ${serviceName?uncap_first}.getById(id);
        if(${domainName?uncap_first} == null){
            throw new NotFoundException("数据不存在");
        }
        ${serviceName?uncap_first}.removeById(id);
    }

    @PutMapping("{id}")
    @Operation(summary = "修改")
    public void put(@PathVariable Long id,@Validated @RequestBody ${domainName} ${domainName?uncap_first}) {
        ${domainName?uncap_first}.setId(id);
        ${serviceName?uncap_first}.updateById(${domainName?uncap_first});
    }

    @GetMapping("{id}")
    @Operation(summary = "获取")
    public ${domainName} get(@PathVariable Long id) {
        ${domainName} ${domainName?uncap_first} = ${serviceName?uncap_first}.getById(id);
        return ${domainName?uncap_first};
    }

    @GetMapping("page")
    @Operation(summary = "分页")
    @PageableAsQueryParam
    public MyPage<${domainName}> page(@Parameter(hidden = true) Pageable pageable) {
        MyPage myPage = new MyPage<>(pageable);
        return ${serviceName?uncap_first}.page(myPage);
    }

}