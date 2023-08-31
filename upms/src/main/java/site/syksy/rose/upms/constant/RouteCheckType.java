package site.syksy.rose.upms.constant;

/**
 * 接口路由的鉴权类型
 * @author Raspberry
 */
public interface RouteCheckType {
    /**
     * 免登录
     */
    int NO_LOGIN = -1;

    /**
     * 登录
     */
    int LOGIN = 0;

    /**
     * 授权
     */
    int AUTH = 1;
}
