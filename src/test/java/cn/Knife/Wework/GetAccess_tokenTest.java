package cn.Knife.Wework;

import cn.Knife.Wework.AddressBookManagement.GetAccess_token;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;


/**
 * @author Knife
 * @description
 * @createTime 2020-05-07 15:50
 */
public class GetAccess_tokenTest extends BaseTest{
    //获取一个GetAccess_token实例对象，才能调用它的方法
    private GetAccess_token getAccess_token = new GetAccess_token();
    private String corpid = "ww2df66b08696343ff";
    private String corpsecret = "vj1C5akNwtxZb18MECiNZym4IVBTWEi18L57r1KJF2s";

    @Test(description = "获取token")
    public void testGetToken() {
        getAccess_token.getToken(corpid, corpsecret).then() //获取响应信息
                .log().all()  //打印全部响应日志
                .statusCode(200) //断言状态码是200
                .body("errcode", equalTo(0)) //断言body中的errcode字段值是0
        ;
    }

    @Test(description = "获取token异常测试")
    public void errGetToken() {
        //获取一个GetAccess_token实例对象，才能调用它的方法
        GetAccess_token getAccess_token = new GetAccess_token();
        String corpid = "ww2df66b08696343ff";
        String corpsecret = "vj1C5akNwtxZb18MECiNZym4IVBTWEi18L57r1KJF2s";
        //corpid为空
        getAccess_token.getToken("", corpsecret).then()
                .log().all()
                .statusCode(200)
                .body("errcode", not(equalTo(0)));
    }
}