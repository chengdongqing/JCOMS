package top.chengdongqing.common.kit;

import org.springframework.http.HttpMethod;
import top.chengdongqing.common.string.StrEncodingType;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.stream.Collectors;

/**
 * HTTP工具类
 * 默认支持GET、POST、PUT、DELETE请求
 * 支持本地证书构建SSL上下文
 * 支持自定义请求头、地址栏参数、请求体等
 *
 * @author Luyao
 */
public class HttpKit {

    public static HttpResponse<String> get(String url) {
        return get(url, null);
    }

    public static HttpResponse<String> get(String url, Kv<String, String> params) {
        return get(url, params, null);
    }

    /**
     * 发送GET请求
     *
     * @param url     请求地址
     * @param params  参数键值对
     * @param headers 请求头
     * @return 响应结果
     */
    public static HttpResponse<String> get(String url, Kv<String, String> params, Kv<String, String> headers) {
        return send(HttpMethod.GET, url, params, headers, null, null, null);
    }

    public static HttpResponse<String> post(String url, String data) {
        return post(url, null, data);
    }

    public static HttpResponse<String> post(String url, Kv<String, String> headers, String data) {
        return post(url, null, headers, data);
    }

    public static HttpResponse<String> post(String url, Kv<String, String> params, Kv<String, String> headers, String data) {
        return post(url, params, headers, data, null, null);
    }

    public static HttpResponse<String> post(String url, String data, InputStream certStream, String certPwd) {
        return post(url, null, null, data, certStream, certPwd);
    }

    /**
     * 发送POST请求
     *
     * @param url        请求地址
     * @param params     参数键值对
     * @param headers    请求头
     * @param data       请求体
     * @param certStream 证书数据
     * @param certPwd    证书密码
     * @return 响应结果
     */
    public static HttpResponse<String> post(String url, Kv<String, String> params, Kv<String, String> headers,
                                            String data, InputStream certStream, String certPwd) {
        return send(HttpMethod.POST, url, params, headers, data, certStream, certPwd);
    }

    /**
     * 发送DELETE请求
     *
     * @param url     请求地址
     * @param params  参数键值对
     * @param headers 请求头
     * @param data    请求体
     * @return 响应结果
     */
    public static HttpResponse<String> delete(String url, Kv<String, String> params, Kv<String, String> headers,
                                              String data) {
        return send(HttpMethod.DELETE, url, params, headers, data, null, null);
    }

    /**
     * 发送PUT请求
     *
     * @param url     请求地址
     * @param params  参数键值对
     * @param headers 请求头
     * @param data    请求体
     * @return 响应结果
     */
    public static HttpResponse<String> put(String url, Kv<String, String> params, Kv<String, String> headers,
                                           String data) {
        return send(HttpMethod.PUT, url, params, headers, data, null, null);
    }

    /**
     * 发送HTTP请求
     *
     * @param method     请求方式
     * @param url        请求地址
     * @param params     参数键值对
     * @param headers    请求头
     * @param data       请求体
     * @param certStream 证书数据
     * @param certPwd    证书密码
     * @return 响应结果
     */
    public static HttpResponse<String> send(HttpMethod method, String url, Kv<String, String> params,
                                            Kv<String, String> headers, String data,
                                            InputStream certStream, String certPwd) {
        try {
            // 构建HTTP客户端
            HttpClient.Builder clientBuilder = HttpClient.newBuilder();
            if (certStream != null && StrKit.isNotBlank(certPwd)) {
                clientBuilder.sslContext(buildSSLContext(certStream, certPwd));
            }
            HttpClient client = clientBuilder.build();

            // 构建HTTP请求
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(buildUrlWithParams(url, params)))
                    .method(method.name(), data == null ?
                            HttpRequest.BodyPublishers.noBody() :
                            HttpRequest.BodyPublishers.ofString(data)
                    );
            if (headers != null && !headers.isEmpty()) headers.forEach(requestBuilder::header);
            HttpRequest httpRequest = requestBuilder.build();

            // 发送请求
            return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建SSL上下文
     *
     * @param certStream 证书数据
     * @param certPwd    证书密码
     * @return SSL上下文
     */
    private static SSLContext buildSSLContext(InputStream certStream, String certPwd) throws Exception {
        // 将证书密码字符串转为字符数组
        char[] password = certPwd.toCharArray();
        // 构建ssl上下文
        SSLContext sslContext = SSLContext.getInstance("TLS");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(certStream, password);
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), SecureRandom.getInstanceStrong());
        return sslContext;
    }

    /**
     * 构建带参数的请求地址
     *
     * @param url    请求地址
     * @param params 参数键值对
     * @return 带参数的请求地址
     */
    public static String buildUrlWithParams(String url, Kv<String, String> params) {
        // 没有参数则直接返回请求路径
        if (params == null || params.isEmpty()) return url;
        // 构建带键值对查询字符串的完整请求路径
        StringBuilder sb = new StringBuilder(url);
        sb.append(url.indexOf('?') == -1 ? '?' : '&');
        return sb.append(StrKit.buildQueryStr(params, StrEncodingType.URL)).toString();
    }

    /**
     * 读取请求体数据
     *
     * @param request 请求对象
     * @return 请求体数据
     */
    public static String readData(HttpServletRequest request) {
        try {
            return request.getReader().lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建发送和接受内容均为JSON的请求头
     *
     * @return 包含Content-Type和Accept的请求头
     */
    public static Kv<String, String> buildHeaderWithJSON() {
        String type = "application/json";
        return Kv.of("Content-Type", type).add("Accept", type);
    }
}
