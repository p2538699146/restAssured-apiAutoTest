package cn.Knife.Wework.Utils;


import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author Knife
 * @description 批量/单条sql执行工具类：基于JdbcQueryUtil重新封装，控制执行
 * @createTime 2020-05-11 18:11
 */
public class JdbcUtil extends JdbcQueryUtil {

    private static JdbcUtil jdbcUtil;

    private static Logger logger = Logger.getLogger(JdbcUtil.class);

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
     * 批量执行修改操作
     *
     * @param sqls
     */
    public static void jdbcUpdateAll(List<String> sqls, Object... args) {
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
     * 执行单条sql，根据占位符"?"，替换值为args
     *
     * @param sql
     * @param args
     * @return
     */
    public static HashMap<String, Object> query(String sql, Object... args) {
        //替换占位符为指定值
        String formatSql = strFormatByArgs(sql, args);
        List<HashMap<String, Object>> contens = jdbcQueryAll(Arrays.asList(formatSql));
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


    /**
     * 执行单条sql修改操作，根据占位符"?"，替换值为args
     *
     * @param sql  执行的sql语句
     * @param args 可变形参
     */
    public static void update(String sql, Object... args) {
        //替换占位符为指定值
        String formatSql = strFormatByArgs(sql, args);
        List<String> list = Arrays.asList(formatSql);
        jdbcUpdateAll(list);
    }


    /**
     * 将占位符替换成可变形参值，格式化sql语句
     *
     * @param sql  sql
     * @param args 需要替换的参数
     * @return
     */
    public static String strFormatByArgs(String sql, Object... args) {

        //循环可变形参
        for (int i = 0; i < args.length; i++) {

            //如果可变形参中出现String，则加上单引号，直接传string数据库不识别的
            if (args[i] instanceof String) {
                args[i] = "\'" + args[i] + "\'";
            }
        }


        //循环字符串
        for (int i = 0; i < sql.length(); i++) {

            //比较是否出现占位符
            if ("?".equals(String.valueOf(sql.charAt(i)))) {

                //将占位符替换成"%s"
                sql = sql.replace("?", "%s");

            }
        }

        logger.info("待执行SQL中，占位符\"?\"已转换为指定值！");

        //通过format格式化参数，进行赋值操作
        return String.format(sql, args);
    }
}
