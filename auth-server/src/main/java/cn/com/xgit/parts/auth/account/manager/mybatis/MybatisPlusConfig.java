package cn.com.xgit.parts.auth.account.manager.mybatis;

import cn.com.xgit.parts.auth.account.infra.datasource.DatasourceConfig;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@Import({DatasourceConfig.class, TableInfoExHelperConfig.class})
@MapperScan("${auth.mybatis.mapper-scan:cn.com.xgit.parts.auth.**.mapper}")
@AutoConfigureBefore(value = {DataSourceAutoConfiguration.class, DatasourceConfig.class})
public class MybatisPlusConfig {


    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "mybatis-plus.global-config.show-performance", matchIfMissing = true)
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * mybatis-plus分页插件
     */
    @Bean
    @ConditionalOnMissingBean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        List<ISqlParser> sqlParserList = new ArrayList<>();
//        TenantSqlParser tenantSqlParser = new DefaultTenantSqlParser();
//        //设置租户处理handler
//        tenantSqlParser.setTenantHandler(new DefaultTenantHandler());
//        sqlParserList.add(tenantSqlParser);
        //逻辑删解析器
//        sqlParserList.add(new LogicDeleteSqlParser());
        //数据规则解析器
//        sqlParserList.add(new DataRuleSqlParser());
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }


    /**
     * mybatis-plus逻辑删除插件
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public LogicSqlInjector logicSqlInjector() {
        return new LogicSqlInjector();
    }

//    /**
//     * mybatis-plus自动填充
//     *
//     * @return
//     */
//    @Bean
//    @ConditionalOnMissingBean
//    public MetaObjectHandler mybatisMetaObjectHandler() {
//        return new MetaObjectHandlerConfig();
//    }
}
