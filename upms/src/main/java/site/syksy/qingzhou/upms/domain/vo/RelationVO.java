package site.syksy.qingzhou.upms.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * @author Raspberry
 */
public class RelationVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String cid;
    private String type;
    private List<Integer> size;
    private String label;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<RelationVO> children;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getSize() {
        return size;
    }

    public void setSize(List<Integer> size) {
        this.size = size;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<RelationVO> getChildren() {
        return children;
    }

    public void setChildren(List<RelationVO> children) {
        this.children = children;
    }
}
