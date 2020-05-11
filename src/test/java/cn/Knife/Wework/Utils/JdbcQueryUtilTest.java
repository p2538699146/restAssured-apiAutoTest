package cn.Knife.Wework.Utils;

import org.testng.annotations.Test;

import java.util.*;


/**
 * @author Knife
 * @description
 * @createTime 2020-05-11 1:41
 */
public class JdbcQueryUtilTest {


    @Test
    public void testJdbcQueryAndUpdate() {
        List<String> list = Arrays.asList("select * from apiautotestparamter where module = 'login' and api_Uri = 'login_by';"
                , "select * from apiautotestparamter where case_id = 6;");
        List<HashMap<String, Object>> list1 = JdbcUtil.jdbcQueryAll(list);
        for (HashMap<String, Object> stringStringHashMap : list1) {
            System.out.println(stringStringHashMap);
        }

    }

    @Test
    public void testUpdate() {
        HashMap<String, String> sql = new HashMap<String, String>() {{
            put("$.update","UPDATE apiautotestparamter \n" +
                    "SET api_Name = '我们开始修改啦啊啦啦啦' \n" +
                    "WHERE\n" +
                    "\tapi_Type = 'post_json' \n" +
                    "\tAND case_Id = 1;");
        }};
        //jdbcQueryUtil.jdbcQueryAndUpdate(sql);
    }

}