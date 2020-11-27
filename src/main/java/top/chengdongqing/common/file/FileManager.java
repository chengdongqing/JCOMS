package top.chengdongqing.common.file;

import org.springframework.web.multipart.MultipartFile;
import top.chengdongqing.common.file.entity.DownloadFile;
import top.chengdongqing.common.file.entity.FileMetadata;
import top.chengdongqing.common.file.manager.LocalFileManager;
import top.chengdongqing.common.file.manager.MongoFileManager;
import top.chengdongqing.common.file.manager.OSSFileManager;

/**
 * 文件管理器
 *
 * @author Luyao
 * @see LocalFileManager
 * @see MongoFileManager
 * @see OSSFileManager
 */
public interface FileManager {

    /**
     * 上传文件
     *
     * @param file    文件对象
     * @param type    文件类型
     * @param formats 允许的格式
     * @param maxSize 允许的大小
     * @return 文件元数据
     * @throws FileException 上传异常
     */
    FileMetadata upload(MultipartFile file, FileType type, String[] formats, long maxSize) throws FileException;

    /**
     * 下载文件
     *
     * @param fileKey 文件键名
     * @return 文件流
     * @throws FileException 下载异常
     */
    DownloadFile download(String fileKey) throws FileException;

    /**
     * 删除文件
     *
     * @param fileKey 文件键名
     * @throws FileException 删除异常
     */
    void delete(String fileKey) throws FileException;
}
