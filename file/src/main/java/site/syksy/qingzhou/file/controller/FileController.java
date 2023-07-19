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
package site.syksy.qingzhou.file.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import site.syksy.qingzhou.common.properties.FileDirectoryProperties;
import site.syksy.qingzhou.common.properties.QingZhouProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Raspberry
 */
@RestController
@Tag(name = "文件上传下载")
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
                Path output = this.uploadDirectory.resolve(uploadInfo.getFileName());
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
