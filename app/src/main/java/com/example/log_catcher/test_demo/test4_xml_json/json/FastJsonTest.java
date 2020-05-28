package com.example.log_catcher.test_demo.test4_xml_json.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.log_catcher.util.LogHelper;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**三种jason解析方法的比较:https://blog.csdn.net/mp624183768/article/details/79714085 <br>
 * Demo例程见:https://www.cnblogs.com/myseries/p/10574184.html<br>
 * 1、JsonObject:<br>
 *    用Android原生技术解析JSON：<br>
 *    特点：很麻烦，对于复杂的json数据解析很容易出错！（不推荐使用）、速度慢<br>
 *2、Gson是Google出品的Json解析函数库，可以将JSON字符串反序列化为对应的Java对象，或者反过来将Java对象序列化为对应的字符串，免去了开发者手动通过JSONObject和JSONArray将Json字段逐个进行解析的烦恼，也减少了出错的可能性，增强了代码的质量。使用gson解析时候，对应的Java实体类无需要使用注解进行标记，支持任意复杂Java对象包括没有源代码的对象。<br>
 *
 * 定义json对应的类的步骤：<br>
 *
 *      先定义成员变量名称<br>
 *      再定义成员变量的类型<br>
 *      再定义泛型的类<br>
 *      从外向内逐渐推进<br>
 *
 *      特点：解析没那么麻烦，代码量简洁，可以很方便地解析复杂的Json数据，而且谷歌官方也推荐使用。<br>
 *      jar包的下载地址：https://mvnrepository.com/artifact/com.google.code.gson/gson<br>
 *      同时Android Studio用开发的话，可以通过Gradle，直接添加依赖，不用下载jar包，特别方便,如下：<br>
 *      implementation group: 'com.google.code.gson', name: 'gson', version: '2.8.0'<br>
 * 3、Fastjson （本类选用）<br>
 *      fastjson是阿里巴巴公司出品的一个java语言编写的高性能且功能完善的JSON函数库，他采用一种“假定有序快速匹配”的算法，把JSON parse的性能提升到极致，号称是目前java语言中最快的JSON库。<br>
 *      Fastjson接口简单易用，已经被广泛使用在缓存序列化，协议交互。Web输出、Android客户端等多种应用场景<br>
 *
 *      特点：用Java语言编写的高性能功能完善的JSON库。它采用了一种“假定有序、快速匹配”的算法<br>
 *      要使用Fastjson，也是想Gson一样，先导入jar包，或者在Gradle中添加依赖：<br>
 *      implementation 'com.alibaba:fastjson:1.1.55.android'<br>
 *
 *   注意：注意要记得创建对象的JavaBean类；要求json对象中的key的名称与Java对象的JavaBean类中的属性名要相同，否则解析不成功！<br>
 */
public class FastJsonTest {

    /**
     *  java对象转 json字符串
     */
      public void objectToJson() {

            // 简单java类转json字符串
            User user = new User("dmego", "123456");
            String UserJson = JSON.toJSONString(user);
            LogHelper.getInstance().w("简单java类转json字符串:" + UserJson);

            // List<Object>转json字符串
            User user1 = new User("zhangsan", "123123");
            User user2 = new User("lisi", "321321");
            List<User> users = new ArrayList<User>();
            users.add(user1);
            users.add(user2);
            String ListUserJson = JSON.toJSONString(users);
            LogHelper.getInstance().w("List<Object>转json字符串:" + ListUserJson);

            // 复杂java类转json字符串
            UserGroup userGroup = new UserGroup("userGroup", users);
            String userGroupJson = JSON.toJSONString(userGroup);
            LogHelper.getInstance().w("复杂java类转json字符串:" + userGroupJson);

        }

        /**
         * json字符串转java对象 注：字符串中使用双引号需要转义 (" --> \"),这里使用的是单引号
         */
    public void JsonToObject() {
           /*
           * json字符串转简单java对象 字符串：{"password":"123456","username":"dmego"}
           */
             String jsonStr1 = "{'password':'123456','username':'dmego'}";
             User user = JSON.parseObject(jsonStr1, User.class);
             LogHelper.getInstance().w("json字符串转简单java对象:" + user.toString());

           /*
           * json字符串转List<Object>对象
           * 字符串：[{"password":"123123","username":"zhangsan"
           * },{"password":"321321","username":"lisi"}]
           */
             String jsonStr2 = "[{'password':'123123','username':'zhangsan'},{'password':'321321','username':'lisi'}]";
             List<User> users = JSON.parseArray(jsonStr2, User.class);
             LogHelper.getInstance().w("json字符串转List<Object>对象:" + users.toString());

           /*
           * json字符串转复杂java对象
           * 字符串：{"name":"userGroup","users":[{"password":"123123"
           * ,"username":"zhangsan"},{"password":"321321","username":"lisi"}]}
           */
             String jsonStr3 = "{'name':'userGroup','users':[{'password':'123123','username':'zhangsan'},{'password':'321321','username':'lisi'}]}";
             UserGroup userGroup = JSON.parseObject(jsonStr3, UserGroup.class);
             LogHelper.getInstance().w("json字符串转复杂java对象:" + userGroup);
    }

    /**
     *
     * @param jasonFile 这里的测试json文件为工程40_LogCatcher_demo\xml_jason_testfile\test_complex.json、
     * @throws IOException
     */
      public void parserJsonTxt(File jasonFile) throws IOException {
            LogHelper.getInstance().w("----------parserJsonTxt in!");
//            ClassLoader cl = this.getClass().getClassLoader();
//            InputStream inputStream = cl.getResourceAsStream("date1.json");
            InputStream inputStream = new FileInputStream(jasonFile);
            String jsontext = IOUtils.toString(inputStream, "utf8");

            JSONObject obj= JSONObject.parseObject(jsontext);//获取jsonobject对象
            LogHelper.getInstance().w("obj:" +obj);
            JSONObject obj1 = obj.getJSONObject("data");
            LogHelper.getInstance().w("obj1:" +obj1);
            JSONArray jsonArray = obj1.getJSONArray("rows");
            LogHelper.getInstance().w("jsonArray:"+jsonArray);
            JSONObject obj2 = jsonArray.getJSONObject(1);
            LogHelper.getInstance().w("obj2:" +obj2);
            for(Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
                    JSONObject jsonObject1 = (JSONObject) iterator.next();
                    LogHelper.getInstance().w(String.valueOf(jsonObject1));
//                    System.out.println(jsonObject1);
                }
            LogHelper.getInstance().w("--------------分隔符-------------");
            Info_util iu = JSON.parseObject(jsontext, Info_util.class);//取得第一层JSONObject
            Info_data_util du = JSON.parseObject(iu.getData(), Info_data_util.class);//取得第二层JSONObject
            List<Info_array_Util> olist = JSON.parseArray(du.getRows(), Info_array_Util.class);//取得第三层JSONArray
            LogHelper.getInstance().w(String.valueOf(iu));
            LogHelper.getInstance().w(String.valueOf(du));
            LogHelper.getInstance().w(String.valueOf(olist));
     }
}
