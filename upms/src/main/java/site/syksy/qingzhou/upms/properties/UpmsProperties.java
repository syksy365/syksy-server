package site.syksy.qingzhou.upms.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Raspberry
 * upms配置
 */
@Component
@ConfigurationProperties(prefix = "qz")
public class UpmsProperties {

    /**
     * 安全配置
     */
    private final Security security = new Security();

    /**
     * 微信小程序配置
     */
    private final WeChat weChat = new WeChat();

    public WeChat getWeChat() {
        return weChat;
    }

    public static class WeChat {
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

    public Security getSecurity() {
        return security;
    }

    public static class Security {
        /**
         * 全局免登录URL(ANT规则匹配)
         */
        private Set<String> webIgnoring;

        /**
         * API初始化
         */
        private Boolean apiInit = true;

        public Set<String> getWebIgnoring() {
            return webIgnoring;
        }

        public void setWebIgnoring(Set<String> webIgnoring) {
            this.webIgnoring = webIgnoring;
        }

        public Boolean getApiInit() {
            return apiInit;
        }

        public void setApiInit(Boolean apiInit) {
            this.apiInit = apiInit;
        }
    }

}
