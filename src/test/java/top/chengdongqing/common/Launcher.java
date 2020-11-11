package top.chengdongqing.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Luyao
 * 启动后可打开接口文档进行部分接口测试
 * 文档默认地址：http://localhost:8080/swagger-ui/
 */
@SpringBootApplication
public class Launcher {

    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }
}
