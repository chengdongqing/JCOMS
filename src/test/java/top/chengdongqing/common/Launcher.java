package top.chengdongqing.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动后可打开接口文档进行部分接口测试
 * Swagger默认访问地址：http://localhost:8080/swagger-ui/
 *
 * @author Luyao
 */
@SpringBootApplication
public class Launcher {

    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }
}
