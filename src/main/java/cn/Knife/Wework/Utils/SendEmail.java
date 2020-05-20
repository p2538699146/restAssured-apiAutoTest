package cn.Knife.Wework.Utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.log4j.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.*;

/**
 * @author Knife
 * @description 发送自动化测试报告
 * @createTime 2020-05-13 21:53
 */
public class SendEmail extends Rest_Perfect {

    private static Logger logger = Logger.getLogger(SendEmail.class);

    private static SendEmail sendEmail;

    /**
     * 获取sendEmail对象
     *
     * @return
     */
    public static SendEmail getInstance() {
        if (Objects.isNull(sendEmail)) {
            sendEmail = new SendEmail();
        }
        return sendEmail;
    }


    /**
     * 发送邮件
     *
     * @param title          邮件标题
     * @param filePath       allure-report中suite.json文件路径
     * @param addresseeEmail 收件邮箱
     */
    public static void send(String title, String filePath, String addresseeEmail) {

        //根据运行测试类，和main入口判断
        String p = SendEmail.class.getResource("/").getPath();
        String[] split = p.split("/");
        for (String s : split) {
            if ("test-classes".equals(s)) {
                filePath = p.substring(0, p.indexOf("/test-classes")) + filePath;
            }
            if ("classes".equals(s)) {
                filePath = p.substring(0, p.indexOf("/classes")) + filePath;

            }
        }
        //获取allure报告内容
        String sendEmailText = getAllureResult(filePath);

        // 创建一个Property文件对象
        Properties props = new Properties();

        // 设置邮件服务器的信息，这里设置smtp主机名称
        props.put("mail.smtp.host", "smtp.163.com");

        // 设置socket factory 的端口
        props.put("mail.smtp.socketFactory.port", "465");

        // 设置socket factory
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // 设置需要身份验证
        props.put("mail.smtp.auth", "true");

        // 设置SMTP的端口，QQ的smtp端口是25
        props.put("mail.smtp.port", "25");

        // 身份验证实现
        Session session = Session.getDefaultInstance(props, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                // 第二个参数，就是我网易邮箱开启smtp的授权码
                return new PasswordAuthentication("写自己邮箱@163.com", "***写自己的授权码");

            }

        });

        try {

            // 创建一个MimeMessage类的实例对象
            Message message = new MimeMessage(session);

            // 设置发件人邮箱地址
            message.setFrom(new InternetAddress("写自己邮箱@163.com"));

            // 设置收件人邮箱地址
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addresseeEmail));

            // 设置邮件主题
            title = getFormatDateTime() + " " + title;
            message.setSubject(title);

            // 创建一个MimeBodyPart的对象，以便添加内容
            BodyPart messageBodyPart1 = new MimeBodyPart();

            // 设置邮件正文内容
            messageBodyPart1.setText(sendEmailText);

            // 创建另外一个MimeBodyPart对象，以便添加其他内容
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            // 创建一个datasource对象，并传递文件
            DataSource source = new FileDataSource(filePath);

            // 设置handler
            messageBodyPart2.setDataHandler(new DataHandler(source));

            // 加载文件
            /*messageBodyPart2.setFileName(filePath);*/

            // 创建一个MimeMultipart类的实例对象
            Multipart multipart = new MimeMultipart();

            // 添加正文1内容
            multipart.addBodyPart(messageBodyPart1);

            // 添加正文2内容
            //multipart.addBodyPart(messageBodyPart2);

            // 设置内容
            message.setContent(multipart);

            // 最终发送邮件
            Transport.send(message);


            logger.info("\n邮件主题：" + title +
                    "\n邮件内容：" + sendEmailText +
                    "\n收件人邮箱：" + addresseeEmail +
                    "\n****************************邮件发送成功****************************");

        } catch (MessagingException e) {

            logger.error("邮件发送异常！");
            logger.error("报错为：" + e);
        }

    }


    /**
     * 解析allure-report中suite.json文件内容
     *
     * @param jsonFilePath
     * @return
     */
    public static String getAllureResult(String jsonFilePath) {

        //定义储存用例结果的容器
        StringBuffer suiteResult = new StringBuffer();

        //实例化suiteResult

        int p_sum = 0;  //执行成功case总数
        int f_sum = 0;  //执行失败case总数
        int timeSum = 0;    //运行总共花费时间

        //存储测试方法名
        Object caseName = null;
        try (FileInputStream fileInputStream = new FileInputStream(jsonFilePath)) {
            //获取documentContext对象
            DocumentContext documentContext = JsonPath.parse(fileInputStream);
            //操作documentContext读取json文件中的内容
            List<LinkedHashMap<String, Object>> caseList = documentContext.read("$.children[*].children[*].children[*].children[*]");

            //遍历获取到的所有测试类
            HashMap<String, Object> err_Map = new HashMap<>();
            for (LinkedHashMap<String, Object> linkedHashMap : caseList) {
                for (Map.Entry<String, Object> entry : linkedHashMap.entrySet()) {

                    //获取测试方法名
                    if ("name".equals(entry.getKey())) {
                        caseName = entry.getValue();
                    }

                    //获取case执行结果状态
                    if ("status".equals(entry.getKey())) {
                        if ("passed".equals(entry.getValue())) {
                            p_sum++;
                        } else if ("failed".equals(entry.getValue())) {
                            //记录是失败case
                            err_Map.put("err_Case" + (f_sum + 1), caseName);
                            f_sum++;
                        }
                    }
                    //获取case执行时间
                    if ("time".equals(entry.getKey())) {
                        entry.getValue();
                        Map<String, Object> timeMap = JSONObject.parseObject(JSON.toJSONString(entry.getValue()));
                        Integer duration = (Integer) timeMap.get("duration");
                        timeSum += duration;
                    }
                }
            }

            suiteResult.append("Case执行总数：" + (p_sum + f_sum));
            suiteResult.append("\n通过：" + p_sum + "条");
            suiteResult.append("\n失败：" + f_sum + "条");
            suiteResult.append((f_sum > 0 ? ("\n执行出错的Method：" + JSON.toJSONString(err_Map)) : ""));
            suiteResult.append("\n执行花费时间：" + (timeSum / 1000) + "秒");

            logger.info("allure-report解析成功");

            return suiteResult.toString();
        } catch (Exception e) {
            logger.error("请检查路径，获取json文件内容！");
            logger.error("报错内容：" + e);
        }
        return null;
    }
}
