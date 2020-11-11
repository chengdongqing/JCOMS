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

    public ImageRender(String format, byte[] data) {
        if (StringUtils.isBlank(format) || data == null) {
            throw new IllegalArgumentException("image format and data cannot be null");
        }
        this.format = format;
        this.data = data;
    }

    public static ImageRender ofPNG(byte[] data) {
        return of("png", data);
    }

    public static ImageRender of(String format, byte[] data) {
        return new ImageRender(format, data);
    }

    @Override
    public void render() {
        // 定义响应头
        response.setDateHeader("Expires", 0);
        response.setContentType("image/" + format);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        try (OutputStream os = response.getOutputStream()) {
            os.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
