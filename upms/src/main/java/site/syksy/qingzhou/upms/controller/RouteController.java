package site.syksy.qingzhou.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.syksy.qingzhou.upms.domain.RouteDO;
import site.syksy.qingzhou.upms.domain.vo.RouteEditVO;
import site.syksy.qingzhou.upms.service.ApiService;
import site.syksy.qingzhou.upms.service.RouteService;
import site.syksy.qingzhou.web.exception.ForbiddenException;
import site.syksy.qingzhou.web.exception.NotFoundException;
import site.syksy.qingzhou.web.exception.PreconditionFailedException;
import site.syksy.qingzhou.web.page.MyPage;
import site.syksy.qingzhou.web.page.Pageable;
import site.syksy.qingzhou.web.page.PageableAsQueryParam;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Raspberry
 * @since 2020-08-26
 */
@RestController
@Tag(name = "路由")
@RequestMapping("upms/route")
public class RouteController {

    @Autowired
    @Lazy
    RouteService routeService;

    @Resource
    ApiService apiService;

    @DeleteMapping("{id}")
    @Operation(summary = "删除路由")
    public void deleteRoute(@PathVariable String id) {
        RouteDO routeDO = routeService.getById(id);
        if (routeDO.getOnline()) {
            throw new ForbiddenException("在线HTTP接口不允许删除");
        }
        if (!routeService.removeById(id)) {
            throw new NotFoundException();
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "修改路由")
    public void putRoute(@PathVariable String id,@Validated @RequestBody RouteEditVO routeEditVO) {
        RouteDO routeDO = new RouteDO();
        BeanUtils.copyProperties(routeEditVO, routeDO);
        routeDO.setId(id);
        if (routeEditVO.getValidPeriod() != null && routeEditVO.getValidPeriod().size() > 0) {
            routeDO.setStartDate(routeEditVO.getValidPeriod().get(0));
            routeDO.setEndDate(routeEditVO.getValidPeriod().get(1));
        }
        if(!routeEditVO.getCheckType().equals(-1)){
            AntPathMatcher matcher = new AntPathMatcher();
            if(apiService.webIgnoringAntMatchers(matcher,routeService.getById(id).getPattern())){
                throw new PreconditionFailedException("该接口在配置文件中已设置全局免登录（ANT匹配规则），不允许修改鉴权类型！");
            }
        }
        routeService.updateById(routeDO);
    }

    @GetMapping("{id}")
    @Operation(summary = "获取路由")
    public RouteDO getRoute(@PathVariable String id) {
        RouteDO routeDO = routeService.getById(id);
        if (routeDO == null) {
            throw new NotFoundException();
        }
        List<LocalDateTime> validPeriod = new ArrayList<>();
        if (routeDO.getStartDate() != null) {
            validPeriod.add(routeDO.getStartDate());
        }
        if (routeDO.getEndDate() != null) {
            validPeriod.add(routeDO.getEndDate());
        }
        routeDO.setValidPeriod(validPeriod);
        return routeDO;
    }

    @PostMapping("overload")
    @Operation(summary = "重载路由")
    public void overload() {
        routeService.registeredRoute();
    }

    @GetMapping("list")
    @Operation(summary = "分页")
    @PageableAsQueryParam
    public MyPage<RouteDO> listToList(
            @Parameter(hidden = true) Pageable pageable,
            @RequestParam(required = false) String requestMethod,
            @RequestParam(required = false) String pattern,
            @RequestParam(required = false) String handlerMethod) {
        MyPage<RouteDO> routeDOPage = new MyPage<>(pageable);
        QueryWrapper<RouteDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.hasLength(requestMethod), RouteDO::getRequestMethod, requestMethod)
                .like(StringUtils.hasLength(pattern), RouteDO::getPattern, pattern)
                .like(StringUtils.hasLength(handlerMethod), RouteDO::getHandlerMethod, handlerMethod);

        for (Map.Entry<String, List<String>> entry : pageable.getFilter().entrySet()) {
            queryWrapper.in(entry.getKey(), entry.getValue());
        }
        queryWrapper.orderBy(pageable.getSorter().getCondition(), pageable.getSorter().getAsc(), pageable.getSorter().getValue());
        routeDOPage = routeService.page(routeDOPage, queryWrapper);
        return routeDOPage;
    }

}