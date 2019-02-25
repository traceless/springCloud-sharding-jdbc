package com.phantoms.framework.cloudbase.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by jie on 16-8-14.
 */
public class HttpClientUtils {

    public static final int    connTimeout = 5000;
    public static final int    readTimeout = 5000;
    public static final String charset     = "UTF-8";
    public static HttpClient   client      = null;
    private static final int   maxTole     = 200;

    static {

        // 需要通过以下代码声明对https连接支持
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslsf)
                .build();

            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            cm.setMaxTotal(maxTole);
            cm.setDefaultMaxPerRoute(maxTole);
            client = HttpClients.custom().setConnectionManager(cm).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public static String postParameters(String url, String parameterStr) throws ConnectTimeoutException,
                                                                        SocketTimeoutException, Exception {
        return post(url, parameterStr, "application/x-www-form-urlencoded", charset, connTimeout, readTimeout);
    }

    public static String postParameters(String url, String parameterStr, String charset, Integer connTimeout,
                                        Integer readTimeout) throws ConnectTimeoutException, SocketTimeoutException,
                                                            Exception {
        return post(url, parameterStr, "application/x-www-form-urlencoded", charset, connTimeout, readTimeout);
    }

    public static String postParameters(String url, Map<String, String> params) throws ConnectTimeoutException,
                                                                               SocketTimeoutException, Exception {
        return postForm(url, params, null, connTimeout, readTimeout);
    }

    public static String postParameters(String url, Map<String, String> params, Integer connTimeout, Integer readTimeout)
                                                                                                                         throws ConnectTimeoutException,
                                                                                                                         SocketTimeoutException,
                                                                                                                         Exception {
        return postForm(url, params, null, connTimeout, readTimeout);
    }

    public static String postParameters(String url, File file, String fileParam) throws ConnectTimeoutException,
                                                                                SocketTimeoutException, Exception {
        return postForm(url, file, fileParam, null, connTimeout, readTimeout);
    }

    public static String get(String url) throws Exception {
        return get(url, charset, null, null);
    }

    public static String get(String url, String charset, int connTimeout) throws Exception {
        return get(url, charset, connTimeout, readTimeout);
    }

    /**
     * 发送一个 Post 请求, 使用指定的字符集编码.
     *
     * @param url
     * @param body RequestBody
     * @param mimeType 例如 application/xml "application/x-www-form-urlencoded"
     * a=1&b=2&c=3
     * @param charset 编码
     * @param connTimeout 建立链接超时时间,毫秒.
     * @param readTimeout 响应超时时间,毫秒.
     * @return ResponseBody, 使用指定的字符集编码.
     * @throws ConnectTimeoutException 建立链接超时异常
     * @throws SocketTimeoutException 响应超时
     * @throws Exception
     */
    public static String post(String url, String body, String mimeType, String charset, Integer connTimeout,
                              Integer readTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {

        HttpPost post = new HttpPost(url);
        String result = "";
        try {
            if (StringUtils.isNotBlank(body)) {
                HttpEntity entity = new StringEntity(body, ContentType.create(mimeType, charset));
                post.setEntity(entity);
            }
            // 设置参数
            RequestConfig.Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            post.setConfig(customReqConf.build());
            HttpResponse res = client.execute(post);
            // result = IOUtils.toString(res.getEntity().getContent(), charset);
            result = EntityUtils.toString(res.getEntity(), charset);
        } finally {
            post.releaseConnection();
            /*
             * if (url.startsWith("https") && client != null&& client instanceof
             * CloseableHttpClient) { ((CloseableHttpClient) client).close(); }
             */
        }
        return result;
    }

    /**
     * 提交form表单
     *
     * @param url
     * @param params
     * @param connTimeout
     * @param readTimeout
     * @return
     * @throws ConnectTimeoutException
     * @throws SocketTimeoutException
     * @throws Exception
     */
    public static String postForm(String url, Map<String, String> params, Map<String, String> headers,
                                  Integer connTimeout, Integer readTimeout) throws ConnectTimeoutException,
                                                                           SocketTimeoutException, Exception {

        HttpPost post = new HttpPost(url);
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();
                Set<Entry<String, String>> entrySet = params.entrySet();
                for (Entry<String, String> entry : entrySet) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
                post.setEntity(entity);
            }
            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置参数
            RequestConfig.Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            post.setConfig(customReqConf.build());
            HttpResponse res = client.execute(post);
            // return IOUtils.toString(res.getEntity().getContent(), "UTF-8");
            return EntityUtils.toString(res.getEntity(), charset);
        } finally {
            post.releaseConnection();
        }
    }

    /**
     * 提交form表单
     *
     * @param url
     * @param
     * @param connTimeout
     * @param readTimeout
     * @return
     * @throws ConnectTimeoutException
     * @throws SocketTimeoutException
     * @throws Exception
     */
    public static String postForm(String url, File file, String fileParam, Map<String, String> headers,
                                  Integer connTimeout, Integer readTimeout) throws ConnectTimeoutException,
                                                                           SocketTimeoutException, Exception {

        HttpPost post = new HttpPost(url);
        try {
            if (file == null) {
                return null;
            }
            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
            reqEntity.addBinaryBody(fileParam, file); // 设置文件
            post.setEntity(reqEntity.build());

            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置参数
            RequestConfig.Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            post.setConfig(customReqConf.build());
            HttpResponse res = client.execute(post);
            // return IOUtils.toString(res.getEntity().getContent(), "UTF-8");
            return EntityUtils.toString(res.getEntity(), charset);
        } finally {
            post.releaseConnection();
        }
    }

    /**
     * 发送一个 GET 请求
     *
     * @param url
     * @param charset
     * @param connTimeout 建立链接超时时间,毫秒.
     * @param readTimeout 响应超时时间,毫秒.
     * @return
     * @throws ConnectTimeoutException 建立链接超时
     * @throws SocketTimeoutException 响应超时
     * @throws Exception
     */
    public static String get(String url, String charset, Integer connTimeout, Integer readTimeout)
                                                                                                  throws ConnectTimeoutException,
                                                                                                  SocketTimeoutException,
                                                                                                  Exception {

        HttpGet get = new HttpGet(url);
        String result = "";
        try {
            // 设置参数
            RequestConfig.Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            get.setConfig(customReqConf.build());
            HttpResponse res = client.execute(get);
            result = EntityUtils.toString(res.getEntity(), charset);
        } finally {
            get.releaseConnection();
        }
        return result;
    }

    /**
     * 下载文件
     * 
     * @param url
     * @param fileType 文件类型， txt jpg 等
     * @return
     * @throws ConnectTimeoutException
     * @throws SocketTimeoutException
     * @throws Exception
     */
    public static File get(String url, String fileType)
            throws ConnectTimeoutException,SocketTimeoutException, Exception {

        HttpGet get = new HttpGet(url);
        File file = File.createTempFile("download_", "."+fileType);
        try {
            // 设置参数
            RequestConfig.Builder customReqConf = RequestConfig.custom();
            customReqConf.setConnectTimeout(connTimeout);
            get.setConfig(customReqConf.build());
            HttpResponse res = client.execute(get);
            InputStream input = res.getEntity().getContent();
        	OutputStream output = new FileOutputStream(file);
        	copyLarge(input, output);
        	output.flush();
        } finally {
            get.releaseConnection();
        }
        return file;
    }

    /**
     * 拷贝字节流
     * TODO Add comments here.
     * @param input
     * @param output
     * @return
     * @throws IOException
     */
    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void main(String[] args) throws Exception {

        String image = "http://10.201.80.212/fss/public/rRJ6HeGv9Y4utXYyJ11EzFDRsSpm0044.jpg";

        String fileType = image.substring(image.lastIndexOf(".") + 1);

        File file = get(image, fileType);
        if (file != null) return;
        String wxurl = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
        Map<String, String> map = new HashMap<String, String>();
        map.put("component_appid", "wx15ee1931e51ab5c8");
        map.put("component_appsecret", "dc37bd5f1063bc3a7f1f352017625ee7");
        map.put("component_verify_ticket",
            "ticket@@@z1QhI-FpvwgRZ7qxqljunEmEl-Y2--BgIwrK_O2cvpWLsPr9qxNV9V6XliAeFpdIf8_NlG4b9t3-3_PvOLNsQw");
        String json = "{}";
        // 获取common_access_token
        // json =
        // HttpClientUtils.postParameters(wxurl,JSONObject.toJSONString(map));

        String common_access_token = JSONObject.parseObject(json).getString("component_access_token");
        wxurl = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="
                + common_access_token;

        // 获取预授权码
        Map<String, String> obj = new HashMap();
        obj.put("component_appid", "wx15ee1931e51ab5c8");

        // json =
        // HttpClientUtils.postParameters(wxurl,JSONObject.toJSONString(obj));

        // json =
        // HttpClientUtils.postParameters(wxurl,JSONObject.toJSONString(obj));

        System.out.println(json);
        String preauth = "preauthcode@@@wUhGlEZEFPZeS_FU5DK4QJC-dhnL8EOulMaQ3v1gX8h4TjHxYUUynyAGX5HW6HKh";

        // 让用户授权
        String callback = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=wx15ee1931e51ab5c8&pre_auth_code=preauthcode@@@wUhGlEZEFPZeS_FU5DK4QJC-dhnL8EOulMaQ3v1gX8h4TjHxYUUynyAGX5HW6HKh&redirect_uri=http://zyjtest.tunnel.qydev.com/platform-web/wxnotice";

        // 获取用户接口调用凭据
        wxurl = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token="
                + common_access_token;
        // json =
        // HttpClientUtils.postParameters(wxurl,"{\"component_appid\":\"wx15ee1931e51ab5c8\",\"authorization_code\": \"queryauthcode@@@yfU5AsFTsnEqsOYkLTyTPm6LATVWvILjQV-0M-6NxIGQqYSe4FnrYF8vpzNOEUdKtT3FcmB-P2lmHysHPY6iOg\"}");

        wxurl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=T5ECoIjQC6r0UnxOvZOdSUz6KXQnJEHjsoi08_vV32ZGavCLL23t_Ap6MpfrBahn3ZZES1OVfOohOyv-PKd5L5HlcsDFzxW_7Hb6UYn6zFDe8rJ71QvgAdbwCkLFX114DUIiAEDGXL&next_openid=";
        // json = HttpClientUtils.get(wxurl);

        System.out.print(json);
    }

}
