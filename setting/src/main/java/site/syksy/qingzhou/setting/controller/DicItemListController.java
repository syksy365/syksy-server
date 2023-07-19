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
import site.syksy.qingzhou.setting.domain.DicItemListDO;
import site.syksy.qingzhou.setting.service.DicItemListService;
import site.syksy.qingzhou.setting.service.DicService;
import site.syksy.qingzhou.web.exception.NotFoundException;
import site.syksy.qingzhou.web.exception.PreconditionFailedException;
import site.syksy.qingzhou.web.page.MyPage;
import site.syksy.qingzhou.web.page.Pageable;
import site.syksy.qingzhou.web.page.PageableAsQueryParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Raspberry
 * @since 2021-01-04
 */
@RestController
@Tag(name = "列表式字典项")
@RequestMapping("setting/dic/item/list")
public class DicItemListController {

    @Autowired
    DicItemListService dicItemListService;

    @Resource
    DicService dicService;

    @PostMapping
    @Operation(summary = "新增")
    public void post(@Validated @RequestBody DicItemListDO dicItemListDO) {
        dicItemListService.save(dicItemListDO);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除")
    public void delete(@PathVariable String id) {
        DicItemListDO dicItemListDO = dicItemListService.getById(id);
        if (dicItemListDO == null) {
            throw new NotFoundException("数据不存在");
        }
        dicItemListService.removeById(id);
    }

    @PutMapping("{id}")
    @Operation(summary = "修改")
    public void put(@PathVariable String id, @Validated @RequestBody DicItemListDO dicItemListDO) {
        DicItemListDO oldDicItemListDO = dicItemListService.getById(id);
        if (oldDicItemListDO == null) {
            throw new NotFoundException("数据不存在");
        }
        dicItemListDO.setId(id);
        dicItemListService.updateById(dicItemListDO);
    }

    @GetMapping("{id}")
    @Operation(summary = "获取")
    public DicItemListDO get(@PathVariable String id) {
        DicItemListDO dicItemListDO = dicItemListService.getById(id);
        return dicItemListDO;
    }

    @GetMapping("page")
    @Operation(summary = "分页")
    @PageableAsQueryParam
    public MyPage pageDicItemList(
            @Parameter(hidden = true) Pageable pageable,
            @RequestParam(required = false) String dicId) {
        MyPage myPage = new MyPage<>(pageable);
        QueryWrapper<DicItemListDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StringUtils.isNotBlank(dicId), DicItemListDO::getDicId, dicId);
        for (Map.Entry<String, List<String>> entry : pageable.getFilter().entrySet()) {
            queryWrapper.in(entry.getKey(), entry.getValue());
        }
        queryWrapper.orderBy(pageable.getSorter().getCondition(), pageable.getSorter().getAsc(), pageable.getSorter().getValue());
        myPage = dicItemListService.page(myPage, queryWrapper);
        return myPage;
    }

    @GetMapping("list")
    @Operation(summary = "列表")
    public List<DicItemListDO> listDicItemList(@RequestParam String dicName) {
        DicDO dicDO = dicService.getByName(dicName);
        if (dicDO == null) {
            throw new PreconditionFailedException(dicName + "：字典类目不存在");
        }
        QueryWrapper<DicItemListDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DicItemListDO::getDicId, dicDO.getId());
        return dicItemListService.list(queryWrapper);
    }

}