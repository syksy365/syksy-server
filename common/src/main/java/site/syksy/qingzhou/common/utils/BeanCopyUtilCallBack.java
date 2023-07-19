package site.syksy.qingzhou.common.utils;

/**
 * @author https://www.cnblogs.com/Johnson-lin/p/12123012.html
 */
@FunctionalInterface
public interface BeanCopyUtilCallBack <S, T> {

    /**
     * 定义默认回调方法
     * @param t
     * @param s
     */
    void callBack(S t, T s);
}