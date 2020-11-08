package top.chengdongqing.common.uploader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

/**
 * 上传文件到MongoDB
 *
 * @author Luyao
 */
@Component
public class MongoUploader extends AbstractUploader {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Async
    @Override
    protected void upload(byte[] fileBytes, String path, String fileName) throws Exception {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            gridFsTemplate.store(inputStream, path + fileName);
        }
    }
}
