package cn.com.xgit.parts.auth.account.manager.mybatis;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;

import javax.annotation.PostConstruct;

@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
public class TableInfoExHelperConfig {

    @PostConstruct
    public void init(){
        TableInfoExHelper.initTable();
    }
}
