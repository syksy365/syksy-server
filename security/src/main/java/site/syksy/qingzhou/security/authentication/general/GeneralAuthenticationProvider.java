package site.syksy.qingzhou.security.authentication.general;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import site.syksy.qingzhou.security.authentication.AuthenticationToken;
import site.syksy.qingzhou.security.constant.GeneralConstant;
import site.syksy.qingzhou.security.constant.SecurityConstant;
import site.syksy.qingzhou.security.properties.CaptchaProperties;
import site.syksy.qingzhou.upms.service.UserService;

import java.util.Map;

/**
 * @author Raspberry
 * 通用身份验证供应器
 */
public class GeneralAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    private CaptchaProperties captchaProperties;

    public GeneralAuthenticationProvider(UserService userService,CaptchaProperties captchaProperties, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.captchaProperties = captchaProperties;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
        Map<String, Object> credentials = (Map<String, Object>) authentication.getCredentials();
        Map<String, String> userMap = userService.getUserMapByUsername(principal.get(SecurityConstant.USERNAME).toString());

        if (captchaProperties.getEnable()) {
            String captcha = principal.get(GeneralConstant.CAPTCHA).toString();
            String sessionCaptcha = credentials.get(GeneralConstant.SESSION_CAPTCHA).toString();
            if (!captcha.equals(sessionCaptcha)) {
                throw new AuthenticationServiceException("验证码错误。");
            }
        }

        String encodedPassword = userMap.get(GeneralConstant.PASSWORD);
        if (StringUtils.isBlank(encodedPassword)) {
            throw new AuthenticationServiceException("账户不存在。");
        }

        if (!passwordEncoder.matches(credentials.get(GeneralConstant.PASSWORD).toString(), encodedPassword)) {
            throw new AuthenticationServiceException("密码错误。");
        }

        principal.put(GeneralConstant.USER_ID, userMap.get(GeneralConstant.USER_ID));
        return new AuthenticationToken(null, principal, credentials);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if (authentication.equals(AuthenticationToken.class)) {
            return true;
        } else {
            return false;
        }
    }
}
