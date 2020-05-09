package cn.Knife.Wework.AddressBookManagement;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * @author Knife
 * @description
 * @createTime 2020-05-07 15:30
 */
public class GetAccess_token {

    /**
     * 获取wework的access_token
     * @param corpid  企业id
     * @param corpsecret    应用密钥
     * @return
     */
    public Response getToken(String corpid, String corpsecret) {
        Response response = RestAssured.given()
                .log().all() //打印请求头信息
                .queryParam("corpid", corpid)
                .queryParam("corpsecret", corpsecret)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/gettoken") //发送get请求
                .then().extract().response();
        return response;
    }

}
