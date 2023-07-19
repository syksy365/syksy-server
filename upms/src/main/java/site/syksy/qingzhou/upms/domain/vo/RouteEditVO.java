package site.syksy.qingzhou.upms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Raspberry
 */
public class RouteEditVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(title = "校验类型", example = "1")
    private Integer checkType;

    @NotNull
    @Schema(title = "是否开启", example = "true")
    private Boolean onOff;

    @Schema(title = "有效期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private List<LocalDateTime> validPeriod;

    @Size(max = 140)
    @Schema(title = "备注")
    private String remark;

    public Integer getCheckType() {
        return checkType;
    }

    public void setCheckType(Integer checkType) {
        this.checkType = checkType;
    }

    public Boolean getOnOff() {
        return onOff;
    }

    public void setOnOff(Boolean onOff) {
        this.onOff = onOff;
    }

    public List<LocalDateTime> getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(List<LocalDateTime> validPeriod) {
        this.validPeriod = validPeriod;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
