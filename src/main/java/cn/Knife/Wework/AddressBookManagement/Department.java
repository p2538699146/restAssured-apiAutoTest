package cn.Knife.Wework.AddressBookManagement;

import cn.Knife.Wework.Utils.Rest_Perfect;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;

/**
 * @author Knife
 * @description 部门管理
 * @createTime 2020-05-07 16:45
 */
public class Department extends Rest_Perfect {


    /**
     * 创建部门
     *
     * @param name     部门名称
     * @param name_en  别名
     * @param parentid 父类id
     * @param order    排序
     * @param id       id
     */
    public Response create(String name, String name_en, String parentid, String order, String id) {
        //将参数都添加到map
        HashMap<String, Object> parame = new HashMap();
        parame.put("name", name);
        parame.put("name_en", name_en);
        parame.put("parentid", parentid);
        parame.put("order", order);
        parame.put("id", id);

        String body = JSON.toJSONString(parame);
        return getResponseFromYaml("/department/create.yaml",new HashMap<String, Object>(){{
            put("$.body",body);
        }});
    }


    /**
     * 更新部门
     *
     * @param name     部门名称
     * @param name_en  别名
     * @param parentid 父类id
     * @param order    排序
     * @return
     */
    public Response update(String name, String name_en, String parentid, String order, String id) {
        //将参数都添加到map
        HashMap<String, Object> parame = new HashMap();
        parame.put("name", name);
        parame.put("name_en", name_en);
        parame.put("parentid", parentid);
        parame.put("order", order);
        parame.put("id", id);
        //将map序列化成一个对象
        JSONObject jsonBody = new JSONObject(parame);
        return getDefaultRequestSpecification()
                .body(jsonBody.toString())
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/update")
                .then()
                .extract().response();
    }

    /**
     * 删除部门
     *
     * @param id 部门id
     * @return
     */
    public Response delete(String id) {
        return getDefaultRequestSpecification()
                .queryParam("id",id)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/department/delete")
                .then()
                .extract().response();
    }

    /**
     * 获取部门列表
     *
     * @param id 部门id
     * @return
     */
    public Response list(String id) {
        return getDefaultRequestSpecification()
                .queryParam("id", id)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/department/list")
                .then()
                .extract().response();
    }

    /**
     * 删除所有部门
     * @return
     */
    public Response deleteAll() {
        //是不是觉得语法很特殊，比起client加工具类处理依赖方便一些，rest直接支持jsonpath语法
        List<Object> userIdLsit = list("").path("department.id");
        //原始部门是不能删除的哈，有子部门也不能删除，部门下存在人员也不能删除
        userIdLsit.remove(0);
        //删除userIdLsit中的所有部门
        userIdLsit.forEach(
                userid -> delete(userid.toString())
        );
        return null;
    }
}
