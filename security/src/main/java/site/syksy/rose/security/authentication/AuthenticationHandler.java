package site.syksy.rose.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Raspberry
 * 身份验证返回结果处理
 */
public class AuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("UTF-8");
        response.getWriter().print("{\"status\":\"error\",\"type\":\"account\",\"message\":\"" + exception.getMessage() + "\"}");
        response.getWriter().flush();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        HttpSession session = request.getSession();
        Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
        Iterator<Map.Entry<String, Object>> entries = principal.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            session.setAttribute(entry.getKey(), entry.getValue());
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("UTF-8");
        response.getWriter().print("{\"status\":\"ok\",\"type\":\"account\"}");
        response.getWriter().flush();
    }
}