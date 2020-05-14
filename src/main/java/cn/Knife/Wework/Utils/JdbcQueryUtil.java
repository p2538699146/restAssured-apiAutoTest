package cn.Knife.Wework.Utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * @author Knife
 * @description 执行sql
 * @createTime 2020-05-11 0:21
 */
public class JdbcQueryUtil extends Super_Utils {

    //properties工具
    private static Properties properties;

    //jdbc配置文件路径
    private static final String JDBC_PATH = "/conf/jdbc.properties";

    //添加当前对象到日志容器
    private static Logger logger = Logger.getLogger(JdbcQueryUtil.class);

    //获取数据库连接对象
    private static Connection connection;


    /**
     * 初始化，加载properties
     *
     * @return
     */
    private static Properties getProperties() {
        //如果properties为null，则创建
        if (Objects.isNull(properties)) {
            properties = new Properties();
        }

        //通过内部代码块加载properties配置内容
        {
            try {
                properties.load(JdbcQueryUtil.class.getResourceAsStream(JDBC_PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }


    /**
     * 修改或查询数据库
     * map中key的表示：修改："$.update"，查询："$.query"
     *
     * @param sqlsMap 写入要获取的字段值
     * @return 返回每个字段查询到的结果集
     */
    public static List<HashMap<String, Object>> jdbcQueryAndUpdate(HashMap<String, List<String>> sqlsMap) {

        //定义储存查询结果的容器
        HashMap<String, Object> queryValue;
        List<HashMap<String, Object>> queryVlues;

        if (Objects.nonNull(sqlsMap)) {

            //定义PreparedStatement对象，此对象有操作数据库的方法
            PreparedStatement preparedStatement;

            try {

                //获取数据库连接
                connection = getConnection();

                //判断当前是更新还是查询数据库
                if (Objects.nonNull(sqlsMap.get("$.update"))) {

                    logger.info("开始执行更新数据操作！");

                    //从list中取出需要执行的sql
                    List<String> list = sqlsMap.get("$.update");

                    for (String update_sql : list) {
                        //如果是更新执行更新操作
                        preparedStatement = connection.prepareStatement(update_sql);
                        //执行修改操作
                        preparedStatement.executeUpdate();

                        logger.info("更新成功，执行的sql：" + update_sql);
                    }
                }

                if (Objects.nonNull(sqlsMap.get("$.query"))) {

                    logger.info("开始执行查询操作！");
                    List<String> list = sqlsMap.get("$.query");

                    //每次入query都重新创建容器
                    queryVlues = new ArrayList<>();

                    for (String query_sql : list) {

                        preparedStatement = connection.prepareStatement(query_sql);

                        //执行查询，获得结果集
                        ResultSet resultSet = preparedStatement.executeQuery();

                        //获取查询的相关信息
                        ResultSetMetaData metaData = resultSet.getMetaData();

                        //获取查询字段的数值
                        int columnCount = metaData.getColumnCount();

                        //没执行一次查询new一次map，所以不用担心里面的值混乱
                        queryValue = new HashMap<>();

                        while (resultSet.next()) {
                            //循环取出每个字段的值
                            for (int i = 0; i < columnCount; i++) {

                                //获取查询字段 //
                                String queryField = metaData.getColumnLabel(i + 1);
                                String value = null;
                                //判断当前字段是否为空
                                if (Objects.nonNull(queryField)) {
                                    //如果不为空，取出查询到的value
                                    value =
                                            Objects.nonNull(resultSet.getObject(queryField)) ?
                                                    (resultSet.getObject(queryField).toString()) : "";
                                }

                                //将查询到的结果添加到map
                                queryValue.put(queryField, value);

                            }
                        }

                        logger.info("\n当前执行查询sql：\n" + query_sql
                                + "\n查询结果：" + queryValue);

                        queryVlues.add(queryValue);
                    }
                    return queryVlues;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 获取数据库连接
     *
     * @return
     */
    private static Connection getConnection() {

        if (Objects.isNull(connection)) {

            //获取properties对象
            properties = getProperties();

            //从properties中获取，url、user、password
            //properties文件，写入/读取，都是已key：value格式
            String url = properties.getProperty("jdbc.url");

            String user = properties.getProperty("jdbc.username");

            String password = properties.getProperty("jdbc.password");

            try {

                logger.info("\n***********开始连接数据库***********"
                        + "\n连接地址：[" + url + "]"
                        + "\n用户名：[" + user + "]"
                        + "\n密码：[" + password + "]");

                //获取数据库连接
                connection = DriverManager.getConnection(url, user, password);

            } catch (SQLException e) {
                logger.error("数据库连接失败，请检查链接地址、用户名、密码！");
                logger.error("报错内容：" + e);
            }
        }
        //返回Connection对象
        return connection;
    }
}
