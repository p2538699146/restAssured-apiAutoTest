package cn.Knife.Wework;

import org.testng.annotations.Test;



/**
 * @author Knife
 * @description
 * @createTime 2020-05-07 21:22
 */
public class WeworkConfigTest {

    private WeworkConfig weworkConfig = new WeworkConfig();

    @Test
    public void yamlTest() {
        WeworkConfig we = weworkConfig.getWeworkConfig("/conf/WeworkConfig.yaml");
        System.out.println(we.corpid);
        System.out.println(we.corpsecret);

    }
}