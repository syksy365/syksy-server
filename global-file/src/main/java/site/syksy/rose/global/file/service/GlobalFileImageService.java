package site.syksy.rose.global.file.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import site.syksy.rose.global.file.FileTool;
import site.syksy.rose.global.file.constant.ImageSizeType;
import site.syksy.rose.global.file.domain.GlobalFile;
import site.syksy.rose.global.file.domain.GlobalFileImageDO;
import site.syksy.rose.global.file.mapper.GlobalFileImageMapper;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Raspberry
 * @since 2023-02-19
 */
@Service
public class GlobalFileImageService extends ServiceImpl<GlobalFileImageMapper, GlobalFileImageDO> {

    private static final Logger log = LoggerFactory.getLogger(GlobalFileImageService.class);


    /**
     * 处理保存
     *
     * @param globalFile
     * @return
     */
    public GlobalFileImageDO handleSave(GlobalFile globalFile) {
        GlobalFileImageDO globalFileImageDO = new GlobalFileImageDO();
        globalFileImageDO.setFileId(globalFile.getId());
        globalFileImageDO.setFileName(globalFile.getFileName());
        try {
            String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String filePath = FileTool.getFilePath(uuid + "-" + globalFile.getFileName());
            Thumbnails.of(globalFile.getFilePath())
                    .size(200, 300)
                    .toFile(filePath);
            globalFileImageDO.setUuid(uuid);
            globalFileImageDO.setFilePath(filePath);
            globalFileImageDO.setSizeType(ImageSizeType.small);
        } catch (IOException e) {
            log.error("图片按比例缩放发生错误：" + e.getMessage());
        } catch (Exception e) {
            log.error("获取文件路径发生错误：" + e.getMessage());
        }
        super.save(globalFileImageDO);
        return globalFileImageDO;
    }

    /**
     * 根据文件ID和图片大小类型获取图片信息
     * @param fileId
     * @param sizeType
     * @return
     */
    public GlobalFileImageDO getByFileIdAndSizeType(Long fileId, ImageSizeType sizeType) {
        QueryWrapper<GlobalFileImageDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GlobalFileImageDO::getFileId, fileId).eq(GlobalFileImageDO::getSizeType, sizeType);
        return getOne(queryWrapper);
    }
}
