package top.chengdongqing.common.render;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 图片渲染器
 *
 * @author Luyao
 */
public class ImageRender extends Render {

    /**
     * 图片格式
     */
    private final String format;
    /**
     * 图片二进制数据
     */
    private final byte[] data;
    /**
     * 是否允许缓存
     */
    private final boolean cache;

    public ImageRender(String format, byte[] data, boolean cache) {
        if (StringUtils.isBlank(format) || data == null) {
            throw new IllegalArgumentException("image format and data cannot be null");
        }
        this.format = format;
        this.data = data;
        this.cache = cache;
    }

    public static ImageRender ofPNG(byte[] data) {
        return of("png", data, false);
    }

    public static ImageRender of(String format, byte[] data) {
        return of(format, data, true);
    }

    public static ImageRender of(String format, byte[] data, boolean cache) {
        return new ImageRender(format, data, cache);
    }

    @Override
    public void render() {
        // 定义响应头
        response.setContentType("image/" + format);
        if (!cache) response.setHeader("Cache-Control", "no-store");
        try (OutputStream os = response.getOutputStream()) {
            os.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
