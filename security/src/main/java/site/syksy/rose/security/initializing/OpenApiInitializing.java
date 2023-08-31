package site.syksy.rose.security.initializing;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import site.syksy.rose.security.properties.CaptchaProperties;


/**
 * @author Raspberry
 * OpenAPI bean加载后增加伪登录接口参数
 */
@Lazy(value = false)
@Component
public class OpenApiInitializing implements InitializingBean {

    @Autowired
    OpenAPI openAPI;

    @Autowired
    CaptchaProperties captchaProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        /*Schema userSchema = new Schema<Map<String, Object>>()
                .addProperty("username", new StringSchema().example("admin"))
                .addProperty("password", new StringSchema().example("1111"));

        if (captchaProperties.getEnable()) {
            userSchema.addProperty("captcha", new StringSchema().example("8888"));
        }
        openAPI.components(new Components().addSchemas("UserSchema", userSchema));*/

    }
}
