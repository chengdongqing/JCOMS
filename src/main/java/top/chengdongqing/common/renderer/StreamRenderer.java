package top.chengdongqing.common.renderer;

import javax.servlet.ServletOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 流渲染器
 *
 * @author Luyao
 */
public class StreamRenderer extends Renderer {

    private final InputStream stream;
    private final long length;
    private final String filename;

    public StreamRenderer(InputStream stream, long length, String filename) {
        this.stream = stream;
        this.length = length;
        this.filename = filename;
    }

    public static StreamRenderer of(InputStream stream, long length, String filename) {
        return new StreamRenderer(stream, length, filename);
    }

    @Override
    public void render() {
        // 每次渲染大小
        int bufferSize = 2048;
        byte[] buffer = new byte[bufferSize];
        // 定义响应头
        response.setBufferSize(bufferSize);
        response.setContentLengthLong(length);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "filename=" + filename);
        try (ServletOutputStream os = response.getOutputStream();
             BufferedInputStream bis = new BufferedInputStream(stream)) {
            // 分段输出
            for (int length; (length = bis.read(buffer)) != -1; ) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new RenderException("流渲染异常", e);
        }
    }
}
