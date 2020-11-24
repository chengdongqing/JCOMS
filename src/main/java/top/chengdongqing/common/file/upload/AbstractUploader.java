package top.chengdongqing.common.file.upload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.chengdongqing.common.file.FileManager;
import top.chengdongqing.common.file.FilePath;
import top.chengdongqing.common.file.manager.LocalFileManager;
import top.chengdongqing.common.file.manager.MongoFileManager;
import top.chengdongqing.common.file.manager.OSSFileManager;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;

import java.util.Arrays;

/**
 * 上传器
 * 模板方法模式
 *
 * @author Luyao
 * @see LocalFileManager
 * @see MongoFileManager
 * @see OSSFileManager
 */
@Slf4j
@Component
public abstract class AbstractUploader {

    /**
     * 上传文件
     *
     * @param file    文件对象
     * @param path    存放路径
     * @param formats 支持的格式
     * @param maxSize 允许的大小
     * @return 上传结果
     */
    public Ret<String> upload(MultipartFile file, FilePath path, String[] formats, int maxSize) {
        // 检查文件
        Ret<String> checkResult = check(file, formats, maxSize);
        if (checkResult.isFail()) return checkResult;

        // 生成文件名
        String filename = StrKit.getRandomUUID() + checkResult.data();

        // 执行上传
        try {
            upload(file.getBytes(), path.getPath(), filename);
            return Ret.ok(path.getPath() + filename);
        } catch (Exception e) {
            log.error("文件上传错误", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 具体上传处理
     *
     * @param fileBytes 文件字节数组
     * @param path      存放路径
     * @param filename  文件名
     */
    protected abstract void upload(byte[] fileBytes, String path, String filename) throws Exception;

    /**
     * 检查文件
     *
     * @param file    文件对象
     * @param formats 支持的格式
     * @param maxSize 允许的大小
     * @return 检查结果，正常将返回文件后缀名
     */
    private Ret<String> check(MultipartFile file, String[] formats, int maxSize) {
        if (file.isEmpty()) throw new IllegalArgumentException("The file is empty.");

        // 检查文件格式
        String format = FileManager.getFormat(file.getName());
        if (Arrays.stream(formats).noneMatch(item -> item.equals(format))) {
            return Ret.fail("不支持的文件格式，支持的格式包括：" + String.join("、", formats));
        }

        // 检查文件大小
        if ((file.getSize() / 1024) > maxSize) {
            return Ret.fail("文件大小超出，最大仅支持上传" + maxSize + "MB");
        }
        return Ret.ok("." + format);
    }
}