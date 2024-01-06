package rainbowsix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * springboot应用启动类.
 */
@SpringBootApplication(scanBasePackages = "rainbowsix")  //指定扫描包路径
@EnableDiscoveryClient
public class SentiApplication {

    /**
     * 启动 Spring Boot 应用.
     * @param args 命令行参数
     */
    public static void main(final String[] args) {
        SpringApplication.run(SentiApplication.class, args); //启动 Spring Boot 应用
    }
}
