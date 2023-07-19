package site.syksy.qingzhou.global.file.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.syksy.qingzhou.global.file.domain.GlobalFile;
import site.syksy.qingzhou.global.file.mapper.GlobalFileMapper;

/**
 * @author Raspberry
 */
@Service
public class GlobalFileService extends ServiceImpl<GlobalFileMapper, GlobalFile> {

    public GlobalFile getByUuid(String uuid) {
        QueryWrapper<GlobalFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GlobalFile::getUuid, uuid);
        return getOne(queryWrapper);
    }
}
