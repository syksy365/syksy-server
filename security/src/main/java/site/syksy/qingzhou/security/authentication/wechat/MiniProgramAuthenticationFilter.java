package site.syksy.qingzhou.security.authentication.wechat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import site.syksy.qingzhou.security.authentication.AuthenticationToken;
import site.syksy.qingzhou.security.authentication.ServletUtil;
import site.syksy.qingzhou.security.constant.MiniProgramConstant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Raspberry
 * 微信小程序身份认证过滤器
 */
public class MiniProgramAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper;

    public MiniProgramAuthenticationFilter(String loginUrl, ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(loginUrl, MiniProgramConstant.HTTP_METHOD_POST));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        if (!request.getMethod().equals(MiniProgramConstant.HTTP_METHOD_POST)) {
            throw new AuthenticationServiceException("请求不支持" + request.getMethod() + "方法，请使用" + MiniProgramConstant.HTTP_METHOD_POST + "方法。");
        }
        JsonNode jsonNode = ServletUtil.getRequestJson(request, objectMapper);
        Map<String, Object> principal = new HashMap<>(1);

        Map<String, Object> credentials = new HashMap<>(1);
        credentials.put(MiniProgramConstant.CODE, jsonNode.get(MiniProgramConstant.CODE).textValue());

        AuthenticationToken authenticationToken = new AuthenticationToken(principal, credentials);
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
