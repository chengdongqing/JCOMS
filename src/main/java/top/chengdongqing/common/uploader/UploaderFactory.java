package top.chengdongqing.common.uploader;

import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 上传器工厂
 *
 * @author James Lu
 */
@Component
@RefreshScope
public class UploaderFactory extends ApplicationObjectSupport {

    @Value("${upload.active}")
    private String active;

    /**
     * 获取上传器实例
     *
     * @return 上传器实例
     */
    public Uploader getUploader() throws CannotLoadBeanClassException {
        Objects.requireNonNull(active, "upload.active cannot be null.");
        String beanName = active + "Uploader";
        return super.getApplicationContext().getBean(beanName, Uploader.class);
    }
}
