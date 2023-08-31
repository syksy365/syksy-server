package site.syksy.rose.upms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import site.syksy.rose.upms.domain.RouteDO;
import site.syksy.rose.upms.domain.RouteLinkDO;
import site.syksy.rose.upms.service.RouteLinkService;
import site.syksy.rose.upms.service.RouteService;
import site.syksy.rose.web.exception.ForbiddenException;
import site.syksy.rose.web.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@RestController
@Tag(name = "路由与角色菜单操作关联")
@RequestMapping("upms/route/link")
public class RouteLinkController {

    @Autowired
    RouteLinkService routeLinkService;

    @Autowired
    @Lazy
    RouteService routeService;

    @PostMapping
    @Operation(summary = "新增")
    public void post(@RequestParam String routeId, @RequestParam String targetId, @RequestParam String targetType) {
        if (routeLinkService.getByRouteIdAndTargetIdAndTargetType(routeId, targetId, targetType) == null) {
            RouteLinkDO routeLinkDO = new RouteLinkDO();
            routeLinkDO.setRouteId(routeId);
            routeLinkDO.setTargetId(targetId);
            routeLinkDO.setTargetType(targetType);
            routeLinkService.save(routeLinkDO);
        } else {
            throw new ForbiddenException("目标已链接路由，请重新加载数据！");
        }
    }

    @DeleteMapping
    @Operation(summary = "删除")
    public void delete(@RequestParam String routeId, @RequestParam String targetId, @RequestParam String targetType) {
        RouteLinkDO routeLinkDO = routeLinkService.getByRouteIdAndTargetIdAndTargetType(routeId, targetId, targetType);
        if (routeLinkDO == null) {
            throw new NotFoundException();
        } else {
            routeLinkService.removeById(routeLinkDO.getId());
        }
    }

    @GetMapping
    @Operation(summary = "查询")
    public List<RouteDO> get(@RequestParam String targetId, @RequestParam String targetType) {
        List<String> routeIdList = routeLinkService.selectRouteIdByTarget(targetId, targetType);
        List<RouteDO> routeDOList = new ArrayList<>();
        if (routeIdList != null && routeIdList.size() > 0) {
            routeDOList = routeService.listByIds(routeIdList);
        }
        return routeDOList;
    }

}