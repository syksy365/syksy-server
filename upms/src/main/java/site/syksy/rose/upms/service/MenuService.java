package site.syksy.rose.upms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import site.syksy.rose.upms.constant.MoveDirection;
import site.syksy.rose.common.utils.BeanCopyUtil;
import site.syksy.rose.common.utils.TreeUtil;
import site.syksy.rose.upms.constant.MenuType;
import site.syksy.rose.upms.constant.RouteLinkType;
import site.syksy.rose.upms.domain.MenuDO;
import site.syksy.rose.upms.domain.dto.MenuDTO;
import site.syksy.rose.upms.mapper.MenuMapper;
import site.syksy.rose.web.exception.NotFoundException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@Service
@CacheConfig(cacheNames = "s_upms_menu", keyGenerator = "cacheKeyGenerator")
public class MenuService extends ServiceImpl<MenuMapper, MenuDO> {

    private static final Logger log = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    RouteLinkService routeLinkService;

    @Autowired
    RoleMenuService roleMenuService;

    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(MenuDO entity) {
        if (getNodeByParentIdAndName(entity.getParentId(), entity.getName()) != null) {
            throw new RuntimeException("同级下存在同名");
        }
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        List<MenuDTO> treeNodeList = allTreeMenu();
        MenuDTO endNode = TreeUtil.findLastNode(treeNodeList, MenuDTO::getChildren, MenuDTO::getId, MenuDTO::getNextId, entity.getParentId());
        super.save(entity);
        if (endNode != null) {
            updateNextId(endNode.getId(), entity.getId());
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    @Override
    public boolean updateById(MenuDO entity) {
        List<MenuDTO> treeNodeList = allTreeMenu();
        MenuDO oldMenuDO = getById(entity.getId());
        if (oldMenuDO == null) {
            throw new RuntimeException("节点不存在");
        }

        String parentId = entity.getParentId();
        if (!Objects.equals(parentId, oldMenuDO.getParentId())) {
            MenuDO parentMenuDO = getById(parentId);
            if (parentMenuDO != null && parentMenuDO.getGenre().equals(MenuType.ACTION)) {
                throw new RuntimeException("父节点是按钮，不能添加子节点");
            }
            MenuDTO previousNode = TreeUtil.findPreviousNode(treeNodeList, MenuDTO::getChildren, MenuDTO::getNextId, entity.getId());
            if (previousNode != null) {
                updateNextId(previousNode.getId(), oldMenuDO.getNextId());
            }
            MenuDTO endNode = TreeUtil.findLastNode(treeNodeList, MenuDTO::getChildren, MenuDTO::getId, MenuDTO::getNextId, parentId);
            if (endNode != null) {
                updateNextId(endNode.getId(), entity.getId());
            }
            updateNextId(entity.getId(), null);
        }

        MenuDO menuDO = getNodeByParentIdAndName(oldMenuDO.getParentId(), entity.getName());
        if (menuDO != null && !menuDO.getId().equals(entity.getId())) {
            throw new RuntimeException("同级下存在同名");
        }
        return super.updateById(entity);
    }

    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean remove(MenuDO menuDO) {
        List<MenuDTO> treeNodeList = allTreeMenu();
        if (!CollectionUtils.isEmpty(TreeUtil.getChildren(treeNodeList, MenuDTO::getChildren, MenuDTO::getId, menuDO.getId()))) {
            throw new RuntimeException("节点下存在子节点，请先移除该节点下所有子节点");
        }
        MenuDO previousNode = getPreviousNodeById(menuDO.getId());
        if (previousNode != null) {
            updateNextId(previousNode.getId(), menuDO.getNextId());
        }
        roleMenuService.removeByMenuId(menuDO.getId());
        routeLinkService.removeByTargetIdAndTargetType(menuDO.getId(), RouteLinkType.MENU);
        return removeById(menuDO.getId());
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    /**
     * 移动位置顺序
     *
     * @param menuDO
     * @param direction
     */
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void move(MenuDO menuDO, MoveDirection direction) {
        List<MenuDTO> treeNodeList = allTreeMenu();
        MenuDTO previousNode = TreeUtil.findPreviousNode(treeNodeList, MenuDTO::getChildren, MenuDTO::getNextId, menuDO.getId());
        switch (direction) {
            case up:
                if (previousNode != null) {
                    updateNextId(previousNode.getId(), menuDO.getNextId());
                    updateNextId(menuDO.getId(), previousNode.getId());
                    MenuDTO ppNode = TreeUtil.findPreviousNode(treeNodeList, MenuDTO::getChildren, MenuDTO::getNextId, previousNode.getId());
                    if (ppNode != null) {
                        updateNextId(ppNode.getId(), menuDO.getId());
                    }
                }
                break;
            case down:
                String nextId = menuDO.getNextId();
                if (StringUtils.isNotBlank(nextId)) {
                    MenuDO nextNode = getById(menuDO.getNextId());
                    if (nextNode != null) {
                        updateNextId(menuDO.getId(), nextNode.getNextId());
                        updateNextId(nextNode.getId(), menuDO.getId());
                        if (previousNode != null) {
                            updateNextId(previousNode.getId(), menuDO.getNextId());
                        }
                    }
                }
                break;
        }
    }

    /**
     * 拖拽节点，注意并发操作的问题
     *
     * @param targetNode 目标节点ID
     * @param dragNode   拖拽节点ID
     * @param dropToGap  是否拖拽到节点之间的缝隙中，true代表目标节点和拖拽节点是邻居关系，false代表目标节点和拖拽节点是父子关系（父子关系时放入子节点尾部）
     * @param isAbove    目标节点和拖拽节点是邻居关系时，true代表放在目标节点上方，false代表放在目标节点下方
     */
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void drag(MenuDO targetNode, MenuDO dragNode, Boolean dropToGap, Boolean isAbove) {
        List<MenuDTO> treeNodeList = allTreeMenu();

        MenuDTO menuDTO = TreeUtil.findPreviousNode(treeNodeList, MenuDTO::getChildren, MenuDTO::getNextId, dragNode.getId());

        //更新拖拽节点原上节点
        if (menuDTO != null) {
            updateNextId(menuDTO.getId(), dragNode.getNextId());
        }

        if (dropToGap) {
            if (isAbove) {
                //更新拖拽节点现上节点，设置下节点
                dragNode.setNextId(targetNode.getId());
                MenuDTO targetTopNode = TreeUtil.findPreviousNode(treeNodeList, MenuDTO::getChildren, MenuDTO::getNextId, targetNode.getId());
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
            if (MenuType.ACTION.equals(targetNode.getGenre())) {
                throw new RuntimeException("父节点为操作类型，不能放在操作类型下一级");
            }

            MenuDTO endNode = TreeUtil.findLastNode(treeNodeList, MenuDTO::getChildren, MenuDTO::getId, MenuDTO::getNextId, targetNode.getId());

            dragNode.setParentId(targetNode.getId());
            dragNode.setNextId(null);
            if (endNode != null) {
                updateNextId(endNode.getId(), dragNode.getId());
            }
        }

        baseMapper.update(null,
                Wrappers.<MenuDO>lambdaUpdate()
                        .set(MenuDO::getNextId, dragNode.getNextId())
                        .set(MenuDO::getParentId, dragNode.getParentId())
                        .eq(MenuDO::getId, dragNode.getId())
        );
    }

    /**
     * 获取全部菜单数据
     *
     * @return
     */
    @Cacheable
    public List<MenuDO> allMenu() {
        List<MenuDO> menuDOList = list();
        return menuDOList;
    }

    /**
     * 获取前一个节点
     *
     * @param id
     * @return
     */
    private MenuDO getPreviousNodeById(String id) {
        QueryWrapper<MenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MenuDO::getNextId, id);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据父节点和名字查找节点
     *
     * @param parentId
     * @param name
     * @return
     */
    private MenuDO getNodeByParentIdAndName(String parentId, String name) {
        QueryWrapper<MenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MenuDO::getParentId, parentId)
                .eq(MenuDO::getName, name);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 更新单链ID
     *
     * @param id
     * @param nextId
     */
    private void updateNextId(String id, String nextId) {
        baseMapper.update(null,
                Wrappers.<MenuDO>lambdaUpdate()
                        .set(MenuDO::getNextId, nextId)
                        .eq(MenuDO::getId, id)
        );
    }

    /**
     * 将全部菜单转换成树形结构返回
     *
     * @return
     */
    @Cacheable
    public List<MenuDTO> allTreeMenu() {
        List<MenuDO> menuDOList = allMenu();
        List<MenuDTO> menuDTOList = BeanCopyUtil.copyListProperties(menuDOList, MenuDTO::new);
        List<MenuDTO> treeNodes = TreeUtil.polymerizationTree(menuDTOList, MenuDTO::getId, MenuDTO::getParentId, MenuDTO::getNextId, MenuDTO::setChildren);
        return treeNodes;
    }


    /**
     * 将全部菜单(不包含操作)转换成树形结构返回
     *
     * @return
     */
    @Cacheable
    public List<MenuDTO> allDirectoryToTree() {
        List<MenuDO> menuDOList = allMenu();
        List<MenuDTO> menuDTOList = BeanCopyUtil.copyListProperties(menuDOList, MenuDTO::new);
        List<MenuDTO> treeNodes = TreeUtil.polymerizationTree(menuDTOList,
                MenuDTO::getId,
                MenuDTO::getParentId,
                MenuDTO::getNextId,
                MenuDTO::setChildren,
                (menuDTO) -> {
                    if (menuDTO.getGenre().equals(MenuType.DIRECTORY)) {
                        return true;
                    } else {
                        return false;
                    }
                });
        return treeNodes;
    }

    /**
     * 将全部菜单(不包含操作)转换成树形结构,并禁用指定项返回
     *
     * @param disabledId
     * @return
     */
    @Cacheable
    public List<MenuDTO> allDirectoryToTree(String disabledId) {
        List<MenuDTO> treeNodes = allDirectoryToTree();
        TreeUtil.traversing(treeNodes, MenuDTO::getChildren, o -> {
            if (o.getId().equals(disabledId)) {
                o.setDisabled(true);
            }
        });
        return treeNodes;
    }


    /**
     * 根据父节点名字和菜单ID获取树形菜单
     *
     * @param parentName
     * @param menuIds
     * @return
     */
    @Cacheable
    public List<MenuDTO> getTreeMenu(String parentName, List<String> menuIds) {
        List<MenuDTO> menuDTOTreeList = new ArrayList<>();
        List<MenuDTO> treeMenu = allTreeMenu();
        if (menuIds != null && menuIds.size() > 0) {
            getMenuByParentName(treeMenu, menuDTOTreeList, parentName);
            TreeUtil.remove(menuDTOTreeList, MenuDTO::getChildren, o -> {
                if (menuIds.stream().filter(id -> id.equals(o.getId())).count() > 0) {
                    return false;
                } else {
                    return true;
                }
            });
        }
        return menuDTOTreeList;
    }

    @Cacheable
    public List<MenuDTO> getTreeMenuByParentId(String parentId) {
        List<MenuDO> menuDOList = allMenu();
        List<MenuDTO> menuDTOList = BeanCopyUtil.copyListProperties(menuDOList, MenuDTO::new);
        List<MenuDTO> treeNodes = TreeUtil.polymerizationTree(menuDTOList, MenuDTO::getId, MenuDTO::getParentId, MenuDTO::getNextId, MenuDTO::setChildren);
        List<MenuDTO> menuDTOTreeList = new ArrayList<>();
        getMenuByParentId(treeNodes, menuDTOTreeList, parentId);
        return menuDTOTreeList;
    }

    /**
     * 根据父节点名字获取子节点
     *
     * @param menuDTOList
     * @param parentName
     * @return
     */
    private void getMenuByParentName(List<MenuDTO> menuDTOList, List<MenuDTO> menuDTOTreeList, String parentName) {
        for (MenuDTO menuDTO : menuDTOList) {
            if (menuDTO.getName().equals(parentName)) {
                menuDTOTreeList.addAll(menuDTO.getChildren());
                break;
            } else if (menuDTO.getChildren() != null) {
                getMenuByParentName(menuDTO.getChildren(), menuDTOTreeList, parentName);
            }
        }
    }

    /**
     * 根据父节点Id获取子节点
     *
     * @param menuDTOList
     * @param parentId
     * @return
     */
    private void getMenuByParentId(List<MenuDTO> menuDTOList, List<MenuDTO> menuDTOTreeList, String parentId) {
        for (MenuDTO menuDTO : menuDTOList) {
            if (menuDTO.getId().equals(parentId)) {
                menuDTOTreeList.addAll(menuDTO.getChildren());
                break;
            } else if (menuDTO.getChildren() != null) {
                getMenuByParentId(menuDTO.getChildren(), menuDTOTreeList, parentId);
            }
        }
    }

    @Cacheable
    @Override
    public MenuDO getById(Serializable id) {
        MenuDO menuDO = super.getById(id);
        if (menuDO == null) {
            log.error("未找到菜单信息,ID:{}", id);
            throw new NotFoundException("未找到菜单信息");
        }
        return menuDO;
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean update(Wrapper<MenuDO> updateWrapper) {
        return super.update(updateWrapper);
    }
}
