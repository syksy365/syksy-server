package site.syksy.rose.upms.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Raspberry
 */
@Schema(title = "用户信息")
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @NotBlank
    @Size(max = 45)
    @Schema(title = "用户名", example = "admin")
    private String username;

    @Schema(title = "名字", example = "管理员")
    private String name;

    @Schema(title = "电子邮箱", example = "admin@syksy.online")
    private String email;

    @Schema(title = "手机号", example = "13088888888")
    private String phone;

    @Schema(title = "组织ID")
    private String orgId;

    @Schema(title = "组织ID,所有级")
    private List<String> orgIds;

    @Schema(title = "组织ID和名字，所有级")
    private Map<String,String> orgs;

    @Schema(title = "角色ID")
    private List<String> roleIds;

    @Schema(title = "角色")
    private Map<String,String> roles;

    @Schema(title = "头像")
    private String avatar;

    @Schema(title = "启停")
    private Boolean onOff;

    @Schema(title = "备注")
    private String remark;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

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

    public List<String> getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(List<String> orgIds) {
        this.orgIds = orgIds;
    }

    public Map<String, String> getOrgs() {
        return orgs;
    }

    public void setOrgs(Map<String, String> orgs) {
        this.orgs = orgs;
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

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public Map<String, String> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, String> roles) {
        this.roles = roles;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
