package cn.Knife.Wework.Utils;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import static cn.Knife.Wework.AddressBookManagement.WeworkLogin.getToken;
import static io.restassured.RestAssured.given;

/**
 * @author Knife
 * @description
 * @createTime 2020-05-08 2:28
 */
public class Rest_Perfect extends JdbcUtil {

    private static Logger logger = Logger.getLogger(Rest_Perfect.class);

    /**
     * 重写父类方法，每次调用都刷新RequestSpecification里面的内容
     *
     * @return
     */
    public RequestSpecification getDefaultRequestSpecification() {

        logger.info("开始初始化RequestSpecification");

        logger.info("\n              ____  __.        .__   _____                        \n" +
                "  /\\|\\/\\       |    |/ _|  ____  |__|_/ ____\\  ____        /\\|\\/\\   \n" +
                " _)    (__     |      <   /    \\ |  |\\   __\\ _/ __ \\      _)    (__ \n" +
                " \\_     _/     |    |  \\ |   |  \\|  | |  |   \\  ___/      \\_     _/ \n" +
                "   )    \\   /\\ |____|__ \\|___|  /|__| |__|    \\___  > /\\    )    \\  \n" +
                "   \\/\\|\\/   \\/         \\/     \\/                  \\/  \\/    \\/\\|\\/  ");


        RequestSpecification requestSpecification;
        requestSpecification = given()
                .log().all()    //打印请求头信息
                .contentType(ContentType.JSON)  //设置ContentType参数类型为json
                .queryParam(getToken()[0], getToken()[1]);  //将token携带在url
        requestSpecification.filter((req, res, ctx) -> {
            //todo： 对请求响应，做封装
            return ctx.next(req, res);
        });
        requestSpecification.then()
                .log().all()
                .expect().statusCode(200);   //打印响应信息，断言状态是200
        return requestSpecification;
    }

    /**
     * 获取当前系统时间，纯数字
     *
     * @return
     */
    public String getSystemDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();

        logger.info("当前系统时间：" + date);
        return dateFormat.format(date);
    }


    /**
     * 获取年-月-日系统时间
     *
     * @return
     */
    public static String getFormatDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy月MM月dd日 HH:mm:ss");
        Date date = new Date();

        logger.info("当前系统时间：" + date);
        return dateFormat.format(date);
    }
}
