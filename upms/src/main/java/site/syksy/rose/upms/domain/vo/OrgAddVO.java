package site.syksy.rose.upms.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Raspberry
 */
public class OrgAddVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 50)
    @Schema(title = "名称")
    private String name;

    @NotBlank
    @Schema(title = "父ID")
    private String parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
