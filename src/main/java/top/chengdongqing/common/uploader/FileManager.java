package top.chengdongqing.common.uploader;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 文件管理器
 *
 * @author Luyao
 */
public interface FileManager {

    /**
     * 获取文件信息
     *
     * @param fileUrl 文件路径
     * @param content 是否包含文件内容
     * @return 文件详情
     * @throws Exception
     */
    File getFile(String fileUrl, boolean content) throws Exception;

    /**
     * 获取文件列表
     *
     * @param path    文件夹路径
     * @param content 是否包含文件内容
     * @return 指定文件夹下的全部文件详情
     * @throws Exception
     */
    List<File> getFiles(FilePath path, boolean content) throws Exception;

    /**
     * 删除文件
     *
     * @param fileUrl 文件路径
     * @throws Exception
     */
    void deleteFile(String fileUrl) throws Exception;

    /**
     * 删除多个文件
     *
     * @param fileUrls 文件路径列表
     * @throws Exception
     */
    void deleteFiles(List<String> fileUrls) throws Exception;

    /**
     * 清空文件夹下的所有文件
     *
     * @param path 文件夹路径
     * @throws Exception
     */
    void clearDirectory(FilePath path) throws Exception;

    /**
     * 获取文件格式
     *
     * @param fileName 文件名
     * @return 文件格式
     */
    static String getFormat(String fileName) {
        if (StringUtils.isBlank(fileName) || !fileName.contains(".")) {
            throw new IllegalArgumentException("The file name is wrong.");
        }
        return fileName.substring(fileName.indexOf(".") + 1);
    }
}
