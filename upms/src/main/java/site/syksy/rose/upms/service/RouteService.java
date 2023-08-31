package site.syksy.rose.upms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import site.syksy.rose.upms.constant.RouteCheckType;
import site.syksy.rose.upms.domain.RouteDO;
import site.syksy.rose.upms.mapper.RouteMapper;

import javax.annotation.Resource;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Raspberry
 * @since 2020-08-26
 */
@Service
@CacheConfig(cacheNames = "s_upms_route", keyGenerator = "cacheKeyGenerator")
public class RouteService extends ServiceImpl<RouteMapper, RouteDO> {

    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    @Lazy
    private RequestMappingHandlerMapping handlerMapping;

    @Resource
    @Lazy
    private RouteLinkService routeLinkService;

    @Resource
    ApiService apiService;

    /**
     * 查询出免登录的接口
     *
     * @return
     */
    @Cacheable
    public Map<String, RouteDO> selectByNoLogin() {
        QueryWrapper<RouteDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RouteDO::getOnOff, true)
                .and(startDate -> startDate.le(RouteDO::getStartDate, LocalDateTime.now()).or().isNull(RouteDO::getStartDate))
                .and(endDate -> endDate.ge(RouteDO::getEndDate, LocalDateTime.now()).or().isNull(RouteDO::getEndDate))
                .eq(RouteDO::getCheckType, RouteCheckType.NO_LOGIN);
        List<RouteDO> routeDOList = list(queryWrapper);
        Map<String, RouteDO> map = new HashMap<>();
        for (RouteDO routeDO : routeDOList) {
            map.put(routeDO.getHandlerMethodHash(), routeDO);
        }
        return map;
    }

    /**
     * 查询出需登录的接口
     *
     * @return
     */
    @Cacheable
    public Map<String, RouteDO> selectByLogin() {
        QueryWrapper<RouteDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RouteDO::getOnOff, true)
                .and(startDate -> startDate.le(RouteDO::getStartDate, LocalDateTime.now()).or().isNull(RouteDO::getStartDate))
                .and(endDate -> endDate.ge(RouteDO::getEndDate, LocalDateTime.now()).or().isNull(RouteDO::getEndDate))
                .eq(RouteDO::getCheckType, RouteCheckType.LOGIN);
        List<RouteDO> routeDOList = list(queryWrapper);
        Map<String, RouteDO> map = new HashMap<>();
        for (RouteDO routeDO : routeDOList) {
            map.put(routeDO.getHandlerMethodHash(), routeDO);
        }
        return map;
    }

    /**
     * 查询用户拥有的接口
     */
    @Cacheable
    public Map<String, RouteDO> selectRouteByUserId(String userId) {
        List<RouteDO> routeDOList = baseMapper.selectRouteByUserId(userId);
        Map<String, RouteDO> map = new HashMap<>();
        for (RouteDO routeDO : routeDOList) {
            map.put(routeDO.getHandlerMethodHash(), routeDO);
        }
        return map;
    }

    /**
     * 根据ID集合和请求方法查询出需授权的接口
     *
     * @param ids
     * @return
     */
    @Cacheable
    public List<RouteDO> selectByIdsAndRequestMethod(List<String> ids) {
        QueryWrapper<RouteDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(RouteDO::getId, ids)
                .eq(RouteDO::getOnOff, true)
                .and(startDate -> startDate.le(RouteDO::getStartDate, LocalDateTime.now()).or().isNull(RouteDO::getStartDate))
                .and(endDate -> endDate.ge(RouteDO::getEndDate, LocalDateTime.now()).or().isNull(RouteDO::getEndDate))
                .eq(RouteDO::getCheckType, RouteCheckType.AUTH);
        return list(queryWrapper);
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean removeById(Serializable id) {
        RouteDO routeDO = getById(id);
        if (routeDO == null) {
            throw new RuntimeException("路由不存在");
        } else if (routeDO.getOnline()) {
            throw new RuntimeException("在线路由不能删除");
        }
        routeLinkService.removeByRouteId(id);
        return super.removeById(id);
    }

    /**
     * 扫描全部http接口，注册路由
     */
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void registeredRoute() {
        Map<RequestMappingInfo, HandlerMethod> map = handlerMapping.getHandlerMethods();
        List<RouteDO> routeDOList = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            Set<RequestMethod> requestMethodSet = requestMappingInfo.getMethodsCondition().getMethods();
            Set<String> patterns = entry.getKey().getPathPatternsCondition().getPatternValues();
            if (requestMethodSet.size() > 0) {
                for (RequestMethod requestMethod : requestMethodSet) {
                    for (String pattern : patterns) {
                        RouteDO routeDO = new RouteDO();
                        routeDO.setHandlerMethod(handlerMethod.toString());
                        routeDO.setHandlerMethodHash(DigestUtils.md5DigestAsHex(handlerMethod.toString().getBytes(StandardCharsets.UTF_8)));
                        routeDO.setRequestMethod(requestMethod.name());
                        routeDO.setPattern(pattern);
                        routeDOList.add(routeDO);
                    }
                }
            } else {
                for (String pattern : patterns) {
                    RouteDO routeDO = new RouteDO();
                    routeDO.setRequestMethod("*");
                    routeDO.setHandlerMethod(handlerMethod.toString());
                    routeDO.setHandlerMethodHash(DigestUtils.md5DigestAsHex(handlerMethod.toString().getBytes(StandardCharsets.UTF_8)));
                    routeDO.setPattern(pattern);
                    routeDOList.add(routeDO);
                }
            }
        }
        updateOrSaveRouteDO(routeDOList);
    }

    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    private void updateOrSaveRouteDO(List<RouteDO> routeDOList) {
        List<RouteDO> routeDOdbList = baseMapper.selectList(null);
        List<RouteDO> newRouteDOList = removeAll(routeDOList, routeDOdbList);
        List<RouteDO> offlineRouteDOList = removeAll(routeDOdbList, routeDOList);
        newRouteDOList.forEach(newRouteDO -> newRouteDO.setOnline(true));
        newRouteDOList.forEach(baseMapper::insert);
        offlineRouteDOList.forEach(offlineRouteDO -> offlineRouteDO.setOnline(false));
        offlineRouteDOList.forEach(baseMapper::updateById);
        List<RouteDO> onlineRouteDOList = retainAll(routeDOdbList, routeDOList);
        onlineRouteDOList.forEach(onlineRouteDO -> {
            if (!onlineRouteDO.getOnline()) {
                onlineRouteDO.setOnline(true);
                baseMapper.updateById(onlineRouteDO);
            }
        });

        AntPathMatcher matcher = new AntPathMatcher();
        List<RouteDO> routeDOS = baseMapper.selectList(null);
        for (RouteDO routeDO : routeDOS) {
            if (apiService.webIgnoringAntMatchers(matcher, routeDO.getPattern())) {
                UpdateWrapper<RouteDO> updateWrapper = new UpdateWrapper<>();
                updateWrapper.lambda().set(RouteDO::getCheckType, -1);
                updateWrapper.lambda().eq(RouteDO::getId, routeDO.getId());
                update(updateWrapper);
            }
        }
    }

    /**
     * 差集
     *
     * @param list1
     * @param list2
     * @return
     */
    private List removeAll(List list1, List list2) {
        List list3 = new ArrayList<>(list1);
        List list4 = new ArrayList<>(list2);
        list3.removeAll(list4);
        return list3;
    }

    /**
     * 交集
     *
     * @param list1
     * @param list2
     * @return
     */
    private List retainAll(List list1, List list2) {
        List list3 = new ArrayList<>(list1);
        List list4 = new ArrayList<>(list2);
        list3.retainAll(list4);
        return list3;
    }

    @Cacheable
    @Override
    public List<RouteDO> listByIds(Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean updateById(RouteDO entity) {
        return super.updateById(entity);
    }

    @Cacheable
    @Override
    public RouteDO getById(Serializable id) {
        return super.getById(id);
    }

    @Cacheable
    @Override
    public <E extends IPage<RouteDO>> E page(E page, Wrapper<RouteDO> queryWrapper) {
        return super.page(page, queryWrapper);
    }

    /**
     * 获取全部在线的接口路由
     *
     * @return
     */
    public Map<String, List<ConfigAttribute>> selectAll() {
        QueryWrapper<RouteDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RouteDO::getOnline, true).eq(RouteDO::getCheckType, 1);
        List<RouteDO> routeDOList = list(queryWrapper);
        Map<String, List<ConfigAttribute>> methodMap = new HashMap<>();
        for (RouteDO routeDO : routeDOList) {
            String handlerMethod = routeDO.getHandlerMethod();
            handlerMethod = handlerMethod.replace("#", ".");
            int leftParenthesisIndex = handlerMethod.indexOf("(");
            handlerMethod = handlerMethod.substring(0, leftParenthesisIndex);
            methodMap.put(handlerMethod, SecurityConfig.createList("MY_ROLE_DUMMY"));
        }
        return methodMap;
    }

}
