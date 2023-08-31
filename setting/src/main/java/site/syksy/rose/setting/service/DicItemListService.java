package site.syksy.rose.setting.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.syksy.rose.setting.domain.DicItemListDO;
import site.syksy.rose.setting.mapper.DicItemListMapper;

/**
 * @author Raspberry
 * @since 2021-01-04
 */
@Service
public class DicItemListService extends ServiceImpl<DicItemListMapper, DicItemListDO> {

    /**
     * 根据字典类目ID,删除项
     * @param dicId
     */
    void removeByDicId(String dicId) {
        QueryWrapper<DicItemListDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DicItemListDO::getDicId, dicId);
        remove(queryWrapper);
    }
}
