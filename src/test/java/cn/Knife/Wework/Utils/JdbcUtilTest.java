package cn.Knife.Wework.Utils;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * @author Knife
 * @description 基于
 * @createTime 2020-05-11 18:16
 */
public class JdbcUtilTest {

    private static JdbcUtil jdbcUtil = JdbcUtil.getInstance();

    @Test
    public void testQuery() {
        String sql = "select * from apiautotestparamter where case_id = 6;";
        HashMap<String, Object> query = jdbcUtil.query(sql);
        System.out.println(query);
    }

    @Test
    public void testQueryAll() {
        String sql = "select * from apiautotestparamter where case_id = 6;";

        List<String> list = Arrays.asList(sql, "select api_Name from apiautotestparamter where case_id = 1;");
        jdbcUtil.jdbcQueryAll(list);
    }


    @Test
    public void testUpdate() {
        String updateSql = "UPDATE apiautotestparamter \n" +
                "SET api_Name = '修改测试' \n" +
                "WHERE\n" +
                "\tapi_Type = 'post_json' \n" +
                "\tAND case_Id = 1;";

        jdbcUtil.update(updateSql);
    }

    @Test
    public void testUpdateAll() {
        String updateSql = "UPDATE apiautotestparamter \n" +
                "SET api_Name = '批量修改测试1' \n" +
                "WHERE\n" +
                "\tapi_Type = 'post_json' \n" +
                "\tAND case_Id = 1;";


        List<String> list = Arrays.asList(updateSql, "UPDATE apiautotestparamter \n" +
                "SET api_Name = '批量修改2' \n" +
                "WHERE\n" + "case_Id = 6;");

        jdbcUtil.jdbcUpdateAll(list);
    }
}