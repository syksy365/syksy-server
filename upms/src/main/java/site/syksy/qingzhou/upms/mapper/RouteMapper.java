package site.syksy.qingzhou.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import site.syksy.qingzhou.upms.domain.RouteDO;

import java.util.List;

/**
 * @author Raspberry
 * @since 2020-08-26
 */
public interface RouteMapper extends BaseMapper<RouteDO> {

    List<RouteDO> selectRouteByUserId(String userId);
}
