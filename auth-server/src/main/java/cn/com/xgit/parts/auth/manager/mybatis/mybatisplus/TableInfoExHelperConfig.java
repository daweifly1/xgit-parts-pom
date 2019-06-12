package cn.com.xgit.parts.auth.manager.mybatis.mybatisplus;

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
