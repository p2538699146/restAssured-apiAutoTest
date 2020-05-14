package cn.Knife.Wework;

import cn.Knife.Wework.AddressBookManagement.WeworkLogin;
import org.testng.annotations.Test;


/**
 * @author Knife
 * @description
 * @createTime 2020-05-08 2:10
 */
public class WeworkLoginTest extends BaseTest{

    WeworkLogin weworkLogin = new WeworkLogin();

    @Test
    public void testGetToken() {
        for (String s : weworkLogin.getToken()) {
            System.out.println(s);
        }

    }
}