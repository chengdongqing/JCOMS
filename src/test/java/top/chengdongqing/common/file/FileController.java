package top.chengdongqing.common.file;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.chengdongqing.common.file.entity.DownloadFile;
import top.chengdongqing.common.file.entity.FileMetadata;
import top.chengdongqing.common.file.upload.Uploader;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.renderer.StreamRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Luyao
 */
@RestController
@Api(tags = "文件相关控制器")
@RequestMapping("/file")
public class FileController {

    @Autowired
    private Uploader uploader;
    @Autowired
    private FileManagerFactory managerFactory;

    @PostMapping
    @ApiOperation("上传图片")
    public Ret<FileMetadata> upload(@ApiParam("图片文件") @RequestPart MultipartFile file,
                                    @ApiParam("文件类型") @RequestParam FileType type) {
        try {
            FileMetadata metadata = uploader.uploadImage(file, type);
            return Ret.ok(metadata);
        } catch (FileException e) {
            return Ret.fail(e.getMessage());
        }
    }

    @GetMapping
    @ApiOperation("下载文件")
    public void download(@ApiParam("文件键名") @RequestParam String fileKey,
                         @ApiParam("文件原始名称") @RequestParam String originalName,
                         HttpServletResponse response) throws IOException {
        try {
            DownloadFile file = managerFactory.getManager().download(fileKey);
            StreamRenderer.of(file.getContent(), file.getLength(), originalName).render();
        } catch (FileException e) {
            response.sendError(404);
        }
    }

    @DeleteMapping
    @ApiOperation("删除文件")
    public Ret<Void> delete(@ApiParam("文件键名") @RequestParam String fileKey) {
        try {
            managerFactory.getManager().delete(fileKey);
            return Ret.ok();
        } catch (FileException e) {
            return Ret.fail("文件删除失败");
        }
    }
}
