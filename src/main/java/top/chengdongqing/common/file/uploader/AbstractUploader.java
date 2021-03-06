package top.chengdongqing.common.file.uploader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.chengdongqing.common.constant.ErrorMsg;
import top.chengdongqing.common.file.FileException;
import top.chengdongqing.common.file.FileManager;
import top.chengdongqing.common.file.FileType;
import top.chengdongqing.common.file.entity.FileMetadata;
import top.chengdongqing.common.file.manager.LocalFileManager;
import top.chengdongqing.common.file.manager.MongoFileManager;
import top.chengdongqing.common.file.manager.OSSFileManager;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.signature.signer.MessageDigestSigner;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 抽象文件上传器
 *
 * @author Luyao
 * @see LocalFileManager
 * @see MongoFileManager
 * @see OSSFileManager
 */
@Slf4j
@Component
public abstract class AbstractUploader implements FileManager {

    @Override
    public FileMetadata upload(MultipartFile file, FileType type, String[] formats, long maxSize) throws FileException {
        // 检查文件并获取文件格式
        String format = check(file, formats, maxSize);
        // 唯一编号
        String id = StrKit.getRandomUUID();
        // 完整键名
        String key = type.getPathname().concat("/").concat(id);

        try (InputStream fileStream = file.getInputStream()) {
            // 保存文件
            upload(fileStream, key);
            // 构建元数据
            return buildMetadata(file, key, format);
        } catch (IOException e) {
            log.error("文件上传异常", e);
            throw new FileException("文件读取错误");
        } catch (FileException e) {
            throw new FileException(ErrorMsg.UPLOAD_FAILED);
        }
    }

    /**
     * 具体上传处理
     *
     * @param fileStream 文件流
     * @param fileKey    文件键名
     */
    protected abstract void upload(InputStream fileStream, String fileKey) throws FileException;

    /**
     * 构建文件元数据
     *
     * @param file   文件对象
     * @param key    文件键名
     * @param format 文件格式
     * @return 文件元数据
     */
    private FileMetadata buildMetadata(MultipartFile file, String key, String format) throws IOException {
        return FileMetadata.builder()
                .fileKey(key)
                .format(format)
                .length(file.getSize())
                .uploadTime(LocalDateTime.now())
                .filename(file.getOriginalFilename())
                .hash(MessageDigestSigner.signature(SignatureAlgorithm.SHA256, file.getBytes()).toHex())
                .build();
    }

    /**
     * 检查文件
     *
     * @param file    文件对象
     * @param formats 支持的格式
     * @param maxSize 允许的大小
     * @return 文件格式
     */
    private String check(MultipartFile file, String[] formats, long maxSize) {
        if (file.isEmpty()) throw new FileException("文件内容为空");

        // 检查文件格式
        String format = format(file.getOriginalFilename());
        if (Arrays.stream(formats).noneMatch(item -> item.equals(format))) {
            throw new FileException("仅支持%s格式".formatted(String.join("、", formats)));
        }

        // 检查文件大小
        if (file.getSize() > maxSize * 1024 * 1024) {
            throw new FileException("不能超过%dMB".formatted(maxSize));
        }
        return format;
    }

    /**
     * 获取文件格式
     *
     * @param fileName 文件名
     * @return 文件格式
     */
    private String format(String fileName) {
        if (StrKit.isBlank(fileName) || !fileName.contains(".")) {
            throw new IllegalArgumentException("The file name is wrong.");
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}