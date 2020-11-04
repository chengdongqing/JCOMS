package top.chengdongqing.common.uploader;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;

import java.util.Arrays;

/**
 * 上传器
 * 模板方法模式
 *
 * @author Luyao
 */
@Slf4j
@Component
public abstract class Uploader {

    @Autowired
    private FileConstants constants;

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @param path 存放路径
     * @return 上传结果
     */
    public Ret uploadImage(MultipartFile file, FilePath path) {
        return upload(file, path, constants.getImageFormats(), constants.getImageMaxSize());
    }

    /**
     * 上传视频
     *
     * @param file 视频文件
     * @param path 存放路径
     * @return 上传结果
     */
    public Ret uploadVideo(MultipartFile file, FilePath path) {
        return upload(file, path, constants.getVideoFormats(), constants.getVideoMaxSize());
    }

    /**
     * 上传文件
     *
     * @param file    文件对象
     * @param path    存放路径
     * @param formats 支持的格式
     * @param maxSize 允许的大小
     * @return 上传结果
     */
    private Ret upload(MultipartFile file, FilePath path, String[] formats, int maxSize) {
        // 检查文件
        Ret checkResult = check(file, formats, maxSize);
        if (checkResult.isFail()) return checkResult;

        // 生成文件名
        String fileName = StrKit.getRandomUUID() + checkResult.getData();

        // 执行上传
        try {
            upload(file.getBytes(), path.getPath(), fileName);
            return Ret.ok(path.getPath() + fileName);
        } catch (Exception e) {
            log.error("文件上传错误", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 具体上传处理
     * 可异步执行
     *
     * @param fileBytes 文件字节数组
     * @param path      存放路径
     * @param fileName  文件名
     */
    abstract void upload(byte[] fileBytes, String path, String fileName) throws Exception;

    /**
     * 检查文件
     *
     * @param file    文件对象
     * @param formats 支持的格式
     * @param maxSize 允许的大小
     * @return 检查结果，如果没问题将文件后缀名返回
     */
    private Ret check(MultipartFile file, String[] formats, int maxSize) {
        // 检查文件是否存在
        if (file.isEmpty()) throw new IllegalArgumentException("The file is empty.");

        // 检查文件格式
        String format = getFormat(file.getOriginalFilename());
        if (Arrays.stream(formats).noneMatch(item -> item.equals(format))) {
            return Ret.fail("不支持的文件格式，支持的格式包括：" + String.join("、", formats));
        }

        // 检查文件大小
        if ((file.getSize() / 1024 / 1024) > maxSize) {
            return Ret.fail("文件大小超出，最大仅支持上传" + maxSize + "MB");
        }
        return Ret.ok("." + format);
    }

    /**
     * 获取文件格式
     *
     * @param fileName 文件名
     * @return 文件格式
     */
    private String getFormat(String fileName) {
        if (StringUtils.isBlank(fileName) || !fileName.contains(".")) {
            throw new IllegalArgumentException("The file name is wrong.");
        }
        return fileName.substring(fileName.indexOf(".") + 1);
    }
}

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "upload")
class FileConstants {

    private String[] imageFormats;
    private Integer imageMaxSize;
    private String[] videoFormats;
    private Integer videoMaxSize;
}