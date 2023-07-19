package site.syksy.qingzhou.common;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author Raspberry
 * 系统全局上下文，存储请求中携带的用户信息，在任意处获取
 */
public class AppContext {

    /**
     * 用户ID
     */
    private Long userId;

    private static final TransmittableThreadLocal<AppContext> LOCAL = new TransmittableThreadLocal<AppContext>() {
        @Override
        protected AppContext initialValue() {
            return new AppContext();
        }
    };

    public static AppContext getContext() {
        return LOCAL.get();
    }

    public static void setContext(AppContext context) {
        LOCAL.set(context);
    }

    public static void removeContext() {
        LOCAL.remove();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
