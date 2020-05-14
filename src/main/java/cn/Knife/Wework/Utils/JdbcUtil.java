package cn.Knife.Wework.Utils;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author Knife
 * @description 批量/单条sql执行工具类：基于JdbcQueryUtil重新封装，控制执行
 * @createTime 2020-05-11 18:11
 */
public class JdbcUtil extends JdbcQueryUtil {

    private static JdbcUtil jdbcUtil;

    /**
     * 获取jdbcUtil
     *
     * @return
     */
    public static JdbcUtil getInstance() {
        if (Objects.isNull(jdbcUtil)) {
            jdbcUtil = new JdbcUtil();
        }
        return jdbcUtil;
    }


    /**
     * 批量执行sql查询操作
     *
     * @param sqls
     * @return
     */
    public static List<HashMap<String, Object>> jdbcQueryAll(List<String> sqls) {
        HashMap<String, List<String>> querys = new HashMap<String, List<String>>() {{
            put("$.query", sqls);
        }};
        return jdbcQueryAndUpdate(querys);
    }


    /**
     * 批量执行修改操作
     *
     * @param sqls
     */
    public static void jdbcUpdateAll(List<String> sqls) {
        HashMap<String, List<String>> querys = new HashMap<String, List<String>>() {{
            put("$.update", sqls);
        }};
        jdbcQueryAndUpdate(querys);
    }


    /**
     * 执行单条sql查询操作
     *
     * @param sql
     * @return
     */
    public static HashMap<String, Object> query(String sql) {
        List<String> list = Arrays.asList(sql);
        List<HashMap<String, Object>> contens = jdbcQueryAll(list);
        if (Objects.nonNull(contens)) {
            return contens.get(0);
        }
        return null;
    }

    /**
     * 执行单条sql修改操作
     *
     * @param sql
     * @return
     */
    public static void update(String sql) {
        List<String> list = Arrays.asList(sql);
        jdbcUpdateAll(list);
    }
}
