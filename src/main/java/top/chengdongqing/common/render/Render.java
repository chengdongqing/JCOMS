package top.chengdongqing.common.render;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 渲染器
 *
 * @author Luyao
 */
public abstract class Render {

    protected final HttpServletRequest request;
    protected final HttpServletResponse response;
    protected final Charset charset = StandardCharsets.UTF_8;

    public Render() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        request = attributes.getRequest();
        response = attributes.getResponse();
    }

    /**
     * render to client.
     */
    public abstract void render();
}