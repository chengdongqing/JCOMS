package top.chengdongqing.common.image;

import top.chengdongqing.common.transformer.BytesToStr;

import java.io.InputStream;

/**
 * 图片识别器
 *
 * @author Luyao
 */
public interface ImageReader {

    /**
     * 读取图片内容
     *
     * @param stream 图片数据流
     * @return 图片内容
     */
    BytesToStr read(InputStream stream);
}
