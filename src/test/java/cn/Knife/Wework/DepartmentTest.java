package cn.Knife.Wework;

import cn.Knife.Wework.AddressBookManagement.Department;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.testng.annotations.*;

import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

/**
 * @author Knife
 * @description
 * @createTime 2020-05-07 17:52
 */
public class DepartmentTest {

    private static Department department;

    @BeforeClass
    @Step("运行前初始化，1.department为空则new，2.运行前清理数据")
    public void setUp() {
        department = Objects.isNull(department) ? new Department() : department;
        //初始化时清理测试数据
        department.deleteAll();
    }

    @Test
    @Description(value = "正确创建部门测试")
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

    @Test(description = "修改部门测试")
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

    @Test(description = "删除部门测试")
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

    @AfterClass
    public void tearDown() {
        //运行结束清理数据
        department.deleteAll();
        department.create("name", "SUPER_Test" + "data", "1", "1", "2")
                .then().statusCode(200).body("errcode", equalTo(0));
    }
}