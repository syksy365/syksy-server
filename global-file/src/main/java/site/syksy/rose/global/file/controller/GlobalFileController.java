package site.syksy.rose.global.file.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.syksy.rose.global.file.FileTool;
import site.syksy.rose.global.file.HttpDownload;
import site.syksy.rose.global.file.constant.ImageSizeType;
import site.syksy.rose.global.file.domain.GlobalFile;
import site.syksy.rose.global.file.domain.GlobalFileImageDO;
import site.syksy.rose.global.file.service.GlobalFileImageService;
import site.syksy.rose.global.file.service.GlobalFileService;
import site.syksy.rose.web.exception.PreconditionFailedException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Raspberry
 */
@RestController
@RequestMapping("/global/file")
@Tag(name = "全局文件管理")
public class GlobalFileController {
    @Resource
    GlobalFileService globalFileService;

    @Resource
    GlobalFileImageService globalFileImageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "文件上传（参数名：file）")
    @Transactional(rollbackFor = Exception.class)
    public String upload(@RequestPart MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        Long size = file.getSize();
        String ext = fileName.substring(file.getOriginalFilename().lastIndexOf(FileTool.DOT) + 1);
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        String filePath = FileTool.getFilePath(uuid + "-" + fileName);
        file.transferTo(Paths.get(filePath));

        GlobalFile globalFile = new GlobalFile();
        globalFile.setUuid(uuid);
        globalFile.setSize(size);
        globalFile.setExt(ext);
        globalFile.setFileName(fileName);
        globalFile.setFilePath(filePath);
        globalFile.setUploadTime(LocalDateTime.now());
        globalFileService.save(globalFile);

        String e = ext.trim().toUpperCase();
        if (e.equals("JPEG") || e.equals("JPG") || e.equals("PNG") || e.equals("GIF") || e.equals("BMP") || e.equals("WBMP")) {
            globalFileImageService.handleSave(globalFile);
        }
        return uuid;
    }

    @GetMapping(value = "{uuid}", produces = "application/octet-stream")
    @Operation(summary = "文件下载")
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable String uuid) {
        GlobalFile globalFile = globalFileService.getByUuid(uuid);
        HttpDownload.download(request, response, globalFile.getFilePath(), globalFile.getFileName());
    }

    @GetMapping(value = "image/small/{uuid}", produces = "application/octet-stream")
    @Operation(summary = "图片缩率图下载")
    public void imageSmallDownload(HttpServletRequest request, HttpServletResponse response, @PathVariable String uuid) {
        GlobalFile globalFile = globalFileService.getByUuid(uuid);
        GlobalFileImageDO globalFileImageDO = globalFileImageService.getByFileIdAndSizeType(globalFile.getId(), ImageSizeType.small);
        if (globalFileImageDO == null) {
            String e = globalFile.getExt().trim().toUpperCase();
            if (e.equals("JPEG") || e.equals("JPG") || e.equals("PNG") || e.equals("GIF") || e.equals("BMP") || e.equals("WBMP")) {
                globalFileImageDO = globalFileImageService.handleSave(globalFile);
            } else {
                throw new PreconditionFailedException("图片不存在");
            }
        }
        HttpDownload.download(request, response, globalFileImageDO.getFilePath(), globalFileImageDO.getFileName());
    }

    @GetMapping("info/{uuid}")
    @Operation(summary = "获取文件详情")
    public GlobalFile getInfo(@PathVariable String uuid) {
        return globalFileService.getByUuid(uuid);
    }


}
