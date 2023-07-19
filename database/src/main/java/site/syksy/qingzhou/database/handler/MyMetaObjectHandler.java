package site.syksy.qingzhou.database.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import site.syksy.qingzhou.common.AppContext;

import java.time.LocalDateTime;

/**
 * @author Raspberry
 * 根据字段注解，自动填充创建时间、更新时间、更新账户数据
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "createUserId", Long.class, AppContext.getContext().getUserId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateUserId", Long.class, AppContext.getContext().getUserId());
    }
}
