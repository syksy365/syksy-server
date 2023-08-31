package site.syksy.rose.setting.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Raspberry
 * @since 2021-01-04
 */
@TableName("s_setting_dic_item_tree")
public class DicItemTreeDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField
    private String nextId;

    @TableField
    private String parentId;

    @NotBlank
    @TableField
    private String dicId;

    @Schema(title = "层级ID")
    @TableField(exist = false)
    private List<String> levelIds;

    @Schema(title = "层级")
    @TableField(exist = false)
    private Map<String, String> level;

    @NotBlank
    @TableField
    private String name;

    @NotBlank
    @TableField
    private String code;

    @TableField
    private String customize;

    /**
     * 子级
     */
    @TableField(exist = false)
    private List<DicItemTreeDO> children;

    /**
     * 是否展开
     */
    @TableField("is_expand")
    private Boolean expand;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDicId() {
        return dicId;
    }

    public void setDicId(String dicId) {
        this.dicId = dicId;
    }

    public List<String> getLevelIds() {
        return levelIds;
    }

    public void setLevelIds(List<String> levelIds) {
        this.levelIds = levelIds;
    }

    public Map<String, String> getLevel() {
        return level;
    }

    public void setLevel(Map<String, String> level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCustomize() {
        return customize;
    }

    public void setCustomize(String customize) {
        this.customize = customize;
    }

    public List<DicItemTreeDO> getChildren() {
        return children;
    }

    public void setChildren(List<DicItemTreeDO> children) {
        this.children = children;
    }

    public Boolean getExpand() {
        return expand;
    }

    public void setExpand(Boolean expand) {
        this.expand = expand;
    }
}
