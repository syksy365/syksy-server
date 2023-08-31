package site.syksy.rose.upms.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Raspberry
 */
@Schema(title = "修改密码")
public class PasswordVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty
    @Schema(title = "原密码")
    @Size(max = 100)
    private String oldPassword;

    @NotEmpty
    @Schema(title = "新密码")
    @Size(max = 100)
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
