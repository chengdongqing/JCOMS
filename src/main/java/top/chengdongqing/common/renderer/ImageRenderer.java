package top.chengdongqing.common.renderer;

import top.chengdongqing.common.constant.media.ImageFormat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * 图片渲染器
 *
 * @author Luyao
 */
public class ImageRenderer extends Renderer {

    private final byte[] data;
    private final ImageFormat format;
    private final boolean allowCache;

    public ImageRenderer(byte[] data, ImageFormat format, boolean allowCache) {
        this.data = Objects.requireNonNull(data);
        this.format = Objects.requireNonNull(format);
        this.allowCache = allowCache;
    }

    public static ImageRenderer ofPNG(byte[] data, boolean allowCache) {
        return of(data, ImageFormat.PNG, allowCache);
    }

    public static ImageRenderer of(byte[] data, ImageFormat format) {
        return of(data, format, true);
    }

    public static ImageRenderer of(byte[] data, ImageFormat format, boolean allowCache) {
        return new ImageRenderer(data, format, allowCache);
    }

    @Override
    public void render() {
        // 定义响应头
        response.setContentType("image/" + format);
        if (!allowCache) response.setHeader("Cache-Control", "no-cache");
        try (OutputStream os = response.getOutputStream()) {
            os.write(data);
        } catch (IOException e) {
            throw new RenderException("图片渲染异常", e);
        }
    }
}
