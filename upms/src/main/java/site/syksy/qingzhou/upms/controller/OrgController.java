package site.syksy.qingzhou.upms.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.syksy.qingzhou.common.utils.TreeUtil;
import site.syksy.qingzhou.upms.domain.OrgDO;
import site.syksy.qingzhou.upms.domain.vo.OrgAddVO;
import site.syksy.qingzhou.upms.domain.vo.OrgVO;
import site.syksy.qingzhou.upms.service.OrgService;
import site.syksy.qingzhou.web.exception.ForbiddenException;
import site.syksy.qingzhou.web.exception.NotFoundException;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@RestController
@Tag(name = "组织")
@RequestMapping("upms/org")
@Validated
public class OrgController {

    @Autowired
    OrgService orgService;

    @PostMapping
    @Operation(summary = "新建")
    public String post( @RequestBody OrgAddVO orgAddVO) {
        OrgDO parentOrg = orgService.getById(orgAddVO.getParentId());
        if (parentOrg == null) {
            throw new NotFoundException("父节点不存在");
        }
        OrgDO orgDO = new OrgDO();
        BeanUtils.copyProperties(orgAddVO, orgDO);
        orgService.save(orgDO);
        return orgDO.getId();
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除")
    public void delete(@PathVariable String id) {
        OrgDO orgDO = orgService.getById(id);
        if (orgDO == null) {
            throw new NotFoundException();
        }
        orgService.removeOrg(orgDO);
    }

    @PutMapping("{id}")
    @Operation(summary = "修改")
    public void put( @PathVariable String id, @Size(max = 10) @RequestParam String name) {
        OrgDO orgDO = new OrgDO();
        orgDO.setName(name);
        orgDO.setId(id);
        orgDO.setUpdateTime(LocalDateTime.now());
        orgService.updateById(orgDO);
    }

    @GetMapping("{id}")
    @Operation(summary = "获取")
    public OrgVO get(@PathVariable String id) {
        OrgDO orgDO = orgService.getById(id);
        OrgVO orgVO = new OrgVO();
        BeanUtils.copyProperties(orgDO, orgVO);
        return orgVO;
    }

    @PutMapping("position")
    @Operation(summary = "拖拽位置修改")
    public void drag(@RequestParam String targetId, @RequestParam String dragId, @RequestParam Boolean dropToGap, @RequestParam Boolean isAbove) {
        if (targetId.equals(dragId)) {
            throw new ForbiddenException("拖拽节点和目标节点不能相同");
        }
        OrgDO targetNode = orgService.getById(targetId);
        OrgDO dragNode = orgService.getById(dragId);
        if (targetNode == null) {
            throw new NotFoundException("目标节点未找到，可能已删除，请重新加载数据");
        }
        if (dragNode == null) {
            throw new NotFoundException("拖拽节点未找到，可能已删除，请重新加载数据");
        }
        orgService.drag(targetNode, dragNode, dropToGap, isAbove);
    }

    @GetMapping("/list")
    @Operation(summary = "全局数据")
    public List<OrgDO> list() {
        List<OrgDO> orgDOTree = orgService.allTreeOrg();
        return orgDOTree;
    }

    @PutMapping("expand/{id}")
    @Operation(summary = "展开和关闭")
    public void expand(@PathVariable String id, @RequestParam Boolean expand) {
        UpdateWrapper<OrgDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(OrgDO::getExpand, expand).eq(OrgDO::getId, id);
        orgService.update(updateWrapper);
    }

    @GetMapping("level/{id}")
    @Operation(summary = "获取层级ID")
    public List<String> getLevel(@PathVariable String id) {
        List<OrgDO> orgDOTree = orgService.allTreeOrg();
        return TreeUtil.findLinkIds(orgDOTree, OrgDO::getChildren, OrgDO::getId, OrgDO::getParentId, id);
    }

}