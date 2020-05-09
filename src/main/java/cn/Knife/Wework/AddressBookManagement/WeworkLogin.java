package cn.Knife.Wework.AddressBookManagement;

import cn.Knife.Wework.WeworkConfig;
import io.restassured.RestAssured;

import java.util.Objects;

/**
 * @author Knife
 * @description
 * @createTime 2020-05-07 23:41
 */
public class WeworkLogin extends WeworkConfig {


    /**
     * 登录获取token
     *
     * @return
     */
    public static String getWeworkToken() {
        return RestAssured.given().log().all()
                .queryParam("corpid", WeworkConfig.getInstance().corpid)
                .queryParam("corpsecret", WeworkConfig.getInstance().corpsecret)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                .then().log().all().statusCode(200)
                .extract().path("access_token");
    }

    /**
     * 返回token的key：value
     *
     * @return token[0]：token字段名，token[1]：token值
     */
    public static String[] getToken() {
        if (Objects.isNull(token)) {
            token = new String[]{"access_token", getWeworkToken()};
        }
        return token;
    }
}

