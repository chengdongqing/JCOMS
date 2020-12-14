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
 * <p>HTTP utility functions</p>
 * <p>By default request method, {@code GET}, {@code POST}, {@code PUT}, {@code DELETE} are supported</p>
 * <p>Support custom SSL certificate</p>
 * <p>Support custom request header, URL query string, request body</p>
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
     * Sends a {@code GET} request
     *
     * @param url     the URL to send
     * @param params  the query string key-value mappings
     * @param headers the request header key-value mappings
     * @return the response entity of this request
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
     * Sends a {@code POST} request
     *
     * @param url        the URL to send
     * @param params     the query string key-value mappings
     * @param headers    the request header key-value mappings
     * @param data       the request body
     * @param certStream the custom SSL certificate file stream
     * @param certPwd    the password of the SSL certificate
     * @return the response entity of this request
     */
    public static HttpResponse<String> post(String url, Kv<String, String> params, Kv<String, String> headers,
                                            String data, InputStream certStream, String certPwd) {
        return send(HttpMethod.POST, url, params, headers, data, certStream, certPwd);
    }

    /**
     * Sends a {@code DELETE} request
     *
     * @param url     the URL to send
     * @param params  the query string key-value mappings
     * @param headers the request header key-value mappings
     * @return the response entity of this request
     */
    public static HttpResponse<String> delete(String url, Kv<String, String> params, Kv<String, String> headers,
                                              String data) {
        return send(HttpMethod.DELETE, url, params, headers, data, null, null);
    }

    /**
     * Sends a {@code PUT} request
     *
     * @param url     the URL to send
     * @param params  the query string key-value mappings
     * @param headers the request header key-value mappings
     * @return the response entity of this request
     */
    public static HttpResponse<String> put(String url, Kv<String, String> params, Kv<String, String> headers,
                                           String data) {
        return send(HttpMethod.PUT, url, params, headers, data, null, null);
    }

    /**
     * Sends HTTP request
     *
     * @param method     the method for send
     * @param url        the URL to send
     * @param params     the query string key-value mappings
     * @param headers    the request header key-value mappings
     * @param data       the request body
     * @param certStream the custom SSL certificate file stream
     * @param certPwd    the password of the SSL certificate
     * @return the response entity of this request
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
     * Builds SSL context
     *
     * @param certStream the SSL certificate file stream
     * @param certPwd    the password of the SSL certificate
     * @return the SSL context
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
     * Builds complete url string by the base url and the query string
     *
     * @param url    the base url
     * @param params the key-value mappings for query string
     * @return the complete url string
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
     * Reads request body as string from {@link HttpServletRequest}
     *
     * @param request the request context
     * @return the request body string
     */
    public static String readData(HttpServletRequest request) {
        try {
            return request.getReader().lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Builds header with JSON</p>
     * <p>For request body and response body, the format both are {@code JSON}</p>
     *
     * @return the header with JSON
     */
    public static Kv<String, String> buildHeaderWithJSON() {
        String type = "application/json";
        return Kv.of("Content-Type", type).add("Accept", type);
    }
}
