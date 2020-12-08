package top.chengdongqing.common.excel;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author Luyao
 */
@RestController
@RequestMapping("/excel")
@Api(tags = "excel处理相关控制器")
public class ExcelController {

    @GetMapping("/generate")
    @ApiOperation("生成excel")
    public void generate(@ApiParam("用户姓名列表，英文逗号分隔") @RequestParam String[] usernames) {
        String[][] titles = {{"name", "名字"}};
        JSONArray rows = new JSONArray();
        rows.addAll(Arrays.asList(usernames));
        ExcelProcessor.newInstance().generate(titles, rows).renderWithDate("用户姓名列表");
    }

    @PostMapping("/parse")
    @ApiOperation("解析excel")
    public String parse(@ApiParam("excel文件") @RequestPart MultipartFile file) throws IOException {
        try (InputStream stream = file.getInputStream()) {
            return ExcelProcessor.newInstance().parse(null, file.getOriginalFilename(), stream).toJSON();
        }
    }
}
