package site.syksy.qingzhou.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author Raspberry
 * 验证码配置
 */
@ConfigurationProperties(prefix = "qz.security.captcha")
public class CaptchaProperties {

    /**
     * 是否开启验证码
     */
    private Boolean enable = true;

    /**
     * 有效时长，ISO-8601持续时间格式，默认是30秒，即PT30S
     */
    private Duration effectiveTime = Duration.ofSeconds(30);

    /**
     * 验证码字符集，默认为：0123456789
     */
    private String character = "0123456789";

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Duration getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Duration effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
