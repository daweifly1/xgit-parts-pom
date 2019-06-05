package cn.com.xgit.parts.auth.account.service;

import com.xgit.bj.gen.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GuidService {
    @Value("${fast.guid.worker-id:0}")
    private int workerId;
    @Value("${fast.guid.datacenter-id:0}")
    private int datacenterId;

    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(this.workerId, this.datacenterId);

    public long nextId() {
        return this.snowflakeIdWorker.nextId();
    }

    public String genTextId() {
        return this.snowflakeIdWorker.nextId() + "";
    }
}
