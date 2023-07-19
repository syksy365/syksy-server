package site.syksy.qingzhou.web.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raspberry
 * 微信小程序服务接口数据格式解析类型："text/plain" 格式的 Content-Type
 */
public class WeChatMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
    public WeChatMappingJackson2HttpMessageConverter() {
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_PLAIN);
        setSupportedMediaTypes(mediaTypes);
    }
}
