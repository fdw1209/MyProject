package com.ch.sockettest.wifi;

/**
 * 工具包
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 和Htttp相关的类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings({ "deprecation", "unused" })
public class HttpUtils {
    private static final int HTTP_STATUS_OK = 200;

    /**
     * 通过post协议发送请求，并获取返回的响应结果
     * 
     * @param url
     *            请求url
     * @param params
     *            post传递的参数
     * @param encoding
     *            编码格式
     * @return 返回服务器响应结果
     * @throws HttpException
     */
	@SuppressWarnings("resource")
	public static String sendPostMethod(String url, Map<String, String> params,
            String encoding) throws Exception {
        String result = "";

		HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // 封装表单
        if (null != params && !params.isEmpty()) {
            List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue().toString();
                BasicNameValuePair pair = new BasicNameValuePair(name, value);
                parameters.add(pair);
            }

            try {
                // 此处为了避免中文乱码，保险起见要加上编码格式
                UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(
                        parameters, encoding);
                post.setEntity(encodedFormEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            HttpResponse response = client.execute(post);
            if (HTTP_STATUS_OK == response.getStatusLine().getStatusCode()) {
                // 获取服务器请求的返回结果，注意此处为了保险要加上编码格式
                result = EntityUtils.toString(response.getEntity(), encoding);
            } else {
                throw new Exception("Invalide response from API"
                        + response.toString());
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过get方式发送请求，并返回响应结果
     * 
     * @param url
     *            请求地址
     * @param params
     *            参数列表，例如name=小明&age=8里面的中文要经过Uri.encode编码
     * @param encoding
     *            编码格式
     * @return 服务器响应结果
     * @throws Exception
     */
	@SuppressWarnings("resource")
	public static String sendGetMethod(String url, String params,
            String encoding) throws Exception {
        String result = "";
        url += ((-1 == url.indexOf("?")) ? "?" : "&") + params;

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        get.setHeader("charset", encoding);

        try {
            HttpResponse response = client.execute(get);
            if (HTTP_STATUS_OK == response.getStatusLine().getStatusCode()) {
                result = EntityUtils.toString(response.getEntity(), encoding);
            } else {
                throw new Exception("Invalide response from Api!"
                        + response.toString());
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过URLConnect的方式发送post请求，并返回响应结果
     * 
     * @param url
     *            请求地址
     * @param params
     *            参数列表，例如name=小明&age=8里面的中文要经过Uri.encode编码
     * @param encoding
     *            编码格式
     * @return 服务器响应结果
     */
    public static String sendPostMethod(String url, String params,
            String encoding) {
        String result = "";
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            URL realUrl = new URL(url);
            // 打开url连接
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            // 5秒后超时
            conn.setConnectTimeout(5000);

            // 设置通用的属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1");

            // post请求必须有下面两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // post请求不应该使用cache
            conn.setUseCaches(false);

            //显式地设置为POST，默认为GET
            conn.setRequestMethod("POST");
            // 获取Urlconnection对象的输出流，调用conn.getOutputStream的时候就会设置为POST方法
            out = new PrintWriter(conn.getOutputStream());
            // 发送参数
            out.print(params);
            // flush输出流的缓冲，这样参数才能发送出去
            out.flush();

            // 读取流里的内容，注意编码问题
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), encoding));

            String line = "";
            while (null != (line = in.readLine())) {
                result += line;
            }

        } catch (IOException e) {
            System.out.println("Send post Exection!");
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                if (null != out) {
                    out.close();
                }
                if (null != in) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 采用URLConnection的方式发送get请求
     * 
     * @param url
     *            请求地址
     * @param params
     *            参数列表，例如name=小明&age=8里面的中文要经过Uri.encode编码
     * @param encoding
     *            编码格式
     * @return 服务器响应结果
     */
    public static String sendGetRequest(String url, String params,
            String encoding) {
        String result = "";
        BufferedReader in = null;

        // 连接上参数
        url += ((-1 == url.indexOf("?")) ? "?" : "&") + params;

        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();

            // 通用设置
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (comptibal; MSIE 6.0; Windows NT 5.1;SV1 )");

            // 不使用缓存
            conn.setUseCaches(false);

            // 建立链接
            conn.connect();

            // 获取所有头字段
            Map<String, List<String>> headers = conn.getHeaderFields();
            for (String key : headers.keySet()) {
                List<String> value = headers.get(key);
            }

            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while (null != (line = in.readLine())) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * 设置代理
     * 
     * @param ip
     *            代理ip
     * @param port
     *            代理端口号
     */
    public static void setProxy(String ip, String port) {
        // 如果不设置，只要代理IP和代理端口正确,此项不设置也可以
        System.getProperties().setProperty("http.proxyHost", ip);
        System.getProperties().setProperty("http.proxyPort", port);
    }
}
