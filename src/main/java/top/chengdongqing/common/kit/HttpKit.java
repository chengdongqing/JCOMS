package top.chengdongqing.common.kit;

import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * HTTP工具类
 *
 * @author Luyao
 */
public class HttpKit {

    /**
     * 内容类型
     */
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

    /**
     * 发送GET请求
     */
    public static String get(String url) {
        return get(url, null);
    }

    /**
     * 发送GET请求
     */
    public static String get(String url, Map<String, String> params) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(buildUrlWithParams(url, params)))
                .header("Content-Type", CONTENT_TYPE).GET().build();
        return send(request);
    }

    /**
     * 给访问路径拼接参数
     */
    private static String buildUrlWithParams(String url, Map<String, String> params) {
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

    /**
     * 发送POST请求
     */
    public static String post(String url, String data) {
        return post(url, data, null);
    }

    /**
     * 发送POST请求
     */
    public static String post(String url, String data, Map<String, String> params) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(buildUrlWithParams(url, params)))
                .header("Content-Type", CONTENT_TYPE)
                .POST(data == null ?
                        HttpRequest.BodyPublishers.noBody() :
                        HttpRequest.BodyPublishers.ofString(data, StandardCharsets.UTF_8)
                ).build();
        return send(request);
    }

    /**
     * 发送http请求
     */
    private static String send(HttpRequest request) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApplyAsync(HttpResponse::body).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送带本地证书的POST请求
     */
    public static String postWithCert(String url, String data, File cert, char[] password) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", CONTENT_TYPE)
                .POST(HttpRequest.BodyPublishers.ofString(data, StandardCharsets.UTF_8)).build();

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(cert), password);
            keyManagerFactory.init(keyStore, password);
            sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
            HttpClient client = HttpClient.newBuilder().sslContext(sslContext).build();
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
