package site.syksy.rose.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import site.syksy.rose.upms.domain.MenuDO;

/**
 * @author Raspberry
 * @since 2020-08-29
 */
@CacheNamespace
public interface MenuMapper extends BaseMapper<MenuDO> {

}
