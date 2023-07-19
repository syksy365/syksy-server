package site.syksy.qingzhou.upms.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@Schema(title = "用户表")
@TableName("s_upms_user")
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @Schema(title = "用户名",example = "admin")
    private String username;

    @Schema(title = "微信小程序openid")
    private String openid;

    /**
     * 名字
     */
    private String name;

    @NotBlank
    @Schema(title = "登录密码",example = "12345678")
    private String password;

    @Schema(title = "电子邮箱",example = "admin@syksy.online")
    private String email;

    @Schema(title = "手机号",example = "13088888888")
    private String phone;

    @NotBlank
    @Schema(title = "组织ID")
    private String orgId;

    @Schema(title = "头像")
    private String avatar;

    /**
     * 启停
     */
    private Boolean onOff;

    @Schema(title = "备注")
    private String remark;

    @TableLogic
    @TableField("is_deleted")
    @Schema(title = "是否删除")
    private Boolean deleted;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @NotNull
    @Schema(title = "更新时间")
    private LocalDateTime updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getOnOff() {
        return onOff;
    }

    public void setOnOff(Boolean onOff) {
        this.onOff = onOff;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
