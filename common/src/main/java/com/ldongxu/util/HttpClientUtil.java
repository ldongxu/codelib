package com.ldongxu.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by liudx on 16/1/12.
 */
public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private final static int DUFAULTTIMEOUT = 3000;

    private static RequestConfig defaultReqConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(DUFAULTTIMEOUT) // 获取连接超时时间
            .setConnectTimeout(DUFAULTTIMEOUT)   // 请求超时时间
            .setSocketTimeout(DUFAULTTIMEOUT)    // 响应超时时间
            .build();

    /**
     * 测出超时重试机制为了防止超时不生效而设置
     *  如果直接放回false,不重试
     *  这里会根据情况进行判断是否重试
     */
    private static HttpRequestRetryHandler retry = new HttpRequestRetryHandler() {
        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if (executionCount >= 3) {// 如果已经重试了3次，就放弃
                return false;
            }
            if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                return true;
            }
            if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                return false;
            }
            if (exception instanceof InterruptedIOException) {// 超时
                return true;
            }
            if (exception instanceof UnknownHostException) {// 目标服务器不可达
                return false;
            }
            if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                return false;
            }
            if (exception instanceof SSLException) {// ssl握手异常
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                return true;
            }
            return false;
        }
    };

    /** 全局连接池对象 */
    private static final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

    /**
     * 静态代码块配置连接池信息
     */
    static {

        // 设置最大连接数
        connManager.setMaxTotal(200);
        // 设置每个连接的路由数
        connManager.setDefaultMaxPerRoute(20);

    }

    public enum HTTPMethod {
        GET, POST, PUT, DELETE
    }

    public static String doGet(String url) {
        return doGet(url, null, null);
    }

    public static String doGet(String url, Map<String, Object> params) {
        return doGet(url, null, params);
    }

    public static String doGet(String url, List<Header> headers, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.size() > 0) {
            sb.append("?");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.lastIndexOf("&"));
        }
        try {
            URI uri = new URI(sb.toString());
            return sendRequest(uri, headerListToArr(headers), null, HTTPMethod.GET);
        } catch (URISyntaxException | IOException e) {
            logger.error("HTTP-Get:" + sb.toString(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String doPost(String url, List<Header> headers, String argsJson) {
        try {
            URI uri = new URI(url);
            StringEntity entity = null;
            if (StringUtils.isNotBlank(argsJson)) {
                entity = new StringEntity(argsJson, ContentType.APPLICATION_JSON);
            }
            return sendRequest(uri, headerListToArr(headers), entity, HTTPMethod.POST);
        } catch (URISyntaxException | IOException e) {
            logger.error("HTTP-Post:" + url, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String doPost(String url, String json) {
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Content-Type", "application/json;charset=utf-8"));
        return doPost(url, headers, json);
    }

    public static String doPost(String url, List<NameValuePair> params) {
        try {
            URI uri = new URI(url);
            Header[] headers = {new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")};
            HttpEntity entity = null;
            if (params != null) {
                entity = new UrlEncodedFormEntity(params, "utf-8");
            }
            return sendRequest(uri, headers, entity, HTTPMethod.POST);
        } catch (Exception  e) {
            logger.error("HTTP-Post:" + url, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String doPost(String url, Map params) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        Set<Map.Entry> entrys = params.entrySet();
        Iterator<Map.Entry> it = entrys.iterator();
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            nameValuePairs.add(new BasicNameValuePair(String.valueOf(entry.getKey()), String.valueOf(entry.getValue())));
        }
        return doPost(url, nameValuePairs);
    }

    public static String doPut(String url, List<Header> headers, String argsJson) {
        try {
            URI uri = new URI(url);
            StringEntity entity = null;
            if (StringUtils.isNotBlank(argsJson)) {
                entity = new StringEntity(argsJson, ContentType.APPLICATION_JSON);
            }
            return sendRequest(uri, headerListToArr(headers), entity, HTTPMethod.PUT);
        } catch (URISyntaxException | IOException e) {
            logger.error("HTTP-Put:" + url, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String doDelete(String url, List<Header> headers) {
        try {
            URI uri = new URI(url);
            return sendRequest(uri, headerListToArr(headers), null, HTTPMethod.DELETE);
        } catch (URISyntaxException | IOException e) {
            logger.error("HTTP-Delete:" + url, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private static String sendRequest(URI uri, Header[] headers, HttpEntity data, HTTPMethod method) throws IOException {
        HttpClient httpClient = getHttpClient(uri);
        String responseContent = null;
        HttpResponse httpResponse = null;
        switch (method) {
            case GET:
                HttpGet httpGet = new HttpGet(uri);
                if (null != headers && headers.length > 0)
                    httpGet.setHeaders(headers);
                httpResponse = httpClient.execute(httpGet);
                break;
            case POST:
                HttpPost httpPost = new HttpPost(uri);
                if (null != headers && headers.length > 0)
                    httpPost.setHeaders(headers);
                if (null != data)
                    httpPost.setEntity(data);
                httpResponse = httpClient.execute(httpPost);
                break;
            case PUT:
                HttpPut httpPut = new HttpPut(uri);
                if (null != headers && headers.length > 0)
                    httpPut.setHeaders(headers);
                if (null != data)
                    httpPut.setEntity(data);
                httpResponse = httpClient.execute(httpPut);
                break;
            case DELETE:
                HttpDelete httpDelete = new HttpDelete(uri);
                if (null != headers && headers.length > 0)
                    httpDelete.setHeaders(headers);
                httpResponse = httpClient.execute(httpDelete);
                break;
            default:
                logger.error("方法参数不合法");
                throw new RuntimeException("请求需要合法的http方法类型");
        }
        HttpEntity httpEntity = httpResponse.getEntity();
        if (null != httpEntity) {
            responseContent = EntityUtils.toString(httpEntity, "UTF-8");
            EntityUtils.consume(httpEntity);
        }

        return responseContent;
    }

    private static HttpClient getHttpClient(URI uri) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setDefaultRequestConfig(defaultReqConfig)  // 把请求相关的超时信息设置到连接客户端
                .setRetryHandler(retry)      // 把请求重试设置到连接客户端
                .setConnectionManager(connManager);   // 配置连接池管理对象
        if (!"https".equals(uri.getScheme())) {
            return builder.build();
        } else {
            X509TrustManager xtm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            try {
                SSLContext sct = SSLContext.getInstance("TLS");
                sct.init(null, new TrustManager[]{xtm}, null);
                SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sct);
                return builder.setSSLSocketFactory(sslSocketFactory).build();
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    private static Header[] headerListToArr(List<Header> headerList) {
        Header[] headers = null;
        if (headerList != null && headerList.size() > 0) {
            headers = new Header[headerList.size()];
            headers = headerList.toArray(headers);
        }
        return headers;
    }
}
