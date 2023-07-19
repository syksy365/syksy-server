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
package site.syksy.qingzhou.generator.service;

import com.baomidou.mybatisplus.annotation.TableName;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import site.syksy.qingzhou.database.domain.vo.TablesVO;
import site.syksy.qingzhou.database.service.TablesService;
import site.syksy.qingzhou.generator.constant.Generator;
import site.syksy.qingzhou.generator.domain.ConfigurationDO;
import site.syksy.qingzhou.generator.domain.TemplateDO;
import site.syksy.qingzhou.generator.domain.dto.TableInfoDTO;
import site.syksy.qingzhou.generator.domain.vo.ClassNameVO;
import site.syksy.qingzhou.generator.domain.vo.CodeVO;
import site.syksy.qingzhou.web.exception.ForbiddenException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author Raspberry
 */
@Service
public class GeneratorService {

    @Autowired
    TablesService tablesService;

    @Autowired
    TemplateService templateService;

    @Autowired
    DomainService domainService;

    /**
     * 代码生成
     *
     * @param classNameVO
     * @param configurationDO
     * @return
     * @throws Exception
     */
    public List<CodeVO> codeGenerator(ClassNameVO classNameVO, ConfigurationDO configurationDO) throws Exception {

        List<CodeVO> codeVOList = new ArrayList<>();
        Configuration configuration = loadTemplate();

        String outputDir = getOutputDir(configurationDO.getModuleName(), configurationDO.convertPackagePath());
        Map<String, Object> objectMap = createMap(classNameVO, configurationDO);

        if (configurationDO.getTheFileType(Generator.Genre.CONTROLLER)) {
            String filePath = outputDir + Generator.Genre.CONTROLLER + File.separator + classNameVO.getControllerName() + ".java";
            CodeVO codeVO = runTemplate(Generator.Genre.CONTROLLER, configuration, objectMap, filePath, classNameVO.getTableName());
            codeVOList.add(codeVO);
        }
        if (configurationDO.getTheFileType(Generator.Genre.SERVICE)) {
            String filePath = outputDir + Generator.Genre.SERVICE + File.separator + classNameVO.getServiceName() + ".java";
            CodeVO codeVO = runTemplate(Generator.Genre.SERVICE, configuration, objectMap, filePath, classNameVO.getTableName());
            codeVOList.add(codeVO);
        }
        if (configurationDO.getTheFileType(Generator.Genre.DOMAIN)) {
            String filePath = outputDir + Generator.Genre.DOMAIN + File.separator + classNameVO.getDomainName() + ".java";
            CodeVO codeVO = runTemplate(Generator.Genre.DOMAIN, configuration, objectMap, filePath, classNameVO.getTableName());
            codeVOList.add(codeVO);
        }
        if (configurationDO.getTheFileType(Generator.Genre.MAPPER)) {
            String filePath = outputDir + Generator.Genre.MAPPER + File.separator + classNameVO.getMapperName() + ".java";
            CodeVO codeVO = runTemplate(Generator.Genre.MAPPER, configuration, objectMap, filePath, classNameVO.getTableName());
            codeVOList.add(codeVO);
        }
        if (configurationDO.getTheFileType(Generator.Genre.MAPPER_XML)) {
            String filePath = getMapperXmlPath(configurationDO.getModuleName(), configurationDO.getGroupPackage()) + File.separator + classNameVO.getMapperName() + ".xml";
            CodeVO codeVO = runTemplate(Generator.Genre.MAPPER_XML, configuration, objectMap, filePath, classNameVO.getTableName());
            codeVOList.add(codeVO);
        }

        return codeVOList;
    }

    private String getMapperXmlPath(String module, String groupPackage) {
        String outputDir = System.getProperty("user.dir");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(module)) {
            outputDir += File.separator + module;
        }
        outputDir += File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "mapper";
        if (!StringUtils.isEmpty(groupPackage)) {
            outputDir += File.separator + groupPackage;
        }
        return outputDir;
    }

    private String getOutputDir(String module, String packagePath) {
        String outputDir = System.getProperty("user.dir");
        if (!StringUtils.isEmpty(module)) {
            outputDir += File.separator + module;
        }
        outputDir += File.separator + "src" + File.separator + "main" + File.separator + "java";
        outputDir += File.separator + packagePath.replaceAll("\\.", Matcher.quoteReplacement(File.separator)) + File.separator;
        return outputDir;
    }

    /**
     * 运行模板输出代码
     *
     * @param genre
     * @param configuration
     * @param objectMap
     * @param filePath
     * @param tableName
     * @return
     * @throws Exception
     */
    private CodeVO runTemplate(String genre, Configuration configuration, Map<String, Object> objectMap, String filePath, String tableName) throws Exception {
        Template template = configuration.getTemplate(genre);
        String code;
        CodeVO codeVO = new CodeVO();
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            template.process(objectMap, new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8.name()));
            code = byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        codeVO.setTableName(tableName);
        codeVO.setGenre(genre);
        codeVO.setContent(code);
        codeVO.setFilePath(filePath);
        return codeVO;
    }

    /**
     * 加载数据库中所有的模板
     *
     * @return
     */
    private Configuration loadTemplate() {
        List<TemplateDO> templateDOList = templateService.list();
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        for (TemplateDO templateDO : templateDOList) {
            stringLoader.putTemplate(templateDO.getGenre(), templateDO.getContent());
            configuration.setTemplateLoader(stringLoader);
        }
        return configuration;
    }


    /**
     * 创建模板数据集合
     *
     * @param classNameVO
     * @param configurationDO
     * @return
     */
    private Map<String, Object> createMap(ClassNameVO classNameVO, ConfigurationDO configurationDO) {
        TablesVO tablesVO = tablesService.selectTable(classNameVO.getTableName());
        TableInfoDTO tableInfoDTO = domainService.convertTableFields(tablesVO, configurationDO);
        tableInfoDTO.setImportPackages(TableName.class.getCanonicalName());
        tableInfoDTO.setImportPackages(Serializable.class.getCanonicalName());

        Map<String, Object> objectMap = new HashMap<>(10);
        objectMap.put(Generator.TemplateMap.PACKAGE_PATH, configurationDO.convertPackagePath());
        objectMap.put(Generator.TemplateMap.AUTHOR, configurationDO.getAuthor());
        objectMap.put(Generator.TemplateMap.SINCE, LocalDate.now().toString());
        objectMap.put(Generator.TemplateMap.ID_TYPE, configurationDO.getIdType() == null ? null : configurationDO.getIdType());
        objectMap.put(Generator.TemplateMap.TABLE, tableInfoDTO);
        objectMap.put(Generator.TemplateMap.TABLE_NAME, classNameVO.getTableName());
        objectMap.put(Generator.TemplateMap.CONTROLLER_NAME, classNameVO.getControllerName());
        objectMap.put(Generator.TemplateMap.SERVICE_NAME, classNameVO.getServiceName());
        objectMap.put(Generator.TemplateMap.DOMAIN_NAME, classNameVO.getDomainName());
        objectMap.put(Generator.TemplateMap.MAPPER_NAME, classNameVO.getMapperName());
        return objectMap;
    }

    /**
     * 写入文件
     *
     * @param codeVOList
     * @throws IOException
     */
    public void codeToFile(List<CodeVO> codeVOList) throws IOException {
        for (CodeVO codeVO : codeVOList) {
            FileWriter writer = null;
            try {
                File file = new File(codeVO.getFilePath());
                if (file.exists()) {
                    throw new ForbiddenException("代码文件已存在，如要再次生成，请先将文件删除。路径：" + codeVO.getFilePath());
                }
                File fileParent = file.getParentFile();
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                file.createNewFile();
                writer = new FileWriter(file);
                writer.write(codeVO.getContent());
                writer.flush();
            } catch (IOException ioException) {
                throw new IOException();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        throw new IOException();
                    }
                }
            }
        }
    }
}
