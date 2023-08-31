package site.syksy.rose.security.authentication.general;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import site.syksy.rose.security.authentication.AuthenticationToken;
import site.syksy.rose.security.authentication.ServletUtil;
import site.syksy.rose.security.constant.GeneralConstant;
import site.syksy.rose.security.constant.SecurityConstant;
import site.syksy.rose.security.properties.CaptchaProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Raspberry
 * 通用身份验证过滤器
 */
public class GeneralAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper;

    private CaptchaProperties captchaProperties;

    public GeneralAuthenticationFilter(String loginUrl, CaptchaProperties captchaProperties, ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(loginUrl, SecurityConstant.HTTP_METHOD_POST));
        this.objectMapper = objectMapper;
        this.captchaProperties = captchaProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException {
        if (!request.getMethod().equals(GeneralConstant.HTTP_METHOD_POST)) {
            throw new AuthenticationServiceException("请求不支持" + request.getMethod() + "方法，请使用" + GeneralConstant.HTTP_METHOD_POST + "方法。");
        }

        JsonNode jsonNode = ServletUtil.getRequestJson(request, objectMapper);
        Map<String, Object> principal = new HashMap<>(2);
        Map<String, Object> credentials = new HashMap<>(2);

        if (captchaProperties.getEnable()) {
            HttpSession httpSession = request.getSession();
            String sessionCaptcha = (String) httpSession.getAttribute(GeneralConstant.CAPTCHA);
            LocalDateTime captchaCreateTime = (LocalDateTime) httpSession.getAttribute(GeneralConstant.CAPTCHA_CREATE_TIME);
            if (StringUtils.isBlank(sessionCaptcha) || captchaCreateTime == null) {
                throw new AuthenticationServiceException("未获取验证码。");
            }
            Duration duration = Duration.between(captchaCreateTime, LocalDateTime.now());
            if (duration.compareTo(captchaProperties.getEffectiveTime()) > 0) {
                throw new AuthenticationServiceException("验证码已过期，请重新获取。");
            }
            credentials.put(GeneralConstant.SESSION_CAPTCHA, sessionCaptcha);
        }

        if (captchaProperties.getEnable()) {
            if (jsonNode.get(GeneralConstant.CAPTCHA) == null) {
                throw new AuthenticationServiceException("未填写验证码。");
            }
            principal.put(GeneralConstant.CAPTCHA, jsonNode.get(GeneralConstant.CAPTCHA).textValue());
        }
        if (jsonNode.get(GeneralConstant.USERNAME) == null) {
            throw new AuthenticationServiceException("未填写账号。");
        }
        principal.put(GeneralConstant.USERNAME, jsonNode.get(GeneralConstant.USERNAME).textValue());
        if (jsonNode.get(GeneralConstant.PASSWORD) == null) {
            throw new AuthenticationServiceException("未填写密码。");
        }
        credentials.put(GeneralConstant.PASSWORD, jsonNode.get(GeneralConstant.PASSWORD).textValue());

        AuthenticationToken authenticationToken = new AuthenticationToken(principal, credentials);
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
