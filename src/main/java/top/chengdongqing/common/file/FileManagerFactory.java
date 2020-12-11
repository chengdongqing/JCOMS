package top.chengdongqing.common.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 文件管理器工厂
 *
 * @author Luyao
 */
@Component
public class FileManagerFactory {

    @Value("${file.active}")
    private String active;

    @Autowired
    private ApplicationContext appContext;

    /**
     * 获取文件管理器实例
     *
     * @return 文件管理器实例
     */
    public FileManager getManager() {
        String beanName = Objects.requireNonNull(active) + "FileManager";
        return appContext.getBean(beanName, FileManager.class);
    }
}
