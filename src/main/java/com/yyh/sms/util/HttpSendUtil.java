package com.yyh.sms.util;

/**
 * Created by guosh on 2017/11/23.
 */

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class HttpSendUtil {
    public static String send(String uri, String account, String pswd, String mobiles, String content, boolean needstatus, String product, String extno)
            throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod();
        try {
            URI base = new URI(uri, false);
            method.setURI(new URI(base, "HttpSendSM", false));
            method.setQueryString(new NameValuePair[]{
                    new NameValuePair("account", account),
                    new NameValuePair("pswd", pswd),
                    new NameValuePair("mobile", mobiles),
                    new NameValuePair("needstatus", String.valueOf(needstatus)),
                    new NameValuePair("msg", content),
                    new NameValuePair("product", product),
                    new NameValuePair("extno", extno)});

            int result = client.executeMethod(method);
            if (result == 200) {
                InputStream in = method.getResponseBodyAsStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                return URLDecoder.decode(baos.toString(), "UTF-8");
            }
            throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
        } finally {
            method.releaseConnection();
        }
    }

    public static String batchSend(String uri, String account, String pswd, String mobiles, String content, boolean needstatus, String product, String extno)
            throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod();
        try {
            URI base = new URI(uri, false);
            method.setURI(new URI(base, "HttpBatchSendSM", false));
            method.setQueryString(new NameValuePair[]{
                    new NameValuePair("account", account),
                    new NameValuePair("pswd", pswd),
                    new NameValuePair("mobile", mobiles),
                    new NameValuePair("needstatus", String.valueOf(needstatus)),
                    new NameValuePair("msg", content),
                    new NameValuePair("product", product),
                    new NameValuePair("sign", "好药e来"),
                    new NameValuePair("extno", extno)});

            int result = client.executeMethod(method);
            if (result == 200) {
                InputStream in = method.getResponseBodyAsStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                return URLDecoder.decode(baos.toString(), "UTF-8");
            }
            throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
        } finally {
            method.releaseConnection();
        }
    }

    public static String sendCR(String uri, String account, String pswd, String mobiles, String content, boolean needstatus, String product, String extno)
            throws Exception {

        // 创建StringBuffer对象用来操作字符串
        StringBuffer sb = new StringBuffer(uri);

        // 向StringBuffer追加用户名
        sb.append("name=" + account);

        sb.append("&pwd=" + pswd);

        // 向StringBuffer追加手机号码
        sb.append("&mobile=" + mobiles);

        // 向StringBuffer追加消息内容转URL标准码
        sb.append("&content=" + URLEncoder.encode(content, "UTF-8"));

        //追加发送时间，可为空，为空为及时发送
        sb.append("&stime=");
        //加签名
        sb.append("&sign=药药好");

        sb.append("&type=pt&extno=");
        // 创建url对象
        URL url = new URL(sb.toString());

        // 打开url连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置url请求方式 ‘get’ 或者 ‘post’
        connection.setRequestMethod("POST");

        // 发送
        InputStream is = url.openStream();

        //转换返回值 ‘0，20140009090990,1，提交成功’ 发送成功
        String returnStr = HttpSendUtil.convertStreamToString(is);
        return returnStr;
    }

    /**
     * 转换返回值类型为UTF-8格式.
     *
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        StringBuilder sb1 = new StringBuilder();
        byte[] bytes = new byte[4096];
        int size = 0;

        try {
            while ((size = is.read(bytes)) > 0) {
                String str = new String(bytes, 0, size, "UTF-8");
                sb1.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb1.toString();
    }

    public static void main(String[] args) {
//        String uri = "http://222.73.117.158/msg/";//应用地址
//        String account = "yaoyaoh";//账号
//        String pswd = "Yyh888888";//密码
        String uri = "http://web.cr6868.com/asmx/smsservice.aspx?";//应用地址
        String account = "杭州药药好";//账号
        String pswd = "9FA6EB4D075A98F84F896CF0D451";//密码

        String mobiles = "13770851917";//手机号码，多个号码使用","分割
        String content = "您的验证码是：12345。请不要把验证码泄露给其他人。如非本人操作，可不用理会！";//短信内容
        try {
//            HttpSender.batchSend(uri, account, pswd, mobiles, content, true, "", "");
            System.out.print(HttpSendUtil.sendCR(uri, account, pswd, mobiles, content, true, "", ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
