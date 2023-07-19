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
package site.syksy.qingzhou.generator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.syksy.qingzhou.common.utils.BeanCopyUtil;
import site.syksy.qingzhou.generator.domain.ConfigurationDO;
import site.syksy.qingzhou.generator.domain.vo.ConfigurationVO;
import site.syksy.qingzhou.generator.service.ConfigurationService;
import site.syksy.qingzhou.web.page.MyPage;
import site.syksy.qingzhou.web.page.Pageable;
import site.syksy.qingzhou.web.page.PageableAsQueryParam;
import site.syksy.qingzhou.web.resolver.UserId;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Raspberry
 */
@RestController
@Tag(name = "配置")
@RequestMapping("generator/configuration")
public class ConfigurationController {

    @Autowired
    ConfigurationService configurationService;

    @Operation(summary = "获取配置")
    @GetMapping
    public ConfigurationVO getCodeConfiguration(@Parameter(hidden = true) @UserId String userId) {
        ConfigurationDO configurationDO = configurationService.getById(userId);
        if (configurationDO != null) {
            ConfigurationVO configurationVO = new ConfigurationVO();
            BeanCopyUtil.copyProperties(configurationDO, configurationVO);
            String[] fileTypes = configurationDO.getFileType().split(",");
            configurationVO.setFileType(Arrays.asList(fileTypes));
            return configurationVO;
        } else {
            return null;
        }
    }

    @Operation(summary = "修改配置")
    @PutMapping
    public void putCodeConfiguration(@Validated @RequestBody ConfigurationVO configurationVO, @Parameter(hidden = true) @UserId String userId) {
        ConfigurationDO configurationDO = new ConfigurationDO();
        BeanCopyUtil.copyProperties(configurationVO, configurationDO);
        String fileType = configurationVO.getFileType().stream().collect(Collectors.joining(","));
        configurationDO.setFileType(fileType);
        configurationDO.setUserId(userId);
        ConfigurationDO oldConfigurationDO = configurationService.selectByUserId(userId);
        if (ObjectUtils.isEmpty(oldConfigurationDO)) {
            configurationService.save(configurationDO);
        } else {
            configurationService.update(configurationDO, configurationService.buildConditionByUserId(userId));
        }
    }

    @GetMapping("list")
    @Operation(summary = "分页查询")
    @PageableAsQueryParam
    public MyPage listToList(
            @Parameter(hidden = true) Pageable pageable,
            @RequestParam(required = false) String author) {
        MyPage configurationDOPage = new MyPage<>(pageable);
        QueryWrapper<ConfigurationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ConfigurationDO::getOpen, true)
                .like(StringUtils.hasLength(author), ConfigurationDO::getAuthor, author);
        for (Map.Entry<String, List<String>> entry : pageable.getFilter().entrySet()) {
            queryWrapper.in(entry.getKey(), entry.getValue());
        }
        queryWrapper.orderBy(pageable.getSorter().getCondition(), pageable.getSorter().getAsc(), pageable.getSorter().getValue());
        configurationDOPage = configurationService.page(configurationDOPage, queryWrapper);
        return configurationDOPage;
    }

}
