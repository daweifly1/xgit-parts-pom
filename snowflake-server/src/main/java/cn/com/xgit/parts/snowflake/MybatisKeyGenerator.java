package cn.com.xgit.parts.snowflake;

import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Component("mybatisKeyGenerator")
public class MybatisKeyGenerator implements IKeyGenerator {

    /**
     * 机器id
     */
    public static Integer machineId;
    /**
     * 本地ip地址
     */
    private static String localIp;

    //工作节点的路径
    private String pathPrefix = "/test/IDMaker/worker-";
    private String pathRegistered = null;

    @Autowired
    private CuratorFramework client;
    /**
     * 雪花算法生成
     */
    private SnowflakeIdWorker snowflakeIdWorker;


    @PostConstruct
    private void init() {
        try {
            //创建一个机器ID
            machineId = initMachineId();
            if (machineId > 31L || machineId < 0L) {
                throw new RuntimeException("节点数量已经达到32个");
            }
            log.info("初始化 machine_id :{}   ===localIp:{}", machineId, localIp);
            this.snowflakeIdWorker = new SnowflakeIdWorker(machineId, 1L);
        } catch (Exception e) {
            log.error("Fatal Error in initMachineId ", e);
            System.exit(0);
        }
    }

    /**
     * 容器销毁前清除注册记录,一定需要优雅停机
     */
    @PreDestroy
    public void destroyMachineId() {
        log.debug("====");
    }

    @Override
    public String executeSql(String incrementerName) {
        //多实例时候需要考虑使用一致的服务(此处借助redis实现,借助zk方式实现会更有优势)
        long uid = 0;
        if (null == snowflakeIdWorker) {
            return "select " + uid + " from dual";
        }
        uid = snowflakeIdWorker.nextId();
        return "select " + uid + " from dual";
    }

    public long nextId() {
        return snowflakeIdWorker.nextId();
    }


    /**
     * 获取ip地址
     *
     * @return
     * @throws UnknownHostException
     */
    private String getIPAddress() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        return address.getHostAddress();
    }


    /**
     * 主方法：获取一个机器id
     *
     * @return
     */
    public Integer initMachineId() {
        // 创建一个 ZNode 节点
        // 节点的 payload 为当前worker 实例
        try {
            byte[] payload = "snowflakexx".getBytes();

            pathRegistered = client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(pathPrefix, payload);
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e.getMessage());
        }
        if (null == pathRegistered) {
            throw new RuntimeException("节点注册失败");
        }
        String sid = null;
        int index = pathRegistered.lastIndexOf(pathPrefix);
        log.info("index:{}", index);
        if (index >= 0) {
            index += pathPrefix.length();
            sid = index <= pathRegistered.length() ? pathRegistered.substring(index) : null;
        }

        if (null == sid) {
            throw new RuntimeException("节点ID生成失败");
        }
        return Integer.parseInt(sid);
    }

}
