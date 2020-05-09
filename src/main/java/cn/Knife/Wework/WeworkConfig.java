package cn.Knife.Wework;

import cn.Knife.Wework.Utils.Super_Utils;

import java.util.Objects;

/**
 * @author Knife
 * @description
 * @createTime 2020-05-08 0:22
 */
public class WeworkConfig extends Super_Utils {

    public String corpid = "1";
    public String corpsecret = "1";

    private static WeworkConfig weworkConfig;

    /**
     * 单例懒汉式：提供方法给外界访问，获得WeworkConfig对象
     *
     * @return
     */
    public static WeworkConfig getInstance() {
        if (weworkConfig == null) {
            weworkConfig = getWeworkConfig("/conf/WeworkConfig.yaml");
        }
        return weworkConfig;
    }

    /**
     * 返回一个WeworkConfig对象，这里是yaml模板
     *
     * @param path yaml路径
     * @return
     */
    public static WeworkConfig getWeworkConfig(String path) {
        if (Objects.nonNull(path)) {
            WeworkConfig wc = new WeworkConfig();
            return (WeworkConfig) YamlFactoryIsReadYamlAndWriterYaml(path, "1", wc);
        } else {
            throw new RuntimeException("当前文件路径为空！");
        }
    }
}
