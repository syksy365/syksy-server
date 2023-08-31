package site.syksy.rose.upms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import site.syksy.rose.upms.constant.RouteLinkType;
import site.syksy.rose.upms.domain.RoleDO;
import site.syksy.rose.upms.mapper.RoleMapper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@Service
@CacheConfig(cacheNames = "s_upms_role", keyGenerator = "cacheKeyGenerator")
public class RoleService extends ServiceImpl<RoleMapper, RoleDO> {

    @Autowired
    UserRoleService userRoleService;

    @Lazy
    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    RouteLinkService routeLinkService;

    @CacheEvict(allEntries = true)
    @Override
    public boolean save(RoleDO entity) {
        if (StringUtils.isEmpty(getByName(entity.getName()))) {
            LocalDateTime now = LocalDateTime.now();
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            return super.save(entity);
        } else {
            throw new RuntimeException(entity.getName() + "角色名已存在");
        }
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean updateById(RoleDO entity) {
        entity.setUpdateTime(LocalDateTime.now());
        RoleDO oldRoleDO = getByName(entity.getName());
        if (!StringUtils.isEmpty(oldRoleDO) && !oldRoleDO.getId().equals(entity.getId())) {
            throw new RuntimeException(entity.getName() + "角色名已存在");
        }
        return super.updateById(entity);
    }

    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        userRoleService.removeByRoleId(id);
        roleMenuService.removeByRoleId(id);
        routeLinkService.removeByTargetIdAndTargetType(id, RouteLinkType.ROLE);
        return super.removeById(id);
    }

    /**
     * 根据角色名查询角色信息
     *
     * @param name
     * @return
     */
    @Cacheable
    public RoleDO getByName(String name) {
        QueryWrapper<RoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleDO::getName, name);
        return baseMapper.selectOne(queryWrapper);
    }

    @Cacheable
    @Override
    public RoleDO getById(Serializable id) {
        return super.getById(id);
    }

    @Cacheable
    @Override
    public List<RoleDO> list() {
        return super.list();
    }
}
