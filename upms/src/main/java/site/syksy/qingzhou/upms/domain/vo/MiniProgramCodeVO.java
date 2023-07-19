package site.syksy.qingzhou.upms.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

/**
 * @author Raspberry
 */
@Schema(title = "微信小程序临时登录凭证code")
public class MiniProgramCodeVO {

    @Schema(title = "临时登录凭证code")
    @NotBlank
    private String code;

    @Schema(title = "用户名")
    @NotBlank
    private String username;

    @Schema(title = "密码")
    @NotBlank
    private String password;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
