package site.syksy.qingzhou.upms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.syksy.qingzhou.upms.domain.UserRoleDO;
import site.syksy.qingzhou.upms.mapper.UserRoleMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@Service
@CacheConfig(cacheNames = "s_upms_user_role", keyGenerator = "cacheKeyGenerator")
public class UserRoleService extends ServiceImpl<UserRoleMapper, UserRoleDO> {

    /**
     * 根据用户ID删除用户角色关联
     *
     * @param userId
     * @return
     */
    @CacheEvict(cacheNames = {"s_upms_user_role", "s_upms_route"}, allEntries = true)
    public boolean removeByUserId(Serializable userId) {
        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleDO::getUserId, userId);
        return remove(queryWrapper);
    }

    /**
     * 根据角色ID删除用户角色关联
     *
     * @param roleId
     * @return
     */
    @CacheEvict(cacheNames = {"s_upms_user_role", "s_upms_route"}, allEntries = true)
    public boolean removeByRoleId(Serializable roleId) {
        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleDO::getRoleId, roleId);
        return remove(queryWrapper);
    }

    /**
     * 根据角色ID和用户ID删除用户角色关联
     *
     * @param roleId
     * @return
     */
    @CacheEvict(cacheNames = {"s_upms_user_role", "s_upms_user", "s_upms_route"}, allEntries = true)
    public boolean removeByRoleIdAndUserId(Serializable roleId, String userId) {
        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserRoleDO::getRoleId, roleId)
                .eq(UserRoleDO::getUserId, userId);
        return remove(queryWrapper);
    }

    /**
     * 保存用户角色关联信息
     * 加减用户角色关联关系
     *
     * @param userId
     * @param roleIds
     */
    @CacheEvict(cacheNames = {"s_upms_user_role", "s_upms_route"}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRole(String userId, List<String> roleIds) {
        roleIds = roleIds.stream().distinct().collect(Collectors.toList());
        List<String> roleIdList = selectRoleIdsByUserId(userId);
        List<String> a = new ArrayList<>(roleIdList);
        List<String> b = new ArrayList<>(roleIds);
        a.retainAll(b);
        roleIds.removeAll(a);
        roleIdList.removeAll(a);

        List<UserRoleDO> userRoleDOList = new ArrayList<>();
        for (String roleId : roleIds) {
            UserRoleDO userRoleDO = new UserRoleDO();
            userRoleDO.setUserId(userId);
            userRoleDO.setRoleId(roleId);
            userRoleDOList.add(userRoleDO);
        }
        if (userRoleDOList.size() > 0) {
            saveBatch(userRoleDOList);
        }

        for (String roleId : roleIdList) {
            removeByRoleIdAndUserId(roleId, userId);
        }
    }

    /**
     * 增加用户角色关联信息
     *
     * @param userIds
     * @param roleId
     */
    @CacheEvict(cacheNames = {"s_upms_user_role", "s_upms_user", "s_upms_route"}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRole(List<String> userIds, String roleId) {
        UserRoleDO userRoleDO = new UserRoleDO();
        userRoleDO.setRoleId(roleId);
        for (String userId : userIds) {
            userRoleDO.setId(null);
            userRoleDO.setUserId(userId);
            save(userRoleDO);
        }
    }

    /**
     * 根据用户ID,查询关联角色ID
     *
     * @param userId
     * @return
     */
    @Cacheable
    public List<String> selectRoleIdsByUserId(String userId) {
        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleDO::getUserId, userId);
        List<UserRoleDO> userRoleDOList = baseMapper.selectList(queryWrapper);
        List<String> roleIds = new ArrayList<>();
        for (UserRoleDO userRoleDO : userRoleDOList) {
            roleIds.add(userRoleDO.getRoleId());
        }
        return roleIds;
    }

    /**
     * 根据用户ID,查询关联角色信息
     *
     * @param userId
     * @return
     */
    public List<UserRoleDO> selectRoleByUserId(String userId) {
        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleDO::getUserId, userId);
        return list(queryWrapper);
    }
}
