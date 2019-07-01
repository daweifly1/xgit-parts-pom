package cn.com.xgit.parts.gen.zk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "apache.zookeeper.retrypolicy")
@Configuration
public class ApacheRetryPolicy {

    private int baseSleepTime = 1000;

    private int maxRetries = 29;

    private int maxSleep = 2147483647;

    public int getBaseSleepTime() {
        return baseSleepTime;
    }

    public void setBaseSleepTime(int baseSleepTime) {
        this.baseSleepTime = baseSleepTime;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getMaxSleep() {
        return maxSleep;
    }

    public void setMaxSleep(int maxSleep) {
        this.maxSleep = maxSleep;
    }
}