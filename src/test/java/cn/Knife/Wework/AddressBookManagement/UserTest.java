package cn.Knife.Wework.AddressBookManagement;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;


/**
 * @author Knife
 * @description
 * @createTime 2020-05-08 21:46
 */
@Epic("企业微信-用户管理")
public class UserTest {

    private static User user;


    @Description("运行前，初始化user对象，清理测试数据")
    @Story("初始化User对象")
    @BeforeClass
    public void setUp() {
        //如果user为空则创建，单例懒汉式
        user = Objects.isNull(user) ? new User() : user;
        //运行前清理数据
        user.deleteAll();
    }


    @Description("创建用户正确业务流程")
    @TmsLink("case_createUser_1")
    @Story("创建用户")
    @Test
    public void testCreate() {
        String data = user.getSystemDate();
        String userid = "Knife_" + data.substring(8, 14);
        String name = "Knife" + data.substring(10, 14);
        String mobile = "183" + data.substring(6, 14);
        String email = data.substring(8, 14) + "@163.com";
        user.create(userid, name, mobile, email).then().body("errcode", equalTo(0));
    }


    @Description("查询用户正确业务流程")
    @TmsLink("case_getUser_2")
    @Story("查询用户")
    @Test
    public void testGet() {
        String data = user.getSystemDate();
        String userid = "Knife_" + data.substring(8, 14);
        String name = "Knife" + data.substring(10, 14);
        String mobile = "183" + data.substring(6, 14);
        String email = data.substring(8, 14) + "@163.com";
        //创建用户
        user.create(userid, name, mobile, email);
        //查询用户，断言name和email
        user.get(userid).then().body("errcode", equalTo(0))
                .body("name", equalTo(name))
                .body("email", equalTo(email));
        //未输入userid，判断errcode不等于0
        user.get("").then().body("errcode", not(equalTo(0)));
    }


    @Description("修改用户正确业务流程")
    @TmsLink("case_updateUser_3")
    @Story("更新用户资料")
    @Test
    public void testUpdate() {
        String data = user.getSystemDate();
        String userid = "Knife_" + data.substring(8, 14);
        String name = "Knife" + data.substring(10, 14);
        String mobile = "183" + data.substring(6, 14);
        String email = data.substring(8, 14) + "@163.com";
        user.create(userid, name, mobile, email).then().body("errcode", equalTo(0));
        //查询用户，断言name和email
        user.get(userid).then().body("errcode", equalTo(0))
                .body("name", equalTo(name))
                .body("email", equalTo(email));

        //更新用户name，email
        String updateName = "update" + data.substring(8, 14);
        String updateEmail = data.substring(8, 14) + "@Knife.com";
        user.update(userid, updateName, mobile, updateEmail).then().body("errcode", equalTo(0));
        //断言更新过的name，email
        user.get(userid).then().body("name", equalTo(updateName)).body("email", equalTo(updateEmail));
    }


    @Description("删除用户正确业务流程")
    @TmsLink("case_deleteUser_4")
    @Story("删除用户")
    @Test
    public void testDelete() {
        String data = user.getSystemDate();
        String userid = "Knife_" + data.substring(8, 14);
        String name = "Knife" + data.substring(10, 14);
        String mobile = "183" + data.substring(6, 14);
        String email = data.substring(8, 14) + "@163.com";
        user.create(userid, name, mobile, email);

        //删除user
        user.delete(userid).then().body("errcode", equalTo(0));
    }


    @Description("批量删除用户正确业务流程")
    @TmsLink("case_batchDeleteUser_5")
    @Story("批量删除用户")
    @Test
    public void testBatchdelete() {
        String data = user.getSystemDate();
        String userid = "Knife_" + data.substring(8, 14);
        String name = "Knife" + data.substring(10, 14);
        String mobile = "183" + data.substring(6, 14);
        String email = data.substring(8, 14) + "@163.com";
        user.create(userid, name, mobile, email).then().body("errcode", equalTo(0));
        String userid2 = "Knife2" + data.substring(8, 14);
        user.update(userid, "two" + data.substring(8, 14), "132" + data.substring(6, 14), data.substring(6, 12) + "@Knife.com");

        user.batchdelete(Arrays.asList(userid, userid)).then().body("errcode", equalTo(0));
        user.get(userid).then().body("errcode", not(equalTo(0)));
        user.get(userid2).then().body("errcode", not(equalTo(0)));
    }


    @Description("获取部门详情正确业务流程")
    @TmsLink("case_simpleListUser_6")
    @Story("获取部门成员详情")
    @Test
    public void testSimplelist() {
        user.simplelist("1", "1").then()
                .body("errcode", equalTo(0))
                .body("userlist[0].userid", equalTo("PengHui"));

        user.simplelist("1", "0").then()
                .body("errcode", equalTo(0));
    }


    @Description("获取部门成员正确/错误业务流程")
    @TmsLink("case_listUser_7")
    @Story("获取部门成员详情")
    @Test
    public void testList() {
        user.list("1", "1").then()
                .body("errcode", equalTo(0));

        user.list("0", "0").then()
                .body("errcode", not(equalTo(0)));
    }


    @Description("运行后清理测试数据")
    @Story("数据清理")
    @AfterClass
    public void tearDown() {
        //运行后清理数据
        user.deleteAll();
    }
}