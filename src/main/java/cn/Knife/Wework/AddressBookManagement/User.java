package cn.Knife.Wework.AddressBookManagement;

import cn.Knife.Wework.Utils.Rest_Perfect;
import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;


/**
 * @author Knife
 * @description
 * @createTime 2020-05-08 21:33
 */
public class User extends Rest_Perfect {


    /**
     * 创建user
     *
     * @param userid userid
     * @param name   名字
     * @param mobile 电话
     * @param email  邮箱
     * @return
     */
    public Response create(String userid, String name, String mobile, String email) {
        HashMap<String, Object> parame = new HashMap<String, Object>() {{
            put("userid", userid);
            put("name", name);
            put("mobile", mobile);
            put("email", email);
        }};
        parame.put("$.file", "/user/create.json");
        return getResponseFromYaml("/user/create.yaml", parame);
    }


    /**
     * 读取成员
     *
     * @param userid
     * @return
     */
    public Response get(String userid) {
        HashMap<String, Object> parame = new HashMap<String, Object>() {{
            put("userid", userid);
        }};
        return getResponseFromYaml("/user/get.yaml", parame);
    }


    /**
     * 更新user
     *
     * @param userid userid
     * @param name   名字
     * @param mobile 电话
     * @param email  邮箱
     * @return
     */
    public Response update(String userid, String name, String mobile, String email) {
        String body = JsonPath.parse(User.class.getResourceAsStream("/user/create.json"))
                .set("$.userid", userid)
                .set("$.name", name)
                .set("$.mobile", mobile)
                .set("$.email", email)
                .jsonString();
        HashMap<String, Object> parame = new HashMap<String, Object>() {{
            put("$.body", body);
        }};
        return getResponseFromYaml("/user/update.yaml", parame);
    }


    /**
     * 删除user
     *
     * @param userid
     * @return
     */
    public Response delete(String userid) {
        HashMap<String, Object> parame = new HashMap<String, Object>() {{
            put("userid", userid);
        }};
        return getResponseFromYaml("/user/delete.yaml", parame);
    }

    /**
     * 批量删除user
     *
     * @param userIdList
     * @return
     */
    public Response batchdelete(List<String> userIdList) {
        HashMap<String, Object> parame = new HashMap<String, Object>() {{
            put("useridlist", userIdList);
        }};
        String body = JSON.toJSONString(parame);
        return getResponseFromYaml("/user/batchdelete.yaml", new HashMap<String, Object>() {{
            put("$.body", body);
        }});
    }

    /**
     * 获取部门成员详情
     *
     * @param department_id 部门id
     * @param fetch_child   1/0：是否递归获取子部门下面的成员
     * @return
     */
    public Response simplelist(String department_id, String fetch_child) {
        HashMap<String, Object> parame = new HashMap<String, Object>() {{
            put("department_id", department_id);
            put("fetch_child", fetch_child);
        }};
        return getResponseFromYaml("/user/simplelist.yaml", parame);
    }

    /**
     * 获取部门成员详情
     *
     * @param department_id 部门id
     * @param fetch_child   1/0：是否递归获取子部门下面的成员
     * @return
     */
    public Response list(String department_id, String fetch_child) {
        HashMap<String, Object> parame = new HashMap<String, Object>() {{
            put("department_id", department_id);
            put("fetch_child", fetch_child);
        }};
        return getResponseFromYaml("/user/list.yaml", parame);
    }

    /**
     * 删除全部的用户
     *
     * @return
     */
    public Response deleteAll() {
        List<String> userIdList = list("1", "1").path("userlist.userid");
        //删除创始人数据
        userIdList.remove(0);
        userIdList.forEach(
                userId -> delete(userId)
        );
        return null;
    }
}
