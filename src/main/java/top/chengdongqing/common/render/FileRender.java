package top.chengdongqing.common.render;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 文件渲染器
 *
 * @author Luyao
 */
public class FileRender extends Render {

    /**
     * 文件名
     */
    private final String filename;
    /**
     * 文件二进制数据
     */
    private final byte[] data;

    public FileRender(String filename, byte[] data) {
        if (StringUtils.isBlank(filename) || data == null) {
            throw new IllegalArgumentException("The filename and data cannot be null");
        }

        this.filename = URLEncoder.encode(filename, charset);
        this.data = data;
    }

    public static Render of(String name, byte[] data) {
        return new FileRender(name, data);
    }

    @Override
    public void render() {
        // 每次缓冲大小
        int bufferSize = 2048;
        byte[] buffer = new byte[bufferSize];
        // 定义响应头
        response.setBufferSize(bufferSize);
        response.setContentLength(data.length);
        response.setHeader("Content-Disposition", "filename=" + filename);
        try (OutputStream os = response.getOutputStream();
             InputStream is = new BufferedInputStream(new ByteArrayInputStream(data))) {
            // 分段输出
            for (int length; (length = is.read(buffer)) != -1; ) {
                os.write(buffer, 0, length);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
