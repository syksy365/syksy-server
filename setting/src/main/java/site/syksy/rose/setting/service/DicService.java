package site.syksy.rose.setting.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.syksy.rose.setting.constant.GenreConstant;
import site.syksy.rose.setting.domain.DicDO;
import site.syksy.rose.setting.mapper.DicMapper;
import site.syksy.rose.web.exception.NotFoundException;

import javax.annotation.Resource;

/**
 * @author Raspberry
 * @since 2021-01-04
 */
@Service
public class DicService extends ServiceImpl<DicMapper, DicDO> {

    @Resource
    DicItemListService dicItemListService;

    @Resource
    DicItemTreeService dicItemTreeService;

    /**
     * 根据名字查询字典类目
     *
     * @param name
     * @return
     */
    public DicDO getByName(String name) {
        QueryWrapper<DicDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DicDO::getName, name);
        return getOne(queryWrapper);
    }


    @Transactional(rollbackFor = Exception.class)
    public void removeById(String id) {
        DicDO dicDO = getById(id);
        if (dicDO == null) {
            throw new NotFoundException("数据不存在");
        }
        super.removeById(id);
        if (dicDO.getGenre().equals(GenreConstant.list)) {
            dicItemListService.removeByDicId(id);
        } else {
            dicItemTreeService.removeByDicId(id);
        }

    }

}
