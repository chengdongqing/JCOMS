package top.chengdongqing.common.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 文件管理器工厂
 *
 * @author Luyao
 */
@Component
@RefreshScope
public class FileManagerFactory extends ApplicationObjectSupport {

    @Value("${file.active}")
    private String active;

    /**
     * 获取文件管理器实例
     *
     * @return 文件管理器实例
     */
    public FileManager getManager() {
        String beanName = Objects.requireNonNull(active) + "FileManager";
        return super.getApplicationContext().getBean(beanName, FileManager.class);
    }
}
