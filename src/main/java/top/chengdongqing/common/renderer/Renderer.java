package top.chengdongqing.common.renderer;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 渲染器
 *
 * @author Luyao
 * @see BytesRenderer
 * @see ImageRenderer
 * @see StreamRenderer
 */
public abstract class Renderer {

    protected final HttpServletRequest request;
    protected final HttpServletResponse response;

    protected static final Charset CHARSET = StandardCharsets.UTF_8;

    public Renderer() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(attributes);
        request = attributes.getRequest();
        response = attributes.getResponse();
    }

    /**
     * 渲染到客户端
     */
    public abstract void render();
}