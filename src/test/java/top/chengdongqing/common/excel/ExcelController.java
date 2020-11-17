package top.chengdongqing.common.excel;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Lkv;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Luyao
 */
@RestController
@RequestMapping("/excel")
@Api(tags = "excel处理相关控制器")
public class ExcelController {

    @GetMapping("/render")
    @ApiOperation("输出excel文件")
    public void render() {
        Lkv<String, String> titles = Lkv.of("name", "名字");
        List<Kv> data = Stream.of("张三", "王五", "里奇").map(item -> Kv.of("name", item)).collect(Collectors.toList());
        JSONArray rows = new JSONArray();
        rows.addAll(data);
        ExcelProcessor.getInstance().write(titles, rows).renderWithDate("用户名称列表");
    }

    @PostMapping("/read")
    @ApiOperation("读取excel文件")
    public String read(@ApiParam("excel文件") @RequestPart MultipartFile file) throws IOException {
        Kv<String, String> titles = Kv.of("名字", "name");
        ExcelRows rows = ExcelProcessor.getInstance().read(titles, file.getOriginalFilename(), file.getBytes());
        return rows.toJSON();
    }
}
