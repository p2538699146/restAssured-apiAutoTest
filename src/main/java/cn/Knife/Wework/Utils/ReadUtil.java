package cn.Knife.Wework.Utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Objects;

/**
 * @author Knife
 * @description 读取文件
 * @createTime 2020-05-14 17:39
 */
public class ReadUtil {

    private static Logger logger = Logger.getLogger(ReadUtil.class);

    private static ReadUtil readUtil;

    public static ReadUtil getInstance() {
        if (Objects.isNull(readUtil)) {
            readUtil = new ReadUtil();
        }
        return readUtil;
    }

    /**
     * 读取文件返回string
     *
     * @param path
     * @return
     */
    public static String readFile(String path) {
        StringBuffer sb = null;
        try (InputStream is = new FileInputStream(new File(path));
             BufferedReader bf = new BufferedReader(new InputStreamReader(is))
        ) {
            sb = new StringBuffer();
            String readVlue;
            while (Objects.nonNull((readVlue = bf.readLine()))) {
                sb.append(readVlue);
            }
            logger.info("\n********************读取结束********************");
            return sb.toString();
        } catch (Exception e) {
            logger.error("读取失败，请检查文件路径！");
            logger.error("报错内容：" + e);
        }
        return null;
    }
}
