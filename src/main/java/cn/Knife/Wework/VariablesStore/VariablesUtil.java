package cn.Knife.Wework.VariablesStore;

import cn.Knife.Wework.Utils.JdbcUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * @author Knife
 * @description
 * @createTime 2020-05-11 21:09
 */
public class VariablesUtil extends JdbcUtil {

    private static Logger logger = Logger.getLogger(VariablesUtil.class);

    /**
     * 从数据库中获取变量
     *
     * @return
     */
    public static String getDepartmentName() {

        logger.info("开始从数据库获取变量！");

        String sql = "select  api_Name from apiautotestparamter where case_Id = 1";
        HashMap<String, Object> query = query(sql);

        logger.info("获取到的变量值：" + query.get("api_Name").toString());

        return query.get("api_Name").toString();
    }
}
