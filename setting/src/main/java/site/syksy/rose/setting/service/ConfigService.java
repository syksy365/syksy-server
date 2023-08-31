package site.syksy.rose.setting.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.syksy.rose.setting.domain.ConfigDO;
import site.syksy.rose.setting.mapper.ConfigMapper;

/**
 * @author Raspberry
 * @since 2020-08-22
 */
@Service
public class ConfigService extends ServiceImpl<ConfigMapper, ConfigDO> {

    @Autowired
    ObjectMapper mapper;

    /*
    {
        "id": "verification_code",
            "title": "验证码",
            "description": "是否开启登录验证码，开启后设置密码错误次数触发验证码开启（不填写或小于1则直接开启验证码）。",
            "uiSchema": "",
            "configuration": "{open:true,passwordIncorrectNumber:2}"
    }

    {
        "id": "concurrent_login",
            "title": "并发登录",
            "description": "是否开启并发登录，开启后设置并发数量（不填写或小于1则不限制），是否允许挤下线。",
            "uiSchema": "",
            "configuration": "{open:true,loginNumber:2,loginSeize:true}"
    }
    */

    public <T> T getConfigurationById(String id,Class<T> t) {
        ConfigDO configDO = getById(id);
        String configuration = configDO.getConfiguration();
        try {
            return mapper.readValue(configuration,t);
        }catch (Exception e){
            new Exception(e.getMessage());
        }
        return null;
    }
}
