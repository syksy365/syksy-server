package site.syksy.qingzhou.setting.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.syksy.qingzhou.common.utils.TreeUtil;
import site.syksy.qingzhou.setting.domain.DicItemTreeDO;
import site.syksy.qingzhou.setting.service.DicItemTreeService;
import site.syksy.qingzhou.web.exception.NotFoundException;

import java.util.List;

/**
 * @author Raspberry
 * @since 2021-01-04
 */
@RestController
@Tag(name = "树形字典项")
@RequestMapping("setting/dic/item/tree")
public class DicItemTreeController {

    @Autowired
    DicItemTreeService dicItemTreeService;

    @GetMapping
    @Operation(summary = "获取列表")
    public List<DicItemTreeDO> list(@RequestParam String dicId) {
        return dicItemTreeService.listToTree(dicId);
    }

    @PostMapping
    @Operation(summary = "新增")
    public void post(@Validated @RequestBody DicItemTreeDO dicItemTreeDO) {
        List<String> levelIds = dicItemTreeDO.getLevelIds();
        if (levelIds != null && levelIds.size() > 0) {
            dicItemTreeDO.setParentId(levelIds.get(levelIds.size() - 1));
        }
        dicItemTreeService.save(dicItemTreeDO);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除")
    public void delete(@PathVariable String id) {
        DicItemTreeDO dicItemTreeDO = dicItemTreeService.getById(id);
        if (dicItemTreeDO == null) {
            throw new NotFoundException();
        }
        dicItemTreeService.removeById(id);
    }

    @PutMapping("{id}")
    @Operation(summary = "修改")
    public void put(@PathVariable String id,@Validated @RequestBody DicItemTreeDO dicItemTreeDO) {
        List<String> levelIds = dicItemTreeDO.getLevelIds();
        if (levelIds != null && levelIds.size() > 0) {
            dicItemTreeDO.setParentId(levelIds.get(levelIds.size() - 1));
        }
        dicItemTreeDO.setId(id);
        dicItemTreeService.updateById(dicItemTreeDO);
    }

    @GetMapping("{id}")
    @Operation(summary = "获取详情")
    public DicItemTreeDO get(@PathVariable String id) {
        DicItemTreeDO dicItemTreeDO = dicItemTreeService.getById(id);
        List<DicItemTreeDO> dicItemTreeDOList = dicItemTreeService.listToTree(dicItemTreeDO.getDicId());
        List<String> levelIds = TreeUtil.findLinkIds(dicItemTreeDOList, DicItemTreeDO::getChildren, DicItemTreeDO::getId, DicItemTreeDO::getParentId, id);
        dicItemTreeDO.setLevelIds(levelIds);
        return dicItemTreeDO;
    }

    @GetMapping("level/{id}")
    @Operation(summary = "获取层级ID")
    public List<String> getLevel(@PathVariable String id, @RequestParam String dicId) {
        List<DicItemTreeDO> dicItemTreeDOList = dicItemTreeService.listToTree(dicId);
        return TreeUtil.findLinkIds(dicItemTreeDOList, DicItemTreeDO::getChildren, DicItemTreeDO::getId, DicItemTreeDO::getParentId, id);
    }

    @PutMapping("expand/{id}")
    @Operation(summary = "展开和关闭")
    public void expand(@PathVariable String id, @RequestParam Boolean expand) {
        UpdateWrapper<DicItemTreeDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(DicItemTreeDO::getExpand, expand).eq(DicItemTreeDO::getId, id);
        dicItemTreeService.update(updateWrapper);
    }

}