package site.syksy.qingzhou.upms.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.syksy.qingzhou.upms.constant.RouteLinkType;
import site.syksy.qingzhou.upms.domain.MenuDO;
import site.syksy.qingzhou.upms.domain.RouteDO;
import site.syksy.qingzhou.upms.domain.dto.MenuDTO;
import site.syksy.qingzhou.upms.domain.vo.RelationVO;
import site.syksy.qingzhou.upms.service.MenuService;
import site.syksy.qingzhou.upms.service.RouteLinkService;
import site.syksy.qingzhou.upms.service.RouteService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raspberry
 */
@RestController
@Tag(name = "权限关系图")
@RequestMapping("upms/relation")
public class RelationController {

    private static final Logger log = LoggerFactory.getLogger(RelationController.class);


    @Autowired
    RouteLinkService routeLinkService;

    @Autowired
    MenuService menuService;

    @Autowired
    @Lazy
    RouteService routeService;

    @GetMapping("user/{id}")
    @Operation(summary = "获取用户权限关系图")
    public void user(@PathVariable String id) {

    }

    @GetMapping("role/{id}")
    @Operation(summary = "获取角色权限关系图")
    public void role(@PathVariable String id) {

    }

    @GetMapping("menu/{id}")
    @Operation(summary = "获取菜单权限关系图")
    public RelationVO menu(@PathVariable String id) {
        RelationVO relationVO = new RelationVO();
        MenuDO menuDO = menuService.getById(id);
        relationVO.setId(IdWorker.getId() + "");
        relationVO.setCid(id);
        relationVO.setType("star");
        List<Integer> size = new ArrayList<>();
        size.add(60);
        relationVO.setSize(size);
        relationVO.setLabel(menuDO.getName());
        List<MenuDTO> menuDTOList = menuService.getTreeMenuByParentId(id);
        abc(relationVO, menuDTOList);
        return relationVO;
    }

    private void abc(RelationVO relationVO, List<MenuDTO> menuDTOList) {
        List<RelationVO> children = new ArrayList<>();
        children.addAll(route(relationVO.getCid(), RouteLinkType.MENU));
        for (MenuDTO menuDTO : menuDTOList) {
            RelationVO menu = new RelationVO();
            menu.setId(IdWorker.getId() + "");
            menu.setCid(menuDTO.getId());
            menu.setLabel(menuDTO.getName());
            List<Integer> size = new ArrayList<>();
            size.add(90);
            size.add(30);
            menu.setSize(size);
            menu.setType("ellipse");
            children.add(menu);
            if (menuDTO.getChildren() != null && menuDTO.getChildren().size() > 0) {
                abc(menu, menuDTO.getChildren());
            } else {
                menu.setChildren(route(menuDTO.getId(), RouteLinkType.MENU));
            }
        }
        if (children.size() > 0) {
            relationVO.setChildren(children);
        }
    }

    private List<RelationVO> route(String targetId, String targetType) {
        List<String> routeIds = routeLinkService.selectRouteIdByTarget(targetId, targetType);
        List<RelationVO> relationVOList = new ArrayList<>();
        for (String routeId : routeIds) {
            RouteDO routeDO = routeService.getById(routeId);
            RelationVO http = new RelationVO();
            http.setId(IdWorker.getId() + "");
            http.setLabel(routeDO.getPattern());
            List<Integer> size = new ArrayList<>();
            size.add(20);
            http.setSize(size);
            http.setType("circle");
            relationVOList.add(http);
        }
        return relationVOList;
    }

}
