package com.ch.sockettest.wifi;

/**
 * ���߰�
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
 * ��Htttp��ص���
 * 
 * @author Administrator
 *
 */
@SuppressWarnings({ "deprecation", "unused" })
public class HttpUtils {
    private static final int HTTP_STATUS_OK = 200;

    /**
     * ͨ��postЭ�鷢�����󣬲���ȡ���ص���Ӧ���
     * 
     * @param url
     *            ����url
     * @param params
     *            post���ݵĲ���
     * @param encoding
     *            �����ʽ
     * @return ���ط�������Ӧ���
     * @throws HttpException
     */
	@SuppressWarnings("resource")
	public static String sendPostMethod(String url, Map<String, String> params,
            String encoding) throws Exception {
        String result = "";

		HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // ��װ��
        if (null != params && !params.isEmpty()) {
            List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue().toString();
                BasicNameValuePair pair = new BasicNameValuePair(name, value);
                parameters.add(pair);
            }

            try {
                // �˴�Ϊ�˱����������룬�������Ҫ���ϱ����ʽ
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
                // ��ȡ����������ķ��ؽ����ע��˴�Ϊ�˱���Ҫ���ϱ����ʽ
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
     * ͨ��get��ʽ�������󣬲�������Ӧ���
     * 
     * @param url
     *            �����ַ
     * @param params
     *            �����б�����name=С��&age=8���������Ҫ����Uri.encode����
     * @param encoding
     *            �����ʽ
     * @return ��������Ӧ���
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
     * ͨ��URLConnect�ķ�ʽ����post���󣬲�������Ӧ���
     * 
     * @param url
     *            �����ַ
     * @param params
     *            �����б�����name=С��&age=8���������Ҫ����Uri.encode����
     * @param encoding
     *            �����ʽ
     * @return ��������Ӧ���
     */
    public static String sendPostMethod(String url, String params,
            String encoding) {
        String result = "";
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            URL realUrl = new URL(url);
            // ��url����
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            // 5���ʱ
            conn.setConnectTimeout(5000);

            // ����ͨ�õ�����
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1");

            // post�����������������
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // post����Ӧ��ʹ��cache
            conn.setUseCaches(false);

            //��ʽ������ΪPOST��Ĭ��ΪGET
            conn.setRequestMethod("POST");
            // ��ȡUrlconnection����������������conn.getOutputStream��ʱ��ͻ�����ΪPOST����
            out = new PrintWriter(conn.getOutputStream());
            // ���Ͳ���
            out.print(params);
            // flush������Ļ��壬�����������ܷ��ͳ�ȥ
            out.flush();

            // ��ȡ��������ݣ�ע���������
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
            // �ر���
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
     * ����URLConnection�ķ�ʽ����get����
     * 
     * @param url
     *            �����ַ
     * @param params
     *            �����б�����name=С��&age=8���������Ҫ����Uri.encode����
     * @param encoding
     *            �����ʽ
     * @return ��������Ӧ���
     */
    public static String sendGetRequest(String url, String params,
            String encoding) {
        String result = "";
        BufferedReader in = null;

        // �����ϲ���
        url += ((-1 == url.indexOf("?")) ? "?" : "&") + params;

        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();

            // ͨ������
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (comptibal; MSIE 6.0; Windows NT 5.1;SV1 )");

            // ��ʹ�û���
            conn.setUseCaches(false);

            // ��������
            conn.connect();

            // ��ȡ����ͷ�ֶ�
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
     * ���ô���
     * 
     * @param ip
     *            ����ip
     * @param port
     *            ����˿ں�
     */
    public static void setProxy(String ip, String port) {
        // ��������ã�ֻҪ����IP�ʹ���˿���ȷ,�������Ҳ����
        System.getProperties().setProperty("http.proxyHost", ip);
        System.getProperties().setProperty("http.proxyPort", port);
    }
}
