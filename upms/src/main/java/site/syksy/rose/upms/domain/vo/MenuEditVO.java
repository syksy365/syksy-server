package site.syksy.rose.upms.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import site.syksy.rose.upms.constant.MenuType;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author Raspberry
 */
@Schema(title = "菜单修改回显")
public class MenuEditVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @NotBlank
    @Schema(title = "名字", example = "系统管理")
    private String name;

    @Schema(title = "菜单类型", example = MenuType.DIRECTORY)
    private String genre;

    @Schema(title = "前端路由路径")
    private String path;

    @Schema(title = "角色")
    private List<String> roleIds;

    @Schema(title = "备注")
    private String remark;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
