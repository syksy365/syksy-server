package site.syksy.rose.upms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import site.syksy.rose.common.utils.TreeUtil;
import site.syksy.rose.upms.domain.OrgDO;
import site.syksy.rose.upms.mapper.OrgMapper;
import site.syksy.rose.web.exception.ForbiddenException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@Service
@CacheConfig(cacheNames = "s_upms_org", keyGenerator = "cacheKeyGenerator")
public class OrgService extends ServiceImpl<OrgMapper, OrgDO> {

    @Lazy
    @Autowired
    UserService userService;

    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(OrgDO entity) {
        if (getNodeByParentIdAndName(entity.getParentId(), entity.getName()) != null) {
            throw new RuntimeException("同级下存在同名");
        }
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        OrgDO lastNode = getLastNodeByParentId(entity.getParentId());
        baseMapper.insert(entity);
        if (!StringUtils.isEmpty(lastNode)) {
            updateNextId(lastNode.getId(), entity.getId());
        }
        return true;
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean updateById(OrgDO entity) {
        OrgDO oldOrgDO = getById(entity.getId());
        if (StringUtils.isEmpty(oldOrgDO)) {
            throw new RuntimeException("节点不存在");
        }
        OrgDO orgDO = getNodeByParentIdAndName(oldOrgDO.getParentId(), entity.getName());
        if (!StringUtils.isEmpty(orgDO) && !orgDO.getId().equals(entity.getId())) {
            throw new RuntimeException("同级下存在同名");
        }
        return super.updateById(entity);
    }

    /**
     * 删除节点
     *
     * @param orgDO
     * @return
     */
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean removeOrg(OrgDO orgDO) {
        List<OrgDO> orgDOTreeList = allTreeOrg();
        List<OrgDO> children = TreeUtil.getChildren(orgDOTreeList, OrgDO::getChildren, OrgDO::getId, orgDO.getId());
        if (!CollectionUtils.isEmpty(children)) {
            throw new ForbiddenException("节点下存在子节点，请先移除该节点下所有子节点");
        }
        if (userService.countUserByOrgId(orgDO.getId()) > 0) {
            throw new ForbiddenException("有用户绑定在该组织节点下，请先移除用户再删除");
        }
        OrgDO previousNode = getPreviousNodeById(orgDO.getId());
        if (previousNode != null) {
            updateNextId(previousNode.getId(), orgDO.getNextId());
        }
        return super.removeById(orgDO.getId());
    }

    /**
     * 注意并发操作的问题
     *
     * @param targetNode 目标节点ID
     * @param dragNode   拖拽节点ID
     * @param dropToGap  是否拖拽到节点之间的缝隙中，true代表目标节点和拖拽节点是邻居关系，false代表目标节点和拖拽节点是父子关系（父子关系时放入子节点尾部）
     * @param isAbove    目标节点和拖拽节点是邻居关系时，true代表放在目标节点上方，false代表放在目标节点下方
     */
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void drag(OrgDO targetNode, OrgDO dragNode, Boolean dropToGap, Boolean isAbove) {
        List<OrgDO> orgDOTreeList = allTreeOrg();
        OrgDO dragTopNode = TreeUtil.findPreviousNode(orgDOTreeList, OrgDO::getChildren, OrgDO::getNextId, dragNode.getId());
        //更新拖拽节点原上节点
        if (dragTopNode != null) {
            updateNextId(dragTopNode.getId(), dragNode.getNextId());
        }
        if (dropToGap) {
            if (isAbove) {
                //更新拖拽节点现上节点，设置下节点
                dragNode.setNextId(targetNode.getId());
                OrgDO targetTopNode = TreeUtil.findPreviousNode(orgDOTreeList, OrgDO::getChildren, OrgDO::getNextId, targetNode.getId());
                if (targetTopNode != null) {
                    updateNextId(targetTopNode.getId(), dragNode.getNextId());
                }
            } else {
                //设置拖拽节点和目标节点下节点
                dragNode.setNextId(targetNode.getNextId());
                targetNode.setNextId(dragNode.getId());
            }
            //设置拖拽节点父节点
            dragNode.setParentId(targetNode.getParentId());
        } else {
            OrgDO endNode = TreeUtil.findLastNode(orgDOTreeList, OrgDO::getChildren, OrgDO::getId, OrgDO::getNextId, targetNode.getId());
            dragNode.setParentId(targetNode.getId());
            dragNode.setNextId(null);
            if (endNode != null) {
                updateNextId(endNode.getId(), dragNode.getId());
            }
        }
        baseMapper.update(null,
                Wrappers.<OrgDO>lambdaUpdate()
                        .set(OrgDO::getNextId, dragNode.getNextId())
                        .set(OrgDO::getParentId, dragNode.getParentId())
                        .eq(OrgDO::getId, dragNode.getId())
        );
    }

    /**
     * 获取前一个节点
     *
     * @param id
     * @return
     */
    @Cacheable
    private OrgDO getPreviousNodeById(String id) {
        QueryWrapper<OrgDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrgDO::getNextId, id);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据父节点和名字查找节点
     *
     * @param parentId
     * @param name
     * @return
     */
    @Cacheable
    private OrgDO getNodeByParentIdAndName(String parentId, String name) {
        QueryWrapper<OrgDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrgDO::getParentId, parentId)
                .eq(OrgDO::getName, name);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 查找该父点下级列表最后一个节点
     *
     * @param parentId 父节点ID
     * @return 下级列表最后一个节点
     */
    @Cacheable
    private OrgDO getLastNodeByParentId(String parentId) {
        QueryWrapper<OrgDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrgDO::getParentId, parentId).isNull(OrgDO::getNextId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 更新单链ID
     *
     * @param id
     * @param nextId
     */
    @CacheEvict(allEntries = true)
    private void updateNextId(String id, String nextId) {
        baseMapper.update(null,
                Wrappers.<OrgDO>lambdaUpdate()
                        .set(OrgDO::getNextId, nextId)
                        .eq(OrgDO::getId, id)
        );
    }

    /**
     * 获取全部菜单数据
     *
     * @return
     */
    @Cacheable
    public List<OrgDO> allTreeOrg() {
        List<OrgDO> orgDOList = list();
        return TreeUtil.polymerizationTree(orgDOList, OrgDO::getId, OrgDO::getParentId, OrgDO::getNextId, OrgDO::setChildren);
    }

    /**
     * 根据组织id,获取从该ID起的所有子节点ID集合
     *
     * @param id
     * @return
     */
    @Cacheable
    public List<String> findToLast(String id) {
        List<OrgDO> orgDOTreeList = allTreeOrg();
        List<String> ids = TreeUtil.getChildrenIds(orgDOTreeList, OrgDO::getChildren, OrgDO::getId, id);
        ids.add(id);
        return ids;
    }


    /**
     * 根据组织id,获取从根到该ID的完整层级ID集合
     *
     * @param id
     * @return
     */
    @Cacheable
    public List<String> findLink(String id) {
        List<OrgDO> orgDOTreeList = allTreeOrg();
        return TreeUtil.findLinkIds(orgDOTreeList, OrgDO::getChildren, OrgDO::getId, OrgDO::getParentId, id);
    }

    /**
     * 根据组织id,获取从根到该ID的完整层级ID集合以及翻译
     *
     * @param id
     * @return
     */
    public void findLink(String id, List<String> orgIds, Map<String, String> orgs) {
        List<OrgDO> orgDOTreeList = allTreeOrg();
        TreeUtil.findLink(orgDOTreeList, OrgDO::getChildren, OrgDO::getId, OrgDO::getParentId, OrgDO::getName, id, orgIds, orgs);
    }

    @Cacheable
    @Override
    public OrgDO getById(Serializable id) {
        return super.getById(id);
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean update(Wrapper<OrgDO> updateWrapper) {
        return super.update(updateWrapper);
    }
}
