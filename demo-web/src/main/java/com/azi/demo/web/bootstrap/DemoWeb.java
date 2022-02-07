
package com.azi.demo.web.bootstrap;

import com.alibaba.dubbo.common.status.Status;
import com.azi.demo.web.controller.SpringContextUtil1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Map;

/**
 * @author azizwz@aliyun.com
 *         Function:
 */
@SpringBootApplication(scanBasePackages = "com.azi.demo.web.controller")
public class DemoWeb {
    public static void main(String[] args) {
        SpringApplication.run(DemoWeb.class, args);
    }
}
