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
package site.syksy.rose.generator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.syksy.rose.generator.domain.TemplateDO;
import site.syksy.rose.generator.service.TemplateService;

import java.util.List;

/**
 * @author Raspberry
 */
@RestController
@Validated
@Tag(name = "代码模板")
@RequestMapping("generator/template")
public class TemplateController {

    @Autowired
    TemplateService templateService;

    @Operation(summary = "获取代码模板")
    @GetMapping
    public List<TemplateDO> getCodeTemplateAll() {
        List<TemplateDO> templateDOList = templateService.selectCodeTemplate();
        return templateDOList;
    }

    @Operation(summary = "根据类型获取代码模板")
    @GetMapping("{genre}")
    public TemplateDO getCodeTemplate(@PathVariable String genre) {
        TemplateDO templateDO = templateService.selectCodeTemplate(genre);
        return templateDO;
    }

    @Operation(summary = "修改代码模板")
    @PutMapping("{genre}")
    public void putCodeTemplate(@PathVariable String genre, @RequestBody String content) {
        TemplateDO templateDO = templateService.selectCodeTemplate(genre);
        if (ObjectUtils.isEmpty(templateDO)) {
            templateDO = new TemplateDO();
            templateDO.setGenre(genre);
            templateDO.setContent(content);
            templateService.save(templateDO);
        } else {
            templateDO.setContent(content);
            templateService.updateById(templateDO);
        }
    }

}
