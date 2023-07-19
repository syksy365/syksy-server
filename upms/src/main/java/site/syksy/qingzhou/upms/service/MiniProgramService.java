package site.syksy.qingzhou.upms.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import site.syksy.qingzhou.upms.domain.dto.MiniProgramSessionDTO;
import site.syksy.qingzhou.upms.properties.UpmsProperties;
import site.syksy.qingzhou.web.converter.WeChatMappingJackson2HttpMessageConverter;
import site.syksy.qingzhou.web.exception.ForbiddenException;

import javax.annotation.Resource;
import java.net.URI;

/**
 * @author clear
 */
@Service
public class MiniProgramService {

    @Resource
    RestTemplate restTemplate;

    @Resource
    UpmsProperties upmsProperties;

    /**
     * 使用微信小程序临时登录凭证code，换取用户唯一标识 OpenID 、 用户在微信开放平台帐号下的唯一标识UnionID（若当前小程序已绑定到微信开放平台帐号） 和 会话密钥 session_key
     *
     * @param code
     * @return
     */
    public MiniProgramSessionDTO code2Session(String code) {
        UpmsProperties.WeChat weChat = upmsProperties.getWeChat();
        URI url = UriComponentsBuilder
                .fromHttpUrl(weChat.getCode2sessionUrl())
                .queryParam("appid", weChat.getAppId())
                .queryParam("secret", weChat.getAppSecret())
                .queryParam("js_code", code)
                .queryParam("grant_type", "authorization_code")
                .build()
                .encode().toUri();
        restTemplate.getMessageConverters().add(new WeChatMappingJackson2HttpMessageConverter());
        MiniProgramSessionDTO miniProgramSessionDTO = this.restTemplate.getForObject(url, MiniProgramSessionDTO.class);
        if (miniProgramSessionDTO.getErrcode() != null && !miniProgramSessionDTO.getErrcode().equals(0)) {
            throw new ForbiddenException(miniProgramSessionDTO.getErrcode() + ":" + miniProgramSessionDTO.getErrmsg());
        }
        return miniProgramSessionDTO;
    }
}
