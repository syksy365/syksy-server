package site.syksy.qingzhou.upms.service;

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
import site.syksy.qingzhou.upms.domain.RouteLinkDO;
import site.syksy.qingzhou.upms.mapper.RouteLinkMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@Service
@CacheConfig(cacheNames = "s_upms_route_link", keyGenerator = "cacheKeyGenerator")
public class RouteLinkService extends ServiceImpl<RouteLinkMapper, RouteLinkDO> {

    @Autowired
    @Lazy
    RouteService routeService;

    /**
     * 存储路由关联信息
     *
     * @param targetId
     * @param targetType
     * @param routeIds
     */
    @CacheEvict(cacheNames={"s_upms_route_link","s_upms_route"},allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void saveRouteLink(String targetId, String targetType, List<String> routeIds) {
        if (routeIds != null && routeIds.size() > 0) {
            routeIds = routeIds.stream().distinct().collect(Collectors.toList());
            List<String> routeIdList = selectRouteIdByTarget(targetId, targetType);
            List<String> a = new ArrayList<>(routeIdList);
            List<String> b = new ArrayList<>(routeIds);
            a.retainAll(b);
            routeIdList.removeAll(a);
            routeIds.removeAll(a);
            RouteLinkDO routeLinkDO = new RouteLinkDO();
            routeLinkDO.setTargetId(targetId);
            routeLinkDO.setTargetType(targetType);
            for (String routeId : routeIds) {
                if (StringUtils.isEmpty(routeService.getById(routeId))) {
                    throw new RuntimeException("所选路由不存在");
                }
                routeLinkDO.setId(null);
                routeLinkDO.setRouteId(routeId);
                baseMapper.insert(routeLinkDO);
            }
            for (String routeId : routeIdList) {
                removeByTargetIdAndTargetTypeAndRouteId(targetId, targetType, routeId);
            }
        }
    }

    /**
     * 根据路由ID、目标信息查询关联信息
     *
     * @param routeId
     * @param targetId
     * @param targetType
     * @return
     */
    @Cacheable
    public RouteLinkDO getByRouteIdAndTargetIdAndTargetType(String routeId, String targetId, String targetType) {
        QueryWrapper<RouteLinkDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RouteLinkDO::getRouteId, routeId)
                .eq(RouteLinkDO::getTargetId, targetId)
                .eq(RouteLinkDO::getTargetType, targetType);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据目标信息查询路由关联信息
     *
     * @param targetId
     * @param targetType
     * @return
     */
    @Cacheable
    public List<String> selectRouteIdByTarget(String targetId, String targetType) {
        QueryWrapper<RouteLinkDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RouteLinkDO::getTargetId, targetId).eq(RouteLinkDO::getTargetType, targetType);
        List<RouteLinkDO> routeLinkDOList = baseMapper.selectList(queryWrapper);
        List<String> routeIds = new ArrayList<>();
        for (RouteLinkDO routeLinkDO : routeLinkDOList) {
            routeIds.add(routeLinkDO.getRouteId());
        }
        return routeIds;
    }

    /**
     * 根据目标信息查询路由关联信息
     *
     * @param targetIds
     * @param targetType
     * @return
     */
    @Cacheable
    public List<String> selectRouteIdByTarget(List<String> targetIds, String targetType) {
        QueryWrapper<RouteLinkDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(RouteLinkDO::getTargetId, targetIds).eq(RouteLinkDO::getTargetType, targetType);
        List<RouteLinkDO> routeLinkDOList = baseMapper.selectList(queryWrapper);
        List<String> routeIds = new ArrayList<>();
        for (RouteLinkDO routeLinkDO : routeLinkDOList) {
            routeIds.add(routeLinkDO.getRouteId());
        }
        return routeIds;
    }


    /**
     * 根据关联类型和关联ID删除连接
     *
     * @return
     */
    @CacheEvict(cacheNames={"s_upms_route_link","s_upms_route"},allEntries = true)
    public boolean removeByTargetIdAndTargetTypeAndRouteId(String targetId, String targetType, String routeId) {
        QueryWrapper<RouteLinkDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RouteLinkDO::getTargetId, targetId)
                .eq(RouteLinkDO::getTargetType, targetType)
                .eq(RouteLinkDO::getRouteId, routeId);
        return remove(queryWrapper);
    }

    /**
     * 根据关联类型和关联ID删除连接
     *
     * @return
     */
    @CacheEvict(cacheNames={"s_upms_route_link","s_upms_route"},allEntries = true)
    public boolean removeByTargetIdAndTargetType(Serializable targetId, String targetType) {
        QueryWrapper<RouteLinkDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RouteLinkDO::getTargetId, targetId).eq(RouteLinkDO::getTargetType, targetType);
        return remove(queryWrapper);
    }

    /**
     * 根据路由ID删除连接
     *
     * @return
     */
    @CacheEvict(cacheNames={"s_upms_route_link","s_upms_route"},allEntries = true)
    public boolean removeByRouteId(Serializable routeId) {
        QueryWrapper<RouteLinkDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RouteLinkDO::getRouteId, routeId);
        return remove(queryWrapper);
    }

    @CacheEvict(cacheNames={"s_upms_route_link","s_upms_route"},allEntries = true)
    @Override
    public boolean save(RouteLinkDO entity) {
        return super.save(entity);
    }

    @CacheEvict(cacheNames={"s_upms_route_link","s_upms_route"},allEntries = true)
    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }
}
