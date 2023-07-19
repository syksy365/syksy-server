package site.syksy.qingzhou.global.file;

/**
 * 文件下载的方式
 * @author Raspberry
 */
public enum RangeSwitchEnum {
    /**
     * 普通
     */
    ORDINARY,
    /**
     * 断点继续
     */
    BEGIN,
    /**
     * 区间
     */
    BLOCK
}
