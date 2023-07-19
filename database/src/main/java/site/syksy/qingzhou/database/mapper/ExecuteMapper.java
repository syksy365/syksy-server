package site.syksy.qingzhou.database.mapper;

/**
 * @author Raspberry
 */
public interface ExecuteMapper {

    /**
     * 执行任意sql语句
     *
     * @param sql sql语句
     */
    void execute(String sql);
}
