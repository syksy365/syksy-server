package site.syksy.qingzhou.upms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.syksy.qingzhou.upms.domain.RoleDO;
import site.syksy.qingzhou.upms.domain.RoleMenuDO;
import site.syksy.qingzhou.upms.mapper.RoleMenuMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@Service
@CacheConfig(cacheNames = "s_upms_role_menu", keyGenerator = "cacheKeyGenerator")
public class RoleMenuService extends ServiceImpl<RoleMenuMapper, RoleMenuDO> {

    @Lazy
    @Autowired
    RoleService roleService;

    /**
     * 新建菜单时关联角色保存
     *
     * @param roleIds
     * @param menuId
     */
    @CacheEvict(cacheNames = {"s_upms_role_menu", "s_upms_route"}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleMenu(List<String> roleIds, String menuId) {
        if (roleIds == null) {
            roleIds = new ArrayList<>();
        }
        roleIds = roleIds.stream().distinct().collect(Collectors.toList());
        List<String> roleIdList = getByMenuId(menuId);
        List<String> a = new ArrayList<>(roleIdList);
        List<String> b = new ArrayList<>(roleIds);
        a.retainAll(b);
        roleIds.removeAll(a);
        roleIdList.removeAll(a);

        RoleMenuDO roleMenuDO = new RoleMenuDO();
        roleMenuDO.setMenuId(menuId);
        for (String roleId : roleIds) {
            if (roleService.getById(roleId) == null) {
                throw new RuntimeException("所选角色不存在");
            }
            roleMenuDO.setId(null);
            roleMenuDO.setRoleId(roleId);
            save(roleMenuDO);
        }
        for (String roleId : roleIdList) {
            removeByMenuIdAndRoleId(menuId, roleId);
        }
    }

    @Cacheable
    public List<String> getByMenuId(String menuId) {
        QueryWrapper<RoleMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleMenuDO::getMenuId, menuId);
        List<RoleMenuDO> roleMenuDOList = list(queryWrapper);
        List<String> roleIds = new ArrayList<>();
        for (RoleMenuDO roleMenuDO : roleMenuDOList) {
            roleIds.add(roleMenuDO.getRoleId());
        }
        return roleIds;
    }

    /**
     * 根据菜单ID，查询关联到角色信息
     *
     * @param menuId
     * @param roleIds
     * @param roles
     */
    public void getRoleByMenuId(String menuId, List<String> roleIds, Map<String, String> roles) {
        QueryWrapper<RoleMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleMenuDO::getMenuId, menuId);
        List<RoleMenuDO> roleMenuDOList = list(queryWrapper);
        for (RoleMenuDO roleMenuDO : roleMenuDOList) {
            RoleDO roleDO = roleService.getById(roleMenuDO.getRoleId());
            roleIds.add(roleDO.getId());
            roles.put(roleDO.getId(), roleDO.getName());
        }
    }


    @Cacheable
    public RoleMenuDO getByRoleIdAndMenuId(String roleId, String menuId) {
        QueryWrapper<RoleMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RoleMenuDO::getRoleId, roleId).eq(RoleMenuDO::getMenuId, menuId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据角色ID查找关联的菜单ID
     *
     * @param roleIds
     * @return
     */
    @Cacheable
    public List<String> selectMenuIdsByRoleIds(List<String> roleIds) {
        if (roleIds.size() == 0) {
            return null;
        }
        QueryWrapper<RoleMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(RoleMenuDO::getRoleId, roleIds);
        List<RoleMenuDO> roleMenuDOList = baseMapper.selectList(queryWrapper);
        List<String> menuIds = new ArrayList<>();
        for (RoleMenuDO roleMenuDO : roleMenuDOList) {
            if (!menuIds.contains(roleMenuDO.getMenuId())) {
                menuIds.add(roleMenuDO.getMenuId());
            }
        }
        return menuIds;
    }

    /**
     * 根据角色ID删除角色菜单关联
     *
     * @param roleId
     * @return
     */
    @CacheEvict(cacheNames = {"s_upms_role_menu", "s_upms_route"}, allEntries = true)
    public boolean removeByRoleId(Serializable roleId) {
        QueryWrapper<RoleMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleMenuDO::getRoleId, roleId);
        return remove(queryWrapper);
    }

    /**
     * 根据菜单ID删除角色菜单关联
     *
     * @param menuId
     * @return
     */
    @CacheEvict(cacheNames = {"s_upms_role_menu", "s_upms_route"}, allEntries = true)
    public boolean removeByMenuId(Serializable menuId) {
        QueryWrapper<RoleMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleMenuDO::getMenuId, menuId);
        return remove(queryWrapper);
    }

    /**
     * 根据菜单ID和角色ID删除角色菜单关联
     *
     * @param menuId
     * @return
     */
    @CacheEvict(cacheNames = {"s_upms_role_menu", "s_upms_route"}, allEntries = true)
    public boolean removeByMenuIdAndRoleId(Serializable menuId, String roleId) {
        QueryWrapper<RoleMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RoleMenuDO::getMenuId, menuId)
                .eq(RoleMenuDO::getRoleId, roleId);
        return remove(queryWrapper);
    }

    @CacheEvict(cacheNames = {"s_upms_role_menu", "s_upms_route"}, allEntries = true)
    @Override
    public boolean save(RoleMenuDO entity) {
        return super.save(entity);
    }

    @CacheEvict(cacheNames = {"s_upms_role_menu", "s_upms_route"}, allEntries = true)
    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Cacheable
    @Override
    public List<RoleMenuDO> list(Wrapper<RoleMenuDO> queryWrapper) {
        return super.list(queryWrapper);
    }
}
