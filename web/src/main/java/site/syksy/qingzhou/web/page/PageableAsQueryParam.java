package site.syksy.qingzhou.web.page;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Raspberry
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(in = ParameterIn.QUERY
        , description = "页数，从1开始 (1..N)"
        , name = "current"
        , schema = @Schema(type = "integer", defaultValue = "1"))
@Parameter(in = ParameterIn.QUERY
        , description = "每页数量大小"
        , name = "pageSize"
        , schema = @Schema(type = "integer", defaultValue = "20"))
@Parameter(in = ParameterIn.QUERY
        , description = "排序，json对象，如{'createTime':'ascend'}"
        , name = "sorter"
        , schema = @Schema(type = "string"))
@Parameter(in = ParameterIn.QUERY
        , description = "过滤条件，json对象，如{'status':['0','1'],...}"
        , name = "filter"
        , schema = @Schema(type = "string"))
public @interface PageableAsQueryParam {
}
