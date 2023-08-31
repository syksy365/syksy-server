/*
 * Copyright 2020-2030 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package site.syksy.rose.file.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import site.syksy.rose.common.properties.FileDirectoryProperties;
import site.syksy.rose.common.properties.QingZhouProperties;
import site.syksy.rose.file.domain.FileDO;
import site.syksy.rose.file.domain.FileListVO;
import site.syksy.rose.file.domain.FileVO;
import site.syksy.rose.file.util.ZipUtil;
import site.syksy.rose.web.exception.ForbiddenException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Raspberry
 */
@RestController
@Tag(name = "文件管理")
@CrossOrigin(exposedHeaders = {"Location", "Upload-Offset"})
@RequestMapping("file")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    private final TusFileUploadService tusFileUploadService;

    private final Path uploadDirectory;

    private final Path tusUploadDirectory;

    public FileController(TusFileUploadService tusFileUploadService,
                          QingZhouProperties qingZhouProperties) {
        this.tusFileUploadService = tusFileUploadService;
        FileDirectoryProperties fileDirectory = qingZhouProperties.getFileDirectory();

        this.uploadDirectory = Paths.get(fileDirectory.getAppUploadDirectory());
        try {
            Files.createDirectories(this.uploadDirectory);
        } catch (IOException e) {
            throw new RuntimeException("create upload directory", e);
        }

        this.tusUploadDirectory = Paths.get(fileDirectory.getTusUploadDirectory());
    }

    @Operation(summary = "文件上传")
    @RequestMapping(value = {
            "upload",
            "upload/**"}, method = {
            RequestMethod.POST,
            RequestMethod.PATCH,
            RequestMethod.HEAD,
            RequestMethod.DELETE,
            RequestMethod.GET})
    public void upload(HttpServletRequest servletRequest,
                       HttpServletResponse servletResponse) throws IOException {
        this.tusFileUploadService.process(servletRequest, servletResponse);
        String uploadURI = servletRequest.getRequestURI();

        UploadInfo uploadInfo;
        try {
            uploadInfo = this.tusFileUploadService.getUploadInfo(uploadURI);
        } catch (IOException | TusException e) {
            throw new RuntimeException("get upload info", e);
        }

        if (uploadInfo != null && !uploadInfo.isUploadInProgress()) {
            try (InputStream is = this.tusFileUploadService.getUploadedBytes(uploadURI)) {
                Path output = Paths.get(uploadInfo.getFileName());
//                Path output = this.uploadDirectory.resolve(uploadInfo.getFileName());
                if (!Files.exists(output.getParent())) {
                    FileUtils.forceMkdir(output.getParent().toFile());
                }
                Files.copy(is, output, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException | TusException e) {
                throw new RuntimeException("get uploaded bytes", e);
            }

            try {
                this.tusFileUploadService.deleteUpload(uploadURI);
            } catch (IOException | TusException e) {
                throw new RuntimeException("delete upload", e);
            }
        }
    }

    @Operation(summary = "获取文件列表")
    @GetMapping("/files")
    public FileListVO getFiles(FileDO fileDO) {
        if (StringUtils.isBlank(fileDO.getPath())) {
            return null;
        }
        File dir = new File(fileDO.getPath());
        List<FileVO> fileList = new ArrayList<>();
        FileListVO result = new FileListVO();
        File parentFile = dir.getParentFile();

        // 如果不是根目录，添加上级目录
        if (!StringUtils.equals(fileDO.getPath(), this.uploadDirectory.toString())) {
            FileVO parent = new FileVO();
            parent.setName("..");
            parent.setPath(parentFile.getPath());
            parent.setSize(parentFile.length());
            parent.setType(parentFile.isDirectory() ? "folder" : "file");
            parent.setLastModifiedDateTime(DateFormatUtils.format(new Date(parentFile.lastModified()), "yyyy-MM-dd HH:mm:ss"));
            parent.setKey(parent.getPath());
            fileList.add(parent);
        }

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (Objects.isNull(files)) {
                return null;
            }
            for (File file : files) {
                FileVO fileVO = new FileVO();
                fileVO.setName(file.getName());
                fileVO.setPath(file.getPath());
                fileVO.setSize(file.length());
                fileVO.setType(file.isDirectory() ? "folder" : "file");
                fileVO.setLastModifiedDateTime(DateFormatUtils.format(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss"));
                fileVO.setKey(fileVO.getPath());

                fileList.add(fileVO);
            }
        }
        FileVO current = new FileVO();
        current.setName(dir.getPath());
        current.setPath(dir.getParent());
        current.setSize(dir.length());
        current.setType(dir.isDirectory() ? "folder" : "file");
        current.setLastModifiedDateTime(DateFormatUtils.format(new Date(dir.lastModified()), "yyyy-MM-dd HH:mm:ss"));
        current.setKey(current.getPath());

        result.setFileList(fileList);
        result.setParentReference(current);
        return result;
    }


    /**
     * 创建文件
     *
     * @param fileDO
     * @throws IOException
     */
    @Operation(summary = "创建文件")
    @PostMapping("/createFile")
    public void createFile(FileDO fileDO) throws IOException {
        if (StringUtils.isBlank(fileDO.getPath())) {
            return;
        }
        // 判断路径是否已经存在, 存在但是是目录，也需要创建
        try {
            Path targetPath = Path.of(fileDO.getPath(), fileDO.getName());
            Files.createFile(targetPath);
        } catch (Exception e) {
            throw new ForbiddenException("文件或目录已存在");
        }

    }

    /**
     * 创建文件夹
     *
     * @param fileDO
     */
    @Operation(summary = "创建文件夹")
    @PostMapping("/createFolder")
    public void createFolder(FileDO fileDO) {
        if (StringUtils.isBlank(fileDO.getPath())) {
            return;
        }
        // 构建目标路径
        Path targetPath = Path.of(fileDO.getPath(), fileDO.getName());
        // 判断路径是否已经存在, 存在但是是文件，也需要创建
        try {
            Files.createDirectories(targetPath);
        } catch (Exception e) {
            throw new ForbiddenException("文件或目录已存在");
        }

    }

    /**
     * 删除文件，如果是文件夹，递归删除所有子文件, 如果为空，不抛出异常
     */
    @Operation(summary = "删除文件")
    @PostMapping("/deleteFile")
    public void deleteFile(@RequestBody FileDO fileDO) {
        List<String> paths = fileDO.getPaths();
        if (Objects.nonNull(paths)) {
            paths.forEach(p -> {
                Path p1 = Path.of(p);
                FileUtils.deleteQuietly(p1.toFile());
            });
        }
    }

    /**
     * 文件下载接口:
     * 单个路径直接下载，如果路径是目录，则打包成zip下。
     * 多个文件打包成zip下载，如果路径是目录，则递归获取目录下的所有文件打包成zip下载。
     */
    @Operation(summary = "下载文件")
    @PostMapping("/downloadFile")
    public void downloadFile(@RequestBody FileDO fileDO, HttpServletResponse response) throws IOException {
        List<String> paths = fileDO.getPaths();
        if (Objects.isNull(paths) || paths.isEmpty()) {
            return;
        }
        // 设置响应类型
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=download.zip");
        if (paths.size() == 1) {
            Path path = Path.of(paths.get(0));
            ZipUtil.zipToOutputStream(List.of(path.toFile()), response.getOutputStream());
        } else {
            List<File> files = paths.stream().map(p -> Path.of(p).toFile()).collect(Collectors.toList());
            ZipUtil.zipToOutputStream(files, response.getOutputStream());
        }
    }


    /**
     * 定时清理临时文件夹内容
     */
    @Scheduled(fixedDelayString = "PT24H")
    public void cleanup() {
        Path locksDir = this.tusUploadDirectory.resolve("locks");
        if (Files.exists(locksDir)) {
            try {
                this.tusFileUploadService.cleanup();
            } catch (IOException e) {
                log.error("error during cleanup", e);
            }
        }
    }

}
