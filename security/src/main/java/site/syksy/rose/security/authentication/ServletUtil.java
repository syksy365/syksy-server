package site.syksy.rose.security.authentication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.AuthenticationServiceException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Raspberry
 */
public class ServletUtil {

    /**
     * 获取请求中的json数据
     * @param request
     * @param objectMapper
     * @return
     * @throws IOException
     */
    public static JsonNode getRequestJson(HttpServletRequest request, ObjectMapper objectMapper) throws IOException {
        ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(request);
        MediaType contentType = servletServerHttpRequest.getHeaders().getContentType();
        if (!contentType.includes(MediaType.APPLICATION_JSON)) {
            throw new AuthenticationServiceException("请求内容不支持" + request.getContentType() + "内容类型，请使用" + MediaType.APPLICATION_JSON + "内容类型。");
        }
        JsonNode jsonNode = objectMapper.readTree(servletServerHttpRequest.getBody());
        return jsonNode;
    }

    /**
     * 移除多余斜杆
     * @param url
     * @return
     */
    public static String removeRedundantSlash(String url){
        return url.replaceAll("(?<!(http:|https:))/+", "/");
    }
}
