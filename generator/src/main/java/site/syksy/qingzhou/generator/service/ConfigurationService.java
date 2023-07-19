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

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import site.syksy.qingzhou.common.event.RemoveUserEvent;
import site.syksy.qingzhou.generator.domain.ConfigurationDO;
import site.syksy.qingzhou.generator.mapper.ConfigurationMapper;

/**
 * @author Raspberry
 */
@Service
public class ConfigurationService extends ServiceImpl<ConfigurationMapper, ConfigurationDO> implements ApplicationListener<RemoveUserEvent> {

    public QueryWrapper<ConfigurationDO> buildConditionByUserId(String userId){
        QueryWrapper<ConfigurationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ConfigurationDO::getUserId,userId);
        return queryWrapper;
    }

    public ConfigurationDO selectByUserId(String userId){
        ConfigurationDO configurationDO = baseMapper.selectOne(buildConditionByUserId(userId));
        return configurationDO;
    }

    @Override
    public <E extends IPage<ConfigurationDO>> E page(E page, Wrapper<ConfigurationDO> queryWrapper) {
        return super.page(page,queryWrapper);
    }

    @Override
    public void onApplicationEvent(RemoveUserEvent removeUserEvent) {
        QueryWrapper<ConfigurationDO> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.lambda().eq(ConfigurationDO::getUserId,removeUserEvent.getSource());
        remove(QueryWrapper);
    }
}
