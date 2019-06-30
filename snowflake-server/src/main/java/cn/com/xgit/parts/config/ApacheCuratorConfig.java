package cn.com.xgit.parts.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private Logger logger = LoggerFactory.getLogger(ApacheCuratorConfig.class);

    @Autowired
    private ApacheZooKeeperProperties apacheZooKeeperProperties;

    @Autowired
    private ApacheRetryPolicy apacheRetryPolicy;

    CuratorFramework client = null;

    @Bean
    public CuratorFramework getCuratorFramework() {
        logger.info("zooKeeper client init...");

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
            logger.info("zooKeeper client start...");

        } catch (Exception e) {
            logger.info("zooKeeper connect error...");
            e.printStackTrace();
        }
        return client;
    }
}