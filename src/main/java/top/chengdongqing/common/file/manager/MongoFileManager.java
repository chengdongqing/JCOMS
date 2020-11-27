package top.chengdongqing.common.file.manager;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.file.FileException;
import top.chengdongqing.common.file.FileManager;
import top.chengdongqing.common.file.entity.DownloadFile;
import top.chengdongqing.common.file.uploader.AbstractUploader;

import java.io.InputStream;
import java.util.Objects;

/**
 * MongoDB GridFs文件管理器
 *
 * @author Luyao
 */
@Slf4j
@Component
public class MongoFileManager extends AbstractUploader implements FileManager {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    private static final String QUERY_KEY = "filename";

    @Override
    protected void upload(InputStream fileStream, String fileKey) {
        try {
            gridFsTemplate.store(fileStream, fileKey);
        } catch (Exception e) {
            log.error("文件上传到GridFS异常", e);
            throw new FileException();
        }
    }

    @Override
    public DownloadFile download(String fileKey) throws FileException {
        try {
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where(QUERY_KEY).is(fileKey)));
            InputStream content = gridFsTemplate.getResource(Objects.requireNonNull(gridFSFile)).getContent();
            return DownloadFile.builder()
                    .length(gridFSFile.getLength())
                    .content(content)
                    .build();
        } catch (Exception e) {
            log.error("从GridFS下载文件异常", e);
            throw new FileException();
        }
    }

    @Override
    public void delete(String fileKey) throws FileException {
        try {
            gridFsTemplate.delete(Query.query(Criteria.where(QUERY_KEY).is(fileKey)));
        } catch (Exception e) {
            log.error("从GridFS删除文件异常", e);
            throw new FileException();
        }
    }
}
