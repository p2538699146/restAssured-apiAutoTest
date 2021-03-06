package cn.Knife.Wework.Utils;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Objects;


/**
 * @author Knife
 * @description
 * @createTime 2020-05-07 21:08
 */
public class Super_Utils {

    //初始化日志加载器
    private static Logger logger = Logger.getLogger(Super_Utils.class);

    //储存token的容器
    public static String token[];

    //用于拼接path，此路径为resources根路径
    private static final String RESOURCES_PATH = "src/main/resources/";


    /**
     * 初始化RequestSpecification对象
     *
     * @return
     */
    public RequestSpecification getDefaultRequestSpecification() {
        return RestAssured.given();
    }


    /**
     * 获取Json模板中的数据
     *
     * @param path   json文件路径
     * @param parame 模板中被替换的参数值
     * @return
     */
    public String getTamplateFromJson(String path, HashMap<String, Object> parame) {
        if (Objects.nonNull(path) && Objects.nonNull(parame)) {
            DocumentContext documentContext = JsonPath.parse(this.getClass().getResourceAsStream(path));
            parame.entrySet().forEach(
                    entry -> documentContext.set(entry.getKey(), entry.getValue())
            );
            return documentContext.jsonString();
        }
        logger.warn("当前未获到取Json模板中的数据！");
        logger.warn("当前读取模板路径：" + (Objects.nonNull(path) ? path : "NULL"));
        logger.warn("替换模板内容：" + (Objects.nonNull(parame) ? parame : "NULL"));
        return null;
    }


    /**
     * 通过先更新yaml模板，获取rest_info对象，取出rest_info数据对接口发起请求，返回一个response
     * 传输规范：body："$.body"，header："$.header",使用json模板，传path："$.file"
     *
     * @param path   yaml路径从resources开始
     * @param parame 接口请求参数："$.header"，query，"$.body"
     * @return
     */
    public Response getResponseFromYaml(String path, HashMap<String, Object> parame) {
        Rest_Info rest_info = updateFromYamlRest_Info(path, parame);
        RequestSpecification requestSpecification;
        if (Objects.nonNull(rest_info)) {

            //调用getDefaultRequestSpecification()返回一个requestSpecification对象
            requestSpecification = getDefaultRequestSpecification();
            if (Objects.nonNull(rest_info.query)) {
                rest_info.query.entrySet().forEach(
                        entry -> requestSpecification.queryParam(entry.getKey(), entry.getValue())
                );
            }

            //给requestSpecification.body赋值这里采用三元表达式，如果rest_info中body不为空则赋值为rest_info.body，反则赋值为null
            requestSpecification.body(
                    ((Objects.nonNull(rest_info.body)) ? rest_info.body : "")
            );
            return requestSpecification
                    .when()
                    .request(rest_info.method, rest_info.url + rest_info.uri)
                    .then()
                    .extract().response();
        }
        logger.warn("当前返回对象为NULL，rest_info：" + rest_info);
        return null;
    }

    /**
     * 通过parame更新Rest_Info中的数据
     *
     * @param path
     * @param parame
     * @return
     */
    public Rest_Info updateFromYamlRest_Info(String path, HashMap<String, Object> parame) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        Rest_Info ri = Rest_Info.getInstance();
        if (Objects.nonNull(path) && Objects.nonNull(parame)) {
            Rest_Info rest_info = (Rest_Info) readYamlValue(objectMapper, path, ri);

            //将yaml中的method取出，转换为小写，进行比较
            if ("get".equals(rest_info.method.toLowerCase())) {

                //如果method为get，将parame中的数据添加到Rest_Info的query
                parame.entrySet().forEach(
                        entry -> {
                            rest_info.query.put(entry.getKey(), entry.getValue());
                        });
            }

            //如果method为post，将parame中body取出，设置为Rest_Info的body。
            //这里的"$.body"，"$.file"只是parame中的key，就像一种规范，用一个标识符号代表某个数据
            if ("post".equals(rest_info.method.toLowerCase())) {

                //如果body和file都不为空，那就会造成参数错乱,按照执行循序给rest_info.body赋值
                //res_info.body的值每次进行赋值操作都会更新，如果接口增加参数也能通过json模板追加
                if ((Objects.nonNull(parame.get("$.body")) && (Objects.nonNull(parame.get("$.file"))))) {

                    //如果两种都要兼容，参考这里写法
                   /* Map<String, Object> bodyByMap =  JSONObject.parseObject(parame.get("$.body"));
                    String path = parame.get("$.file").toString();
                    parame.remove("$.file");
                    parame.remove("$.body");
                    Map<String,Object> b = JSONObject.parseObject(getTamplateFromJson(path, parame));
                    b.entrySet().forEach(
                            e -> bodyByMap.put(e.getKey(),e.getValue())
                    );
                    rest_info.body = JSON.toJSONString(bodyByMap);*/

                    logger.warn("请选择body，或json模板方式传参");

                    return null;
                }

                //如果body不为空则设置为rest_info.body
                if (Objects.nonNull(parame.get("$.body"))) {
                    rest_info.body = parame.get("$.body").toString();

                    logger.info("当前参数获取方式：从parame中取出body");
                }
                //如果file不为空，则使用json模板
                if (Objects.nonNull(parame.get("$.file"))) {
                    String jsonTamplatePth = parame.get("$.file").toString();
                    parame.remove("$.file");
                    rest_info.body = getTamplateFromJson(jsonTamplatePth, parame);

                    logger.info("当前参数获取方式：Json模板");
                }
            }

            //返回的rest_info对象，这里已经替换过query或body
            return rest_info;
        }

        logger.warn("Path：" + (Objects.nonNull(path) ? path : "Null"));
        logger.warn("Parame：" + (Objects.nonNull(parame) ? JSON.toJSONString(parame) : "Null"));

        return null;
    }


    /**
     * @param path yaml文件路径
     * @param type 0：代表写入，1：代表读取
     * @param obj  接收或写入的对象
     * @return
     */
    public static Object YamlFactoryIsReadYamlAndWriterYaml(String path, String type, Object obj) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        if (Objects.nonNull(type)) {
            logger.info("当前操作状态：" + (type.equals("0") ? "写入" : "读取"));
            if ("0".equals(type)) {
                writerYaml(objectMapper, path, obj);
            }
            if ("1".equals(type)) {
                //返回读取的yaml数据，序列化成对象
                return readYamlValue(objectMapper, path, obj);
            }
        }
        logger.warn("当前Type为空，请传Type值");
        return null;
    }

    /**
     * 读取yaml文件数据
     *
     * @param objectMapper
     * @param path         resources下文件路径
     * @param obj          读取yaml数据的对象实体
     * @return
     */
    public static Object readYamlValue(ObjectMapper objectMapper, String path, Object obj) {
        if (Objects.nonNull(path)) {
            try {
                return objectMapper.readValue(Super_Utils.class.getResourceAsStream(path), obj.getClass());
            } catch (IOException e) {
                logger.error("文件读取异常，请检查当前读取路径：" + path);
                logger.error("报错内容：" + e);
            }
        }
        logger.warn("当前读取Yaml路径为空");
        return null;
    }

    /**
     * 传一个对象，写入内容到yaml
     *
     * @param objectMapper
     * @param path         resources下文件路径
     * @param obj          写入内容的对象
     */
    public static void writerYaml(ObjectMapper objectMapper, String path, Object obj) {
        if (Objects.nonNull(path) && Objects.nonNull(obj)) {
            //定义resource的路径，避免写的时候写入到target/classes下面
            path = RESOURCES_PATH + path;
            //获取一个输出流对象，指定写入文件路径
            try (OutputStream os = new FileOutputStream(new File(path))) {
                //写入对象到yaml文件
                objectMapper.writeValue(os, obj);

                //打印写入yaml的数据
                logger.info("\n写入内容如下：" +
                        "\n***********************************************************************"
                        + "\n" + objectMapper.writeValueAsString(obj)
                        + "\n***********************************************************************");
            } catch (IOException e) {

                logger.error("文件读写异常，请检查路径：" + path);
                logger.info("当前写入内容：" + obj.toString());
                logger.error("报错内容：" + e);
            }
        }
    }
}
