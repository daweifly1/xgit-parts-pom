package cn.com.xgit.parts.gen.zk.config;

import cn.com.xgit.parts.gen.snowflake.worker.ISnowflakeIdWorker;
import cn.com.xgit.parts.gen.snowflake.worker.bean.StandAloneSnowflakeWorker;
import cn.com.xgit.parts.gen.snowflake.worker.bean.ZkSnowflakeWorker;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 连接zooKeeper server，获得zkClient
 */
@Configuration
@EnableConfigurationProperties(value = {ApacheZooKeeperProperties.class, ApacheRetryPolicy.class})
public class ApacheCuratorConfig {

    private Logger log = LoggerFactory.getLogger(ApacheCuratorConfig.class);

    @Autowired
    private ApacheZooKeeperProperties apacheZooKeeperProperties;

    @Autowired
    private ApacheRetryPolicy apacheRetryPolicy;

    @ConditionalOnProperty(value = "apache.zookeeper.enabled")
    @Bean
    public CuratorFramework getCuratorFramework() {
        log.info("zooKeeper client init...");
        CuratorFramework client = null;
        try {
            //当zk连接时失败的重连策略
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(apacheRetryPolicy.getBaseSleepTime(), apacheRetryPolicy.getMaxRetries());

            //获得实例对象，拿到ZK client
            //CuratorFramework client = CuratorFrameworkFactory.newClient(apacheZooKeeperProperties.getConnectUrl(), apacheZooKeeperProperties.getSessionTimeout(), apacheZooKeeperProperties.getConnectionTimeout(), retryPolicy);
            List<AuthInfo> authInfos = new ArrayList<>();
            authInfos.add(new AuthInfo(apacheZooKeeperProperties.getScheme(), apacheZooKeeperProperties.getAuthId().getBytes()));

            client = CuratorFrameworkFactory.builder()
                    .authorization(authInfos)
                    .connectString(apacheZooKeeperProperties.getConnectUrl())
                    .sessionTimeoutMs(apacheZooKeeperProperties.getSessionTimeout())
                    .connectionTimeoutMs(apacheZooKeeperProperties.getConnectionTimeout())
                    .retryPolicy(retryPolicy)
                    .namespace("workspace")
                    .build();

            client.start();
            log.info("zooKeeper client start...");

        } catch (Exception e) {
            log.error("zooKeeper connect error...");
        }
        return client;
    }


    @Bean
    @ConditionalOnProperty(value = "apache.zookeeper.enabled") // 需要被配置的类
    public ISnowflakeIdWorker getZkSnowflakeWorker() {
        log.info("初始化 getZkSnowflakeWorker");
        return new ZkSnowflakeWorker(getCuratorFramework());
    }

    @Bean
    @ConditionalOnMissingBean(ISnowflakeIdWorker.class)
    public ISnowflakeIdWorker getStandAloneSnowflakeWorker() {
        log.info("初始化 getStandAloneSnowflakeWorker");
        return new StandAloneSnowflakeWorker();
    }
}