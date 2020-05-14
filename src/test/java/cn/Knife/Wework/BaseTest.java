package cn.Knife.Wework;

import cn.Knife.Wework.Utils.OutputDos;
import cn.Knife.Wework.Utils.SendEmail;
import io.qameta.allure.Description;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.Objects;

/**
 * @author Knife
 * @description
 * @createTime 2020-05-13 23:03
 */
public class BaseTest {

    private static OutputDos od;

    private static SendEmail sendEmail = SendEmail.getInstance();

    @Description("删除allure遗留的测试结果")
    @BeforeSuite
    public void be() {
        if (Objects.isNull(od)) {
            od = new OutputDos();
            //运行前删除allure遗留测试结果
            od.delAllureResults("/allure-results");
        }
    }


    @Description("解析allure.json文件为report，发送邮件")
    @AfterSuite
    public void af() {
        //解析allure的json文件为html
        od.allureGenerateHtmlReport("/allure-results", "/allure-report");

        sendEmail.send("接口自动化测试", "/allure-report/data/suites.json", "2538699146@qq.com");

    }
}
