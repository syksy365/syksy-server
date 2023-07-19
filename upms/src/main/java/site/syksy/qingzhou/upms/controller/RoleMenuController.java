package site.syksy.qingzhou.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.syksy.qingzhou.upms.domain.RoleMenuDO;
import site.syksy.qingzhou.upms.service.RoleMenuService;
import site.syksy.qingzhou.web.exception.ForbiddenException;
import site.syksy.qingzhou.web.exception.NotFoundException;

import java.util.List;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@RestController
@Tag(name = "角色菜单关联")
@RequestMapping("upms/role/menu")
public class RoleMenuController {

    @Autowired
    RoleMenuService roleMenuService;

    @PostMapping
    @Operation(summary = "新增")
    public void post(@RequestParam String roleId, @RequestParam String menuId) {
        if (roleMenuService.getByRoleIdAndMenuId(roleId, menuId) == null) {
            RoleMenuDO roleMenuDO = new RoleMenuDO();
            roleMenuDO.setRoleId(roleId);
            roleMenuDO.setMenuId(menuId);
            roleMenuService.save(roleMenuDO);
        } else {
            throw new ForbiddenException("角色已关联菜单，请重新加载数据！");
        }
    }

    @DeleteMapping
    @Operation(summary = "删除")
    public void delete(@RequestParam String roleId, @RequestParam String menuId) {
        RoleMenuDO roleMenuDO = roleMenuService.getByRoleIdAndMenuId(roleId, menuId);
        if (roleMenuDO == null) {
            throw new NotFoundException();
        } else {
            roleMenuService.removeById(roleMenuDO.getId());
        }
    }

    @GetMapping("{roleId}")
    @Operation(summary = "查询")
    public List<RoleMenuDO> get(@PathVariable String roleId) {
        QueryWrapper<RoleMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RoleMenuDO::getRoleId, roleId);
        return roleMenuService.list(queryWrapper);
    }

}