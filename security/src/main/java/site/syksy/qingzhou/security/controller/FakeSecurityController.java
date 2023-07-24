package site.syksy.qingzhou.security.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import site.syksy.qingzhou.security.constant.GeneralConstant;
import site.syksy.qingzhou.security.properties.CaptchaProperties;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Raspberry
 */
@Tag(name = "通用登录")
@RestController
@RequestMapping(value = "/")
public class FakeSecurityController {

    @Resource
    DefaultKaptcha captchaProducer;

    @Resource
    CaptchaProperties captchaProperties;


    @Operation(summary = "登录", description = "伪造的登录接口，请求由security登录过滤器拦截处理")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(ref = "#/components/schemas/UserSchema")))
    @PostMapping(value = "/login/account")
    public void login(@RequestBody Map<String, String> map) {
    }

    @Operation(summary = "获取验证码")
    @GetMapping("captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String capText = captchaProducer.createText();
        LocalDateTime localDateTime = LocalDateTime.now();

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(GeneralConstant.CAPTCHA, capText);
        httpSession.setAttribute(GeneralConstant.CAPTCHA_CREATE_TIME, localDateTime);

        BufferedImage bi = captchaProducer.createImage(capText);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline().toString());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        ImageIO.write(bi, "png", response.getOutputStream());
    }

    @Operation(summary = "登录验证码开启状态和有效时长查询")
    @GetMapping("captcha/status")
    public Map<String, Object> captchaStatus() {
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("enable", captchaProperties.getEnable());
        map.put("effectiveTime", captchaProperties.getEffectiveTime().getSeconds());
        return map;
    }
}
