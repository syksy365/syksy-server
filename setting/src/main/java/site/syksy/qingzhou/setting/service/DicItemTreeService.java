package site.syksy.qingzhou.setting.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.syksy.qingzhou.common.utils.TreeUtil;
import site.syksy.qingzhou.setting.domain.DicItemTreeDO;
import site.syksy.qingzhou.setting.mapper.DicItemTreeMapper;

import java.util.List;

/**
 * @author Raspberry
 * @since 2021-01-04
 */
@Service
public class DicItemTreeService extends ServiceImpl<DicItemTreeMapper, DicItemTreeDO> {


    public List<DicItemTreeDO> listToTree(String dicId) {
        QueryWrapper<DicItemTreeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DicItemTreeDO::getDicId, dicId);
        List<DicItemTreeDO> dicItemTreeDOList = list(queryWrapper);
        return TreeUtil.polymerizationTree(dicItemTreeDOList, DicItemTreeDO::getId, DicItemTreeDO::getParentId, DicItemTreeDO::getNextId, DicItemTreeDO::setChildren);
    }

    /**
     * 根据字典类目ID,删除项
     *
     * @param dicId
     */
    public void removeByDicId(String dicId) {
        QueryWrapper<DicItemTreeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DicItemTreeDO::getDicId, dicId);
        remove(queryWrapper);
    }

}
