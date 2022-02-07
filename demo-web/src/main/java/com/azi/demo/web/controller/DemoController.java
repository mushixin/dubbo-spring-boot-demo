
package com.azi.demo.web.controller;

import com.alibaba.dubbo.common.status.Status;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.azi.demo.service.api.DemoService;
import com.azi.demo.service.dto.TestVO;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author azizwz@aliyun.com
 *         Function:
 */
@RestController("demo/")
public class DemoController {

    @Reference
    private DemoService demoService;

    @RequestMapping(value = "test", method = RequestMethod.POST)
    public String addTest(@RequestParam String name) {
        demoService.insertModel(name);
        return "Add a new Test";
    }

    @RequestMapping(value = "test/{id}", method = RequestMethod.GET)
    public TestVO getTest(@PathVariable String id) {
        return demoService.getTestById(id);
    }

    @RequestMapping(value = "test/name/{name}", method = RequestMethod.GET)
    public List<TestVO> getTests(@PathVariable String name) {
        return demoService.getTestByName(name);
    }

    @RequestMapping(value = "check", method = RequestMethod.GET)
    public Status check() {
        ApplicationContext context = SpringContextUtil1.getApplicationContext();
        if (context == null) {
            return new Status(Status.Level.UNKNOWN);
        }
        Map<String, DataSource> dataSources = context.getBeansOfType(DataSource.class, false, false);
        if (dataSources == null || dataSources.size() == 0) {
            return new Status(Status.Level.UNKNOWN);
        }
        Status.Level level = Status.Level.OK;
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
            DataSource dataSource = entry.getValue();
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append(entry.getKey());
            try {
                Connection connection = dataSource.getConnection();
                try {
                    DatabaseMetaData metaData = connection.getMetaData();
                    ResultSet resultSet = metaData.getTypeInfo();
                    try {
                        if (!resultSet.next()) {
                            level = Status.Level.ERROR;
                        }
                    } finally {
                        resultSet.close();
                    }
                    buf.append(metaData.getURL());
                    buf.append("(");
                    buf.append(metaData.getDatabaseProductName());
                    buf.append("-");
                    buf.append(metaData.getDatabaseProductVersion());
                    buf.append(")");
                } finally {
                    connection.close();
                }
            } catch (Throwable e) {
//                logger.warn(e.getMessage(), e);
                return new Status(level, e.getMessage());
            }
        }
        return new Status(level, buf.toString());
    }
}
