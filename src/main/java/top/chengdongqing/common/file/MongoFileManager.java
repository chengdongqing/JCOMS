package top.chengdongqing.common.file;

import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;

/**
 * MongoDB GridFs文件管理器
 *
 * @author Luyao
 */
@Component
public class MongoFileManager extends AbstractUploader implements FileManager {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    private static final String QUERY_KEY = "filename";

    @Override
    protected void upload(byte[] fileBytes, String path, String fileName) throws Exception {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            gridFsTemplate.store(inputStream, path + fileName);
        }
    }

    @Override
    public File getFile(String fileUrl, boolean content) throws Exception {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where(QUERY_KEY).is(fileUrl)));
        // 获取文件详情
        File file = File.builder()
                .path(fileUrl)
                .name(FileManager.getName(fileUrl))
                .format(FileManager.getFormat(fileUrl))
                .size(gridFSFile.getLength())
                .uploadTime(gridFSFile.getUploadDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .isDirectory(false)
                .build();

        // 获取文件内容
        if (content) {
            GridFsResource gridFsResource = gridFsTemplate.getResource(gridFSFile);
            try (InputStream stream = new BufferedInputStream(gridFsResource.getInputStream())) {
                file.setBytes(stream.readAllBytes());
            }
        }
        return file;
    }

    @Override
    public List<File> getFiles(FilePath path, boolean content) throws Exception {
        GridFSFindIterable gridFSFiles = gridFsTemplate.find(Query.query(Criteria.where(QUERY_KEY).alike(Example.of(path.getPath()))));
        LinkedList<File> files = new LinkedList<>();
        for (GridFSFile gridFSFile : gridFSFiles) {
            files.add(getFile(gridFSFile.getFilename(), content));
        }
        return files;
    }

    @Override
    public void deleteFile(String fileUrl) throws Exception {
        gridFsTemplate.delete(Query.query(Criteria.where(QUERY_KEY).is(fileUrl)));
    }

    @Override
    public void deleteFiles(List<String> fileUrls) throws Exception {
        gridFsTemplate.delete(Query.query(Criteria.where(QUERY_KEY).in(fileUrls)));
    }

    @Override
    public void clearDirectory(FilePath path) throws Exception {
        gridFsTemplate.delete(Query.query(Criteria.where(QUERY_KEY).alike(Example.of(path.getPath()))));
    }

    @Override
    public void renameFile(String fileUrl, String name) throws Exception {
        throw new IllegalStateException("gridFs still not support to rename file now.");
    }

    @Override
    public void moveFile(String fileUrl, FilePath targetPath) throws Exception {
        throw new IllegalStateException("gridFs still not support to move file now.");
    }

    @Override
    public void moveFiles(List<String> fileUrls, FilePath targetPath) throws Exception {
        throw new IllegalStateException("gridFs still not support to move files now.");
    }
}
