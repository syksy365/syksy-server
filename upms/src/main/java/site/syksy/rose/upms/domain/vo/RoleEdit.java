package site.syksy.rose.upms.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author Raspberry
 */
@Schema(title = "角色")
public class RoleEdit implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 20)
    @Schema(title = "名称", example = "管理员")
    private String name;

    @Size(max = 140)
    @Schema(title = "备注", example = "备注")
    private String remark;

    @Schema(title = "路由")
    private List<String> routeIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getRouteIds() {
        return routeIds;
    }

    public void setRouteIds(List<String> routeIds) {
        this.routeIds = routeIds;
    }
}
