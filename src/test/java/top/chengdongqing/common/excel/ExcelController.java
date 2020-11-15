package top.chengdongqing.common.excel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
        LinkedHashMap<String, String> titles = new LinkedHashMap<>() {{
            put("name", "名字");
        }};
        List<JSONObject> data = Stream.of("张三", "王五", "里奇").map(item -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", item);
            return jsonObject;
        }).collect(Collectors.toList());
        JSONArray rows = new JSONArray();
        rows.addAll(data);
        ExcelProcessor.getInstance().write(titles, rows).renderWithDate("用户名称列表");
    }

    @PostMapping("/read")
    @ApiOperation("读取excel文件")
    public String read(@ApiParam("excel文件") @RequestPart MultipartFile file) throws IOException {
        HashMap<String, String> titles = new HashMap<>() {{
            put("名字", "name");
        }};
        ExcelRows rows = ExcelProcessor.getInstance().read(titles, file.getOriginalFilename(), file.getBytes());
        return rows.toJSON();
    }
}
