package cn.Knife.Wework;

import cn.Knife.Wework.AddressBookManagement.Department;
import cn.Knife.Wework.VariablesStore.VariablesUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;

/**
 * @author Knife
 * @description
 * @createTime 2020-05-11 21:26
 */
public class VariablesTest extends BaseTest{
    private static Department department = new Department();


    @BeforeClass
    public void setUp() {
        department.deleteAll();
    }

    @Test
    public void getMysqlVariableTest() {

        //从数据库中获取某个值，如果接口依赖这个值，比如：消费卷id，商品code；
        String name = VariablesUtil.getDepartmentName();

        //执行查询
        HashMap<String, Object> query = department.query("select api_Name from apiautotestparamter where case_Id = 8;");
        int aset = Integer.parseInt(query.get("api_Name").toString());

        department.create(name, "111", "1", "1000", "1010")
                //如果我们执行的用例需要数据库断言？？
                //或者执行后写条sql进行结果校验
                .then().body("errcode", equalTo(aset));
    }
}
