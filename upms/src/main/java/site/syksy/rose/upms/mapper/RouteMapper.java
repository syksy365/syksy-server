package site.syksy.rose.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import site.syksy.rose.upms.domain.RouteDO;

import java.util.List;

/**
 * @author Raspberry
 * @since 2020-08-26
 */
public interface RouteMapper extends BaseMapper<RouteDO> {

    List<RouteDO> selectRouteByUserId(String userId);
}
