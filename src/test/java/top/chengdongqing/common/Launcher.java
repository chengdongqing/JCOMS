package top.chengdongqing.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>启动后可访问接口文档进行部分接口测试</p>
 * <p>默认访问地址: http://localhost:8080/swagger-ui/</p>
 *
 * @author Luyao
 */
@SpringBootApplication
public class Launcher {

    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }
}
