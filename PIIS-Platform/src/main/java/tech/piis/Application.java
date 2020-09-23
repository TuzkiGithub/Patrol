package tech.piis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 */
@MapperScan(basePackages = {"tech.piis.modules.**.mapper"})
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class Application
{
    public static void main(String[] args)
    {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(Application.class, args);
        System.out.println("Run Success!");
    }
}
