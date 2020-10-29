package top.chengdongqing.common.render;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.net.URLEncoder;

/**
 * 流渲染器
 *
 * @author Luyao
 */
public class StreamRender extends Render {

    private final String name;
    private final byte[] data;

    public StreamRender(String name, byte[] data) {
        this.name = URLEncoder.encode(name, charset);
        this.data = data;
    }

    /**
     * 工厂方法创建当前对象
     */
    public static Render init(String name, byte[] data) {
        return new StreamRender(name, data);
    }

    @Override
    public void render() {
        // 定义响应头，打开方式为附件下载，并设置文件名
        response.setHeader("Content-disposition", "filename=" + name);
        // 每次读取的大小
        byte[] buffer = new byte[2048];
        // 使用带缓冲的输入和输出流
        try (BufferedOutputStream os = new BufferedOutputStream(response.getOutputStream());
             BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(data))) {
            // 用length接收的目的：最后一次读取的长度可能不够buffer的长度，导致最后一次写入时出错
            for (int length; (length = is.read(buffer)) != -1; ) {
                os.write(buffer, 0, length);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
