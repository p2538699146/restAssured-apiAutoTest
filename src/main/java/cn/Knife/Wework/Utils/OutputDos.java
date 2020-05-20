package cn.Knife.Wework.Utils;

import org.apache.log4j.Logger;

import java.io.*;


/**
 * @author Knife
 * @description
 * @createTime 2020-05-13 15:49
 */
public class OutputDos extends Rest_Perfect {

    private static Logger logger = Logger.getLogger(OutputDos.class);

    private static String classPath = OutputDos.class.getResource("/").getPath();

    static {
        //获取target根路径
        classPath = classPath.substring(1, classPath.indexOf("test-classes"));
    }


    /**
     * 输入路径（从target根路径输入）：删除目录下全部文件
     *
     * @param path
     */
    public static void delAllureResults(String path) {
        File file = new File(classPath + path);
        File[] files = file.listFiles();
        for (File f : files) {
            f.delete();
            logger.info(String.format("当前文件：%s，删除成功", f));
        }
    }

    /**
     * 输入路径（从target根路径输入）：results和report得到allure得html报告
     *
     * @param rawPath
     * @param outPath
     */
    public static void allureGenerateHtmlReport(String rawPath, String outPath) {
        StringBuffer dos = new StringBuffer();
        dos.append(String.format("cmd /c allure generate %s -o %s --clean", (classPath + rawPath), (classPath + outPath)));
        try  {
            Process exec = Runtime.getRuntime().exec(dos.toString());
            //执行dos命令
            exec.waitFor();
        } catch (Exception e) {
            logger.error("文件路径错误，请重新输入！");
            logger.error("报错内容：" + e);
        }
    }
}
