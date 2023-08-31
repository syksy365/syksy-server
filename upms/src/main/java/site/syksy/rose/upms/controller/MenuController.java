package site.syksy.rose.upms.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.syksy.rose.common.utils.TreeUtil;
import site.syksy.rose.upms.constant.MenuType;
import site.syksy.rose.upms.constant.MoveDirection;
import site.syksy.rose.upms.domain.MenuDO;
import site.syksy.rose.upms.domain.vo.MenuAddVO;
import site.syksy.rose.upms.domain.dto.MenuDTO;
import site.syksy.rose.upms.domain.vo.MenuVO;
import site.syksy.rose.upms.service.MenuService;
import site.syksy.rose.upms.service.RoleMenuService;
import site.syksy.rose.upms.service.UserRoleService;
import site.syksy.rose.web.exception.ForbiddenException;
import site.syksy.rose.web.exception.NotFoundException;
import site.syksy.rose.web.resolver.UserId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@RestController
@Tag(name = "菜单")
@RequestMapping("upms/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleMenuService roleMenuService;

    @PostMapping
    @Operation(summary = "新建")
    @Transactional(rollbackFor = Exception.class)
    public void post(@Validated @RequestBody MenuAddVO menuAddVO) {
        String parentId = null;
        List<String> levelIds = menuAddVO.getLevelIds();
        if (levelIds != null && levelIds.size() > 0) {
            parentId = levelIds.get(levelIds.size() - 1);
            MenuDO parentMenuDO = menuService.getById(parentId);
            if (parentMenuDO.getGenre().equals(MenuType.ACTION)) {
                throw new ForbiddenException("父节点是按钮，不能添加子节点");
            }
        }
        MenuDO menuDO = new MenuDO();
        BeanUtils.copyProperties(menuAddVO, menuDO);
        menuDO.setParentId(parentId);
        menuService.save(menuDO);
        roleMenuService.saveRoleMenu(menuAddVO.getRoleIds(), menuDO.getId());
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除")
    public void delete(@PathVariable String id) {
        MenuDO MenuDO = menuService.getById(id);
        menuService.remove(MenuDO);
    }

    @PutMapping("{id}")
    @Operation(summary = "修改")
    @Transactional(rollbackFor = Exception.class)
    public void put(@PathVariable String id,@Validated @RequestBody MenuAddVO menuAddVO) {
        MenuDO menuDO = new MenuDO();
        BeanUtils.copyProperties(menuAddVO, menuDO);
        menuDO.setId(id);
        menuDO.setUpdateTime(LocalDateTime.now());

        //类型不允许修改
        menuDO.setGenre(null);

        List<String> levelIds = menuAddVO.getLevelIds();
        if (levelIds != null && levelIds.size() > 0) {
            String parentId = levelIds.get(levelIds.size() - 1);
            if (id.equals(parentId)) {
                throw new ForbiddenException("父节点不允许是自己");
            }
            menuDO.setParentId(parentId);
        }

        if (menuService.updateById(menuDO)) {
            roleMenuService.saveRoleMenu(menuAddVO.getRoleIds(), id);
        } else {
            throw new NotFoundException();
        }
    }

    @GetMapping("{id}")
    @Operation(summary = "获取菜单详情")
    public MenuVO get(@PathVariable String id) {
        MenuDO menuDO = menuService.getById(id);
        MenuVO menuVO = new MenuVO();
        BeanUtils.copyProperties(menuDO, menuVO);

        if (menuDO.getParentId() != null) {
            List<MenuDTO> menuDTOTree = menuService.allTreeMenu();
            List<String> levelIds = TreeUtil.findLinkIds(menuDTOTree, MenuDTO::getChildren, MenuDTO::getId, MenuDTO::getParentId, menuDO.getParentId());
            menuVO.setLevelIds(levelIds);
        }

        List<String> roleIds = new ArrayList<>();
        Map<String, String> roles = new HashMap<>(3);
        roleMenuService.getRoleByMenuId(id, roleIds, roles);
        menuVO.setRoleIds(roleIds);
        menuVO.setRoles(roles);
        return menuVO;
    }

    @PutMapping("position/{id}")
    @Operation(summary = "修改排序位置")
    public void move(@PathVariable String id, @RequestParam MoveDirection direction) {
        MenuDO menuDO = menuService.getById(id);
        menuService.move(menuDO, direction);
    }

    @GetMapping("current")
    @Operation(summary = "获取当前账户菜单")
    public List<MenuDTO> getCurrentMenu(@Parameter(hidden = true) @UserId String userId) {
        List<String> roleIds = userRoleService.selectRoleIdsByUserId(userId);
        List<String> menuIds = roleMenuService.selectMenuIdsByRoleIds(roleIds);
        return menuService.getTreeMenu("轻舟", menuIds);
    }

    @GetMapping
    @Operation(summary = "全部菜单(目录和操作)列表")
    public List<MenuDTO> list() {
        return menuService.allTreeMenu();
    }

    @GetMapping("directory")
    @Operation(summary = "全部目录列表")
    public List<MenuDTO> listDirectory(@RequestParam(required = false) String disabledId) {
        if (StringUtils.isNotBlank(disabledId)) {
            return menuService.allDirectoryToTree(disabledId);
        } else {
            return menuService.allDirectoryToTree();
        }
    }

    @PutMapping("expand/{id}")
    @Operation(summary = "菜单展开和关闭")
    public void expand(@PathVariable String id, @RequestParam Boolean expand) {
        UpdateWrapper<MenuDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(MenuDO::getExpand, expand).eq(MenuDO::getId, id);
        menuService.update(updateWrapper);
    }

    @GetMapping("level/{id}")
    @Operation(summary = "获取层级ID")
    public List<String> getLevel(@PathVariable String id) {
        List<MenuDTO> menuDTOTree = menuService.allTreeMenu();
        return TreeUtil.findLinkIds(menuDTOTree, MenuDTO::getChildren, MenuDTO::getId, MenuDTO::getParentId, id);
    }


}