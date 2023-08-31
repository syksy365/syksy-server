package site.syksy.rose.upms.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * @author Raspberry
 */
public class OrgVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @Schema(title = "名称")
    private String name;

    @Schema(title = "父ID")
    private String parentId;

    @Schema(title = "单链-下一个节点ID")
    private String nextId;

    @Schema(title = "是否展开")
    private Boolean expand;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public Boolean getExpand() {
        return expand;
    }

    public void setExpand(Boolean expand) {
        this.expand = expand;
    }
}
