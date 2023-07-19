package site.syksy.qingzhou.upms.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import site.syksy.qingzhou.upms.constant.MenuType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Raspberry
 */
@Schema(title = "菜单")
public class MenuVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @Schema(title = "菜单名", example = "系统管理")
    private String name;

    @Schema(title = "菜单类型", example = MenuType.DIRECTORY)
    private String genre;

    @Schema(title = "前端路由路径")
    private String path;

    @Schema(title = "备注")
    private String remark;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "更新时间")
    private LocalDateTime updateTime;

    @Schema(title = "角色ID")
    private List<String> roleIds;

    @Schema(title = "角色翻译")
    private Map<String,String> roles;

    @Schema(title = "层级ID")
    private List<String> levelIds;

    public List<String> getLevelIds() {
        return levelIds;
    }

    public void setLevelIds(List<String> levelIds) {
        this.levelIds = levelIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, String> roles) {
        this.roles = roles;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
