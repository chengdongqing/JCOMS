package top.chengdongqing.common.excel;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.chengdongqing.common.kit.Lkv;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Luyao
 */
@RestController
@RequestMapping("/excel")
@Api(tags = "excel处理相关控制器")
public class ExcelController {

    @GetMapping("/render")
    @ApiOperation("输出excel文件")
    public void render(@ApiParam("用户姓名列表，逗号分隔") @RequestParam String[] usernames) {
        Lkv<String, String> titles = Lkv.of("name", "名字");
        JSONArray rows = new JSONArray();
        rows.addAll(Arrays.asList(usernames));
        ExcelProcessor.getInstance().write(titles, rows).renderWithDate("用户姓名列表");
    }

    @PostMapping("/read")
    @ApiOperation("读取excel文件")
    public String read(@ApiParam("excel文件") @RequestPart MultipartFile file) throws IOException {
        ExcelRows rows = ExcelProcessor.getInstance().read(null, file.getOriginalFilename(), file.getBytes());
        return rows.toJSON();
    }
}
