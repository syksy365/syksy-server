package site.syksy.qingzhou.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Raspberry
 * 微信配置
 */
@ConfigurationProperties(prefix = "qz.we-chat")
public class WeChatProperties {

    /**
     * 小程序ID
     */
    private String appId;
    /**
     * 小程序密钥
     */
    private String appSecret;

    /**
     * 登录凭证校验URL
     */
    private String code2sessionUrl = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 小程序登录URL
     */
    private String loginUrl = "we-chat/login/account";

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getCode2sessionUrl() {
        return code2sessionUrl;
    }

    public void setCode2sessionUrl(String code2sessionUrl) {
        this.code2sessionUrl = code2sessionUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
