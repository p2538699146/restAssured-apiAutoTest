package cn.Knife.Wework.AddressBookManagement;

import cn.Knife.Wework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Knife
 * @description
 * @createTime 2020-05-15 1:59
 */
public class SendEmailTest extends BaseTest {

    @Test
    public void test1() {
        String s = "我来测试了";
        Assert.assertEquals(s,"我来测试了");
    }

    @Test
    public void test2() {
        String s = "我来测试了";
        Assert.assertEquals(s,"我来测试了");
    }
    @Test
    public void test3() {
        String s = "我来测试了";
        Assert.assertEquals(s,"我来测试了");
    }
    @Test
    public void test4() {
        String s = "我来测试了";
        Assert.assertEquals(s,"我来测试了");
    }

    @Test
    public void errTest() {
        Object o = null;
        Assert.assertNotNull(o);
    }

    @Test
    public void errTestNotEquals(){
        String a = "Knife";
        String b = "Knife";
        Assert.assertNotEquals(a,b);

    }
}
