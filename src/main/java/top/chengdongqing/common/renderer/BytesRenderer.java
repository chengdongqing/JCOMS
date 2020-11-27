package top.chengdongqing.common.renderer;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * 字节数组渲染器
 *
 * @author Luyao
 */
public class BytesRenderer extends Renderer {

    private final byte[] data;
    private final String filename;

    public BytesRenderer(byte[] data, String filename) {
        if (StringUtils.isBlank(filename)) {
            throw new IllegalArgumentException("The filename cannot be blank.");
        }

        this.filename = URLEncoder.encode(filename, CHARSET);
        this.data = Objects.requireNonNull(data);
    }

    public static Renderer of(byte[] data, String name) {
        return new BytesRenderer(data, name);
    }

    @Override
    public void render() {
        // 每次渲染大小
        int bufferSize = 2048;
        byte[] buffer = new byte[bufferSize];
        // 定义响应头
        response.setBufferSize(bufferSize);
        response.setContentLength(data.length);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "filename=" + filename);
        try (OutputStream os = response.getOutputStream();
             BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(data))) {
            // 分段输出
            for (int length; (length = bis.read(buffer)) != -1; ) {
                os.write(buffer, 0, length);
            }
        } catch (Exception e) {
            throw new RenderException("文件渲染异常", e);
        }
    }
}
