package com.syshlang.smm.util;


import com.syshlang.smm.util.JsonMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;


/**
 * Created by sunys on 2017/7/5 23:55.
 * Description: Http请求工具类，提供一些http的请求操作
 */
public class HttpClientUtils {
    /** 编码方式 */
    private static final String ENCODING = "UTF-8";
    private final static Log logger = LogFactory.getLog(HttpClientUtils.class);
    private static HttpClientUtils httpClientUtil;

    private HttpClientBuilder clientBuilder;

    private HttpClientUtils() {
        this.clientBuilder = HttpClientBuilder.create();
        clientBuilder.setDefaultRequestConfig(
                RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(15000).build());
    }

    public static HttpClientUtils getInst() {
        try {
            synchronized (HttpClientUtils.class) {
                if (httpClientUtil == null) {
                    httpClientUtil = new HttpClientUtils();
                }
            }
        } catch (Exception e) {
            logger.error("HttpClientUtil getInst is error:", e);
        }
        return httpClientUtil;
    }

    /**
     * 以Post方法获取url对应的数据内容，并以`byte[]`方式返回
     * @param url 请求的地址
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public byte[] post(String url) throws ClientProtocolException, IOException {
        final HttpPost httpPost = new HttpPost(url);

        final HttpClient client = clientBuilder.build();
        final HttpResponse response = client.execute(httpPost);

        final StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() == 200) {
            return EntityUtils.toByteArray(response.getEntity());
        } else {
            throw new ClientProtocolException(statusLine.getReasonPhrase());
        }
    }

    public byte[] httpPost(String url, byte[] requestBytes) {
        if (StringUtils.isBlank(url)) {
            return null;
        }

        byte[] returnBytes = null;
        try {
            final HttpClient client = clientBuilder.build();

            HttpPost httpPost = new HttpPost(url);

            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(requestBytes);
            httpPost.setEntity(byteArrayEntity);

            HttpResponse response = client.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toByteArray(httpEntity);
            }
        } catch (IOException e) {
            logger.error("httpPost is error:", e);
        }
        return returnBytes;
    }

    public byte[] httpPost(String url, List<NameValuePair> params) {
        if (StringUtils.isBlank(url)) {
            return null;
        }

        byte[] returnBytes = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, HttpClientUtils.ENCODING));
            HttpResponse response = this.clientBuilder.build().execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            returnBytes = EntityUtils.toByteArray(httpEntity);
        } catch (IOException e) {
            logger.error("httpPost is error:", e);
        }
        return returnBytes;
    }

    /**
     * 以post方式，并携带`json`为请求体的json格式数据请求url地址对应的数据，将设置header:content-type:
     * application/json
     *
     * @param url 请求地址
     * @param json json格式的请求体内容
     * @return 响应字符串
     * @author bobo
     */
    public String post(String url, String json) {
        final HttpClient client = this.clientBuilder.build();
        final HttpPost post = new HttpPost(url);
        try {
            if (StringUtils.isNotBlank(json)) {
                final ByteArrayEntity s = new ByteArrayEntity(json.getBytes());
                s.setContentEncoding(HttpClientUtils.ENCODING);
                s.setContentType("application/json");
                post.setEntity(s);
            }

            final HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                byte[] bytes = IOUtils.toByteArray(entity.getContent());
                return new String(bytes, HttpClientUtils.ENCODING);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * <p>
     * 将`obj`参数以jackson方式转换成json字符串，设置为请求体，发送post http请求至指定地址；并将返回的结果字符串，
     * 以json的方式转换成`resultType`对应的对象实体，作为返回结果。
     * </p>
     * @param <T> 返回结果的类型
     * @param url 请求地址
     * @param obj 将转换为json字符串，设置请求体的对象
     * @param resultType 返回结果对应的类型
     * @return `resultType`对应的类型实例，或为空
     * @author bobo
     * @throws IOException http操作失败时抛出
     */
    public <T> T postJson(String url, Object obj, Class<T> resultType) throws IOException {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("Invalid arguments 'url'");
        }
        final CloseableHttpClient client = this.clientBuilder.build();
        final HttpPost post = new HttpPost(url);
        try {
            final JsonMapper jsonMapper = JsonMapper.getInstance();
            ByteArrayEntity entity = new ByteArrayEntity(jsonMapper.toJson(obj).getBytes(HttpClientUtils.ENCODING));
            entity.setContentEncoding(HttpClientUtils.ENCODING);
            entity.setContentType("application/json");

            final HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                byte[] bytes = IOUtils.toByteArray(res.getEntity().getContent());
                final String sJson = new String(bytes, HttpClientUtils.ENCODING);
                return jsonMapper.fromJson(sJson, resultType);
            }
        } catch (UnsupportedEncodingException e) {
            // never be throw
        } catch (UnsupportedOperationException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
        return null;
    }

    /**
     * <p>
     * 将`obj`参数以jackson方式转换成json字符串，设置为请求体，发送post http请求至指定地址；并将返回的结果字符串，
     * 以json的方式转换成`resultType`对应的对象实体，作为返回结果。
     * </p>
     * @param <T> 返回结果的类型
     * @param url 请求地址
     * @param obj 将转换为json字符串，设置请求体的对象
     * @param headMap 设置头消息参数
     * @param resultType 返回结果对应的类型
     * @return `resultType`对应的类型实例，或为空
     * @author bobo
     * @throws IOException http操作失败时抛出
     */
    public <T> T postEntity(String url, Object obj, Class<T> resultType, Map<String,String> headMap) throws IOException {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("Invalid arguments 'url'");
        }
        final CloseableHttpClient client = this.clientBuilder.build();
        final HttpPost post = new HttpPost(url);
        try {
            final JsonMapper jsonMapper = JsonMapper.getInstance();
            ByteArrayEntity entity = new ByteArrayEntity(jsonMapper.toJson(obj).getBytes(HttpClientUtils.ENCODING));
            entity.setContentEncoding(HttpClientUtils.ENCODING);
            entity.setContentType("application/json");
            /** 在头消息里设置参数 */
            if(!headMap.isEmpty()) {
                for (String key : headMap.keySet()) {
                    post.setHeader(key, headMap.get(key));
                }
            }
            final HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                byte[] bytes = IOUtils.toByteArray(res.getEntity().getContent());
                final String sJson = new String(bytes, HttpClientUtils.ENCODING);
                return jsonMapper.fromJson(sJson, resultType);
            }
        } catch (UnsupportedEncodingException e) {
            // never be throw
        } catch (UnsupportedOperationException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
        return null;
    }

    /**
     * 以post方式，并携带`json`为请求体的json格式数据请求url地址对应的数据，将设置header:content-type:
     * application/json
     *
     * @param url 请求地址
     * @param json json格式的请求体内容
     * @param headMap 设置头消息参数
     * @return 响应字符串
     * @author bobo
     */
    public String post(String url, String json, Map<String,String> headMap) {
        final HttpClient client = this.clientBuilder.build();
        final HttpPost post = new HttpPost(url);
        try {
            if (StringUtils.isNotBlank(json)) {
                final ByteArrayEntity s = new ByteArrayEntity(json.getBytes());
                s.setContentEncoding(HttpClientUtils.ENCODING);
                s.setContentType("application/json");
                /** 在头消息里设置参数 */
                if(!headMap.isEmpty()) {
                    for (String key : headMap.keySet()) {
                        post.setHeader(key, headMap.get(key));
                    }
                }
                post.setEntity(s);
            }

            final HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                byte[] bytes = IOUtils.toByteArray(entity.getContent());
                return new String(bytes, HttpClientUtils.ENCODING);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}