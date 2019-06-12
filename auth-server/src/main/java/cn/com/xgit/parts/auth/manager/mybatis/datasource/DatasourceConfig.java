package cn.com.xgit.parts.auth.manager.mybatis.datasource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ServletComponentScan
public class DatasourceConfig {
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource getDataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", this.driverClassName);
        props.put("url", this.url);
        props.put("username", this.username);
        props.put("password", this.password);
        props.put("filters", "stat");
        return DruidDataSourceFactory.createDataSource(props);
    }
}
