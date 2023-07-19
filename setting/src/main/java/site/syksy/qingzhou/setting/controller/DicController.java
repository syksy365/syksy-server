package site.syksy.qingzhou.setting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.syksy.qingzhou.setting.domain.DicDO;
import site.syksy.qingzhou.setting.service.DicService;
import site.syksy.qingzhou.web.exception.NotFoundException;
import site.syksy.qingzhou.web.exception.PreconditionFailedException;
import site.syksy.qingzhou.web.page.MyPage;
import site.syksy.qingzhou.web.page.Pageable;
import site.syksy.qingzhou.web.page.PageableAsQueryParam;

import java.util.List;
import java.util.Map;

/**
 * @author Raspberry
 * @since 2021-01-04
 */
@Tag(name = "字典")
@RestController
@RequestMapping("setting/dic")
@Validated
public class DicController {

    @Autowired
    DicService dicService;

    @GetMapping
    @Operation(summary = "获取列表")
    public List<DicDO> list(@RequestParam String genre) {
        QueryWrapper<DicDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DicDO::getGenre, genre);
        return dicService.list(queryWrapper);
    }

    @PostMapping
    @Operation(summary = "新增")
    public void post(@Validated @RequestBody DicDO dicDO) {
        if (dicService.getByName(dicDO.getName()) != null) {
            throw new PreconditionFailedException("类目名已存在!");
        }
        dicService.save(dicDO);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除")
    public void delete(@PathVariable String id) {
        dicService.removeById(id);
    }

    @PutMapping("{id}")
    @Operation(summary = "修改")
    public void put(@PathVariable String id, @Validated @RequestBody DicDO dicDO) {
        if (dicService.getByName(dicDO.getName()) != null) {
            throw new PreconditionFailedException("类目名已存在!");
        }
        DicDO oldDicDO = dicService.getById(id);
        if (oldDicDO == null) {
            throw new NotFoundException("类目不存在！");
        }
        dicDO.setId(id);
        dicService.updateById(dicDO);
    }

    @GetMapping("{id}")
    @Operation(summary = "获取")
    public DicDO get(@PathVariable String id) {
        return dicService.getById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询")
    @PageableAsQueryParam
    public MyPage page(
            @Parameter(hidden = true) Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String genre) {
        MyPage myPage = new MyPage<>(pageable);
        QueryWrapper<DicDO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.lambda().like(DicDO::getName, name);
        }
        if (StringUtils.isNotBlank(genre)) {
            queryWrapper.lambda().eq(DicDO::getGenre, genre);
        }
        for (Map.Entry<String, List<String>> entry : pageable.getFilter().entrySet()) {
            queryWrapper.in(entry.getKey(), entry.getValue());
        }
        queryWrapper.orderBy(pageable.getSorter().getCondition(), pageable.getSorter().getAsc(), pageable.getSorter().getValue());
        myPage = dicService.page(myPage, queryWrapper);
        return myPage;
    }

}