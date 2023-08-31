package site.syksy.rose.upms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.syksy.rose.upms.constant.RouteLinkType;
import site.syksy.rose.upms.domain.RoleDO;
import site.syksy.rose.upms.domain.vo.RoleEdit;
import site.syksy.rose.upms.service.RoleService;
import site.syksy.rose.upms.service.RouteLinkService;
import site.syksy.rose.upms.service.UserRoleService;
import site.syksy.rose.web.exception.NotFoundException;

import java.util.List;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@RestController
@Tag(name = "角色")
@RequestMapping("upms/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    RouteLinkService routeLinkService;

    @Autowired
    UserRoleService userRoleService;

    @PostMapping
    @Operation(summary = "新增")
    @Transactional(rollbackFor = Exception.class)
    public void post(@Validated @RequestBody RoleEdit roleEdit) {
        RoleDO roleDO = new RoleDO();
        BeanUtils.copyProperties(roleEdit, roleDO);
        roleService.save(roleDO);
        if (roleEdit.getRouteIds() != null) {
            routeLinkService.saveRouteLink(roleDO.getId(), RouteLinkType.ROLE, roleEdit.getRouteIds());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除")
    public void delete(@PathVariable String id) {
        if (!roleService.removeById(id)) {
            throw new NotFoundException();
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "修改")
    @Transactional(rollbackFor = Exception.class)
    public void put(@PathVariable String id,@Validated @RequestBody RoleEdit roleEdit) {
        RoleDO roleDO = new RoleDO();
        BeanUtils.copyProperties(roleEdit, roleDO);
        roleDO.setId(id);
        roleService.updateById(roleDO);
        routeLinkService.saveRouteLink(id, RouteLinkType.ROLE, roleEdit.getRouteIds());
    }

    @GetMapping
    @Operation(summary = "获取列表")
    public List<RoleDO> getList() {
        return roleService.list();
    }

    @GetMapping("{id}")
    @Operation(summary = "获取详细信息")
    public RoleDO get(@PathVariable String id) {
        return roleService.getById(id);
    }

    @DeleteMapping("{id}/{userId}")
    @Operation(summary = "移除用户")
    public void deleteUser(@PathVariable String id,@PathVariable String userId) {
        userRoleService.removeByRoleIdAndUserId(id,userId);
    }

    @PostMapping("{id}")
    @Operation(summary = "增加用户")
    public void addUser(@PathVariable String id,@RequestBody List<String> userIds) {
        userRoleService.saveUserRole(userIds,id);
    }


}