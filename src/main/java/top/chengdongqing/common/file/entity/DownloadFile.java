package top.chengdongqing.common.file.entity;

import java.io.InputStream;

/**
 * 文件下载实体
 *
 * @author Luyao
 */
public record DownloadFile(
        /**
         * 文件流
         */
        InputStream stream,
        /**
         * 文件大小
         */
        long length) {
}
