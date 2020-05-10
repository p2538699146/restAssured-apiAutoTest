package cn.Knife.Wework;

import cn.Knife.Wework.AddressBookManagement.Department;
import io.qameta.allure.*;
import org.testng.annotations.*;

import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

/**
 * @author Knife
 * @description
 * @createTime 2020-05-07 17:52
 */
@Epic("企业微信-部门管理")   //模块
public class DepartmentTest {

    private static Department department;

    @Description("运行前初始化，运行前清理数据")  //用例描述
    @Step("初始化Department对象")    //执行步骤
    @BeforeClass
    public void setUp() {
        department = Objects.isNull(department) ? new Department() : department;
        //初始化时清理测试数据
        department.deleteAll();
    }


    @TmsLink("department__create_01")    //用例编号
    @Issue("0101")  //bug编号
    @Description("验证部门创建正确业务流程")  //描述
    @Story("部门创建")  //执行步骤
    @Test
    public void testCreate() {
        String data = department.getSystemDate();
        String name = "高级测试部门" + data;
        String nameTwo = "高级测试部门1" + data;
        department.create(name, "SUPER_Test" + data, "1", "1", data)
                .then().statusCode(200).body("errcode", equalTo(0));

        department.create(nameTwo, "SUPER_Test2" + data, "1", "1", data.substring(8, 14))
                .then().statusCode(200).body("errcode", equalTo(0));

        //通过list接口查询到id
        String idOne = department.list("").path("department.find { it.name == '" + name + "'}.id").toString();
        String idTwo = department.list("").path("department.find { it.name == '" + nameTwo + "'}.id").toString();

        //修改创建的部门名称和别名
        department.update("测试进行中" + data.substring(10, 14), "update" + data.substring(10, 14)
                , "1", data.substring(10, 14), idOne);
        department.update("测试进行中" + data.substring(10, 14), "update" + data.substring(10, 14)
                , "1", data.substring(10, 14), idOne);
    }


    @Description("验证修改部门的正确业务流程")
    @Story("修改部门")
    @Test
    public void testUpdate() {
        String data = department.getSystemDate();
        String name = "这是修改试部门测试" + data;
        String nameTwo = "这是修改试部门测试1" + data;
        department.create(name, "Update_Test" + data, "1", "1", data);

        department.create(nameTwo, "Update_Test2" + data, "1", "1", data.substring(8, 14));

        //通过list接口查询到id
        String idOne = department.list("").path("department.find { it.name == '" + name + "'}.id").toString();
        String idTwo = department.list("").path("department.find { it.name == '" + nameTwo + "'}.id").toString();

        //修改创建的部门名称和别名
        department.update("这是修改测试" + data.substring(10, 14), "this is update" + data.substring(10, 14)
                , "1", data.substring(10, 14), idOne);
        department.update("这是修改测试2" + data.substring(10, 14), "this is update2" + data.substring(10, 14)
                , "1", data.substring(10, 14), idOne);
    }

    //@Test(description = "获取部门列表")
    public void testList() {

        //我知道你们在想有没有断言多个参数，rest-assured它支持多个参数断言，同时也能断言多个字段
        department.list("").then().statusCode(200)
                //因为我刚开始已经创建一个了，所以这里断言三个部门
                .body("department.name", hasItems("Knife", "更新高级测试部门", "高级测试部门1"))
                .body("errcode", equalTo(0));

        department.list("1").then()
                .body("department.id", hasItems(1, 100, 101))
                .body("errcode", equalTo(0));

        department.list("").then().statusCode(200)
                //引用官方原话：注意这里的"json path"语法使用的是Groovy的GPath标注法，不要和Jayway的JsonPath语法混淆
                .body("department.find { it.id==100 }.name", equalTo("更新高级测试部门"));
    }

    @Description("删除部门正确业务场景测试")
    @Step("删除部门")
    @Test
    public void testDelete() {
        String data = department.getSystemDate();
        String name = "删除部门测试" + data;
        String nameTwo = "删除部门测试1" + data;
        department.create(name, "delete_Test" + data, "1", "1", data);

        department.create(nameTwo, "delete_Test2" + data, "1", "1", data.substring(8, 14));

        //通过list接口查询到id
        String idOne = department.list("").path("department.find { it.name == '" + name + "'}.id").toString();
        String idTwo = department.list("").path("department.find { it.name == '" + nameTwo + "'}.id").toString();

        //修改创建的部门名称和别名
        department.update("这是删除测试" + data.substring(10, 14), "this is delete" + data.substring(10, 14)
                , "1", data.substring(10, 14), idOne);
        department.update("这是删除测试2" + data.substring(10, 14), "this is delete2" + data.substring(10, 14)
                , "1", data.substring(10, 14), idOne);

        //通过从list接口返回的id，进行部门是删除
        department.delete(idOne).then().body("errcode", equalTo(0));
        department.delete(idTwo).then().body("errcode", equalTo(0));
    }

    @AfterClass(description = "运行后，清理数据")
    @Story("运行后清理数据，创建一个部门")
    public void tearDown() {
        //运行结束清理数据
        department.deleteAll();
        department.create("name", "SUPER_Test" + "data", "1", "1", "2")
                .then().statusCode(200).body("errcode", equalTo(0));
    }
}