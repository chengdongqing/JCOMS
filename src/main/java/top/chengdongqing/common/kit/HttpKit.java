package top.chengdongqing.common.kit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Map;

/**
 * HTTP工具类
 *
 * @author Luyao
 */
public class HttpKit {

    public static HttpResponse<String> get(String url) {
        return get(url, null);
    }

    public static HttpResponse<String> get(String url, Map<String, String> queryParams) {
        return get(url, queryParams, null);
    }

    /**
     * 发送GET请求
     *
     * @param url         请求地址
     * @param queryParams 查询参数对
     * @param headers     请求头
     * @return 响应结果
     */
    public static HttpResponse<String> get(String url, Map<String, String> queryParams, Map<String, String> headers) {
        return send(HttpMethod.GET, url, queryParams, headers, null, null, null);
    }

    public static HttpResponse<String> post(String url, String data) {
        return post(url, null, data);
    }

    public static HttpResponse<String> post(String url, Map<String, String> headers, String data) {
        return post(url, null, headers, data);
    }

    public static HttpResponse<String> post(String url, Map<String, String> queryParams, Map<String, String> headers, String data) {
        return post(url, queryParams, headers, data, null, null);
    }

    public static HttpResponse<String> post(String url, String data, byte[] certBytes, String certPwd) {
        return post(url, null, null, data, certBytes, certPwd);
    }

    /**
     * 发送POST请求
     *
     * @param url         请求地址
     * @param queryParams 查询参数对
     * @param headers     请求头
     * @param data        请求体
     * @param certBytes   证书数据
     * @param certPwd     证书密码
     * @return 响应结果
     */
    public static HttpResponse<String> post(String url, Map<String, String> queryParams, Map<String, String> headers,
                                            String data, byte[] certBytes, String certPwd) {
        return send(HttpMethod.POST, url, queryParams, headers, data, certBytes, certPwd);
    }

    /**
     * 发送DELETE请求
     *
     * @param url         请求地址
     * @param queryParams 查询参数对
     * @param headers     请求头
     * @param data        请求体
     * @return 响应结果
     */
    public static HttpResponse<String> delete(String url, Map<String, String> queryParams, Map<String, String> headers,
                                              String data) {
        return send(HttpMethod.DELETE, url, queryParams, headers, data, null, null);
    }

    /**
     * 发送PUT请求
     *
     * @param url         请求地址
     * @param queryParams 查询参数对
     * @param headers     请求头
     * @param data        请求体
     * @return 响应结果
     */
    public static HttpResponse<String> put(String url, Map<String, String> queryParams, Map<String, String> headers,
                                           String data) {
        return send(HttpMethod.PUT, url, queryParams, headers, data, null, null);
    }

    /**
     * 发送HTTP请求
     *
     * @param method      请求方式
     * @param url         请求地址
     * @param queryParams 跟随请求地址后的参数对
     * @param headers     请求头
     * @param data        请求体
     * @param certBytes   证书数据
     * @param certPwd     证书密码
     * @return 响应结果
     */
    public static HttpResponse<String> send(HttpMethod method, String url, Map<String, String> queryParams,
                                            Map<String, String> headers, String data,
                                            byte[] certBytes, String certPwd) {
        try {
            // 构建HTTP客户端
            HttpClient.Builder clientBuilder = HttpClient.newBuilder();
            if (certBytes != null && StringUtils.isNotBlank(certPwd)) {
                clientBuilder.sslContext(buildSSLContext(certBytes, certPwd));
            }
            HttpClient client = clientBuilder.build();

            // 构建HTTP请求
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(buildUrlWithQueryStr(url, queryParams)))
                    .method(method.name(), data == null ?
                            HttpRequest.BodyPublishers.noBody() :
                            HttpRequest.BodyPublishers.ofString(data)
                    );
            if (headers != null && !headers.isEmpty()) headers.forEach(requestBuilder::header);
            HttpRequest httpRequest = requestBuilder.build();

            // 发送请求
            return client.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建SSL上下文
     *
     * @param certBytes 证书数据
     * @param certPwd   证书密码
     * @return SSL上下文
     * @throws Exception
     */
    private static SSLContext buildSSLContext(byte[] certBytes, String certPwd) throws Exception {
        // 将证书密码转为字符数组
        char[] password = certPwd.toCharArray();

        // 读取证书流
        try (ByteArrayInputStream certStream = new ByteArrayInputStream(certBytes)) {
            // 构建ssl上下文
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(certStream, password);
            keyManagerFactory.init(keyStore, password);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), SecureRandom.getInstanceStrong());
            return sslContext;
        }
    }

    /**
     * 给访问路径拼接参数
     */
    private static String buildUrlWithQueryStr(String url, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }

        StringBuilder sb = new StringBuilder(url);
        // 若已有参数则和之前的参数合并
        sb.append(url.indexOf('?') == -1 ? '?' : '&');
        // 循环拼接参数
        params.forEach((key, value) -> {
            if (StringUtils.isNotBlank(value)) {
                value = URLEncoder.encode(value, StandardCharsets.UTF_8);
                sb.append(key).append('=').append(value).append('&');
            }
        });
        // 去掉最后的&
        return sb.substring(0, sb.length() - 1);
    }
}
