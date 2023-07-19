package site.syksy.qingzhou.setting.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.syksy.qingzhou.setting.domain.ConfigDO;
import site.syksy.qingzhou.setting.service.ConfigService;
import site.syksy.qingzhou.web.exception.NotFoundException;

import java.util.List;

/**
 * @author Raspberry
 * @since 2020-08-22
 */
@RestController
@RequestMapping("setting/config")
public class ConfigController {

    @Autowired
    ConfigService configService;

    @PostMapping
    @Operation(summary = "新增")
    public void post(@Validated @RequestBody ConfigDO configDO) {
        configService.save(configDO);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除")
    public void delete(@PathVariable String id) {
        ConfigDO configDO = configService.getById(id);
        if (configDO == null) {
            throw new NotFoundException("数据不存在");
        }
        configService.removeById(id);
    }

    @PutMapping("{id}")
    @Operation(summary = "修改")
    public void put(@PathVariable String id,@Validated @RequestBody ConfigDO configDO) {
        configDO.setId(id);
        configService.updateById(configDO);
    }

    @GetMapping("{id}")
    @Operation(summary = "获取")
    public ConfigDO get(@PathVariable String id) {
        return configService.getById(id);
    }

    @GetMapping("list")
    @Operation(summary = "查询全部")
    public List<ConfigDO> list() {
        return configService.list();
    }

}