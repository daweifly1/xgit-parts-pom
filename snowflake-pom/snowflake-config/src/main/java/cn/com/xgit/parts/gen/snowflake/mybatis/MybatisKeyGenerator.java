package cn.com.xgit.parts.gen.snowflake.mybatis;

import cn.com.xgit.parts.gen.snowflake.worker.ISnowflakeIdWorker;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("mybatisKeyGenerator")
public class MybatisKeyGenerator implements IKeyGenerator {

    private static final Logger log = LoggerFactory.getLogger(MybatisKeyGenerator.class);
    /**
     * 雪花算法生成
     */
    @Autowired
    private ISnowflakeIdWorker snowflakeIdWorker;


    @Override
    public String executeSql(String incrementerName) {
        //单实例时候可以使用，或者少量机器ip根据32取模不重复，否则会有冲突的概率
        long uid = 0;
        if (null == snowflakeIdWorker) {
            return "select " + uid + " from dual";
        }
        uid = snowflakeIdWorker.nextId();
        return "select " + uid + " from dual";
    }

}
