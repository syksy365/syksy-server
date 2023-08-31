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
import org.springframework.web.bind.annotation.*;
import site.syksy.rose.generator.domain.ConfigurationDO;
import site.syksy.rose.generator.domain.vo.ClassNameVO;
import site.syksy.rose.generator.domain.vo.CodeVO;
import site.syksy.rose.generator.service.ConfigurationService;
import site.syksy.rose.generator.service.GeneratorService;
import site.syksy.rose.web.resolver.UserId;

import java.util.List;

/**
 * @author Raspberry
 */
@RestController
@Tag(name = "代码生成")
@RequestMapping("generator/code")
public class GeneratorController {

    @Autowired
    GeneratorService generatorService;

    @Autowired
    ConfigurationService configurationService;

    @Operation(summary = "代码生成预览")
    @PostMapping("preview")
    public List<CodeVO> postPreview(@RequestBody ClassNameVO classNameVO, @UserId String userId) throws Exception {
        ConfigurationDO configurationDO = configurationService.getById(userId);
        List<CodeVO> codeVOList = generatorService.codeGenerator(classNameVO, configurationDO);
        return codeVOList;
    }

    @Operation(summary = "代码直接生成")
    @PostMapping
    public void postCode(@RequestBody ClassNameVO classNameVO, @UserId String userId) throws Exception {
        ConfigurationDO configurationDO = configurationService.getById(userId);
        List<CodeVO> codeVOList = generatorService.codeGenerator(classNameVO, configurationDO);
        generatorService.codeToFile(codeVOList);
    }

    @Operation(summary = "代码预览编辑后保存")
    @PutMapping
    public void putCode(@RequestBody List<CodeVO> codeVOList) throws Exception {
        generatorService.codeToFile(codeVOList);
    }

    @Operation(summary = "代码生成记录")
    @GetMapping("record")
    public void getCodeGeneratorRecord(@PathVariable String genre) {

    }

    @Operation(summary = "代码生成撤回")
    @DeleteMapping("record")
    public void deleteCodeGeneratorRecord(@PathVariable String genre) {
    }

}
