package site.syksy.rose.upms.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Raspberry
 * @since 2020-08-26
 */
@Schema(title = "路由表")
@TableName("s_upms_route")
public class RouteDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @Size(max = 50)
    @Schema(title = "路径模板")
    private String pattern;

    @Schema(title = "请求方法")
    private String requestMethod;

    @Schema(title = "处理方法")
    private String handlerMethod;

    @Schema(title = "处理方法hash值")
    private String handlerMethodHash;

    @Schema(title = "是否在线")
    private Boolean online;

    @Schema(title = "是否开启")
    private Boolean onOff;

    /**
     * 校验类型
     */
    private Integer checkType;

    @Schema(title = "开始时间")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime startDate;

    @Schema(title = "结束时间")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime endDate;

    @Size(max = 140)
    @Schema(title = "备注")
    private String remark;

    @TableField(exist = false)
    private List<LocalDateTime> validPeriod;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RouteDO routeDO = (RouteDO) o;
        return Objects.equals(pattern, routeDO.pattern) &&
                Objects.equals(requestMethod, routeDO.requestMethod) &&
                Objects.equals(handlerMethod, routeDO.handlerMethod);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerMethod(String handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public String getHandlerMethodHash() {
        return handlerMethodHash;
    }

    public void setHandlerMethodHash(String handlerMethodHash) {
        this.handlerMethodHash = handlerMethodHash;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Boolean getOnOff() {
        return onOff;
    }

    public void setOnOff(Boolean onOff) {
        this.onOff = onOff;
    }

    public Integer getCheckType() {
        return checkType;
    }

    public void setCheckType(Integer checkType) {
        this.checkType = checkType;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<LocalDateTime> getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(List<LocalDateTime> validPeriod) {
        this.validPeriod = validPeriod;
    }
}
