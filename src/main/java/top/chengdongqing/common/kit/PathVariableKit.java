package top.chengdongqing.common.kit;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求路径变量相关工具类
 *
 * @author Luyao
 */
public class PathVariableKit {

    /**
     * 获取路径变量
     *
     * @PathVariable修饰的变量值不能包含斜杠 此方法可获取包含斜杠的值
     * 适用于如文件路径类的接口使用
     * 如访问文件：/file/user/avatar/123.png
     * /file为接口名，后面的为动态值
     * 符合restful风格
     */
    public static String getPathVariable(HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }
}
