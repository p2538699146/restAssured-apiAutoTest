package cn.Knife.Wework.AddressBookManagement;

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
public class UserTest {

    private User user = new User();

    @BeforeClass
    public void setUp() {
        //如果user为空则创建，单例懒汉式
        user = Objects.nonNull(user) ? new User() : user;
        //运行前清理数据
        user.deleteAll();
    }

    @Test
    public void testCreate() {
        String data = user.getSystemDate();
        String userid = "Knife_" + data.substring(8, 14);
        String name = "Knife" + data.substring(10, 14);
        String mobile = "183" + data.substring(6, 14);
        String email = data.substring(8, 14) + "@163.com";
        user.create(userid, name, mobile, email).then().body("errcode", equalTo(0));
    }

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

    @Test
    public void testSimplelist() {
        user.simplelist("1", "1").then()
                .body("errcode", equalTo(0))
                .body("userlist[0].userid", equalTo("PengHui"));
    }

    @Test
    public void testList() {
        user.list("1", "1").then()
                .body("errcode", equalTo(0));
    }

    @AfterClass
    public void tearDown() {
        //运行后清理数据
        user.deleteAll();
    }
}