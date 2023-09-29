package com.softeem.utils;

import com.aliyun.oss.ClientException;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class SMSUtils {


    /**
     * 发送短信
     *
     * @param phoneNumbers
     * @param param
     * @throws ClientException
     */
    public static void sendShortMessage(String templateCode, String phoneNumbers, String param) throws ClientException {
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "934df8f1ee0b491589d48b025830f269";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phoneNumbers);
        //六位数随机验证码
//        Integer code = ValidateCodeUtils.generateValidateCode(6);
        querys.put("param", "**code**:" + param + ",**minute**:5");
        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", templateCode);//"908e94ccf08b4476ba6c876d13f084ad"
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*public static void main(String[] args) {
        sendShortMessage("908e94ccf08b4476ba6c876d13f084ad", "13970317123", "6688");
    }*/

}
