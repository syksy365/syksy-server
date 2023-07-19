package site.syksy.qingzhou.setting.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import site.syksy.qingzhou.setting.constant.GenreConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Raspberry
 * @since 2021-01-04
 */
@TableName("s_setting_dic")
public class DicDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @NotBlank
    private String name;

    /**
     * 类型（list或tree）
     */
    @NotNull
    private GenreConstant genre;

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

    public GenreConstant getGenre() {
        return genre;
    }

    public void GenreConstant(GenreConstant genre) {
        this.genre = genre;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
