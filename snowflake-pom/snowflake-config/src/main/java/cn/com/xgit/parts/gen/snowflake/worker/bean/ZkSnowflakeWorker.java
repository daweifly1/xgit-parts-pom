package cn.com.xgit.parts.gen.snowflake.worker.bean;

import cn.com.xgit.parts.gen.snowflake.worker.ISnowflakeIdWorker;
import cn.com.xgit.parts.gen.snowflake.worker.SnowflakeIdWorker;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class ZkSnowflakeWorker implements ISnowflakeIdWorker {


    private static final Logger log = LoggerFactory.getLogger(ZkSnowflakeWorker.class);
    /**
     * 机器id
     */
    public static Integer machineId;
    /**
     * 本地ip地址
     */
    private static String localIp;

    //工作节点的路径
    private String pathPrefix = "/ccc2-";
    private String pathRegistered = null;

    private CuratorFramework client;
    /**
     * 雪花算法生成
     */
    private SnowflakeIdWorker snowflakeIdWorker;


    public ZkSnowflakeWorker(CuratorFramework client) {
        this.client = client;
        try {
            //创建一个机器ID
            machineId = initMachineId();
            localIp = getIPAddress();
            log.info("初始化 machine_id :{}....===localIp:{}............................", machineId, localIp);
            this.snowflakeIdWorker = new SnowflakeIdWorker(machineId, 1L);
        } catch (Exception e) {
            log.error("Fatal Error in initMachineId ", e);
            System.exit(0);
        }
    }


    public long nextId() {
        log.info("ZkSnowflakeWorker...........................................");
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
            for (int i = 0; i < 32; i++) {
                String path = pathPrefix + i;
                Stat r = client.checkExists().forPath(path);
                if (null == r) {
                    pathRegistered = client.create()
                            .creatingParentsIfNeeded()
                            .withMode(CreateMode.EPHEMERAL)
                            .forPath(path, payload);
                    if (null == pathRegistered) {
                        continue;
                    } else {
                        return i;
                    }
                }
            }
            throw new RuntimeException("节点已经注册饱和");
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e.getMessage());
        }
    }

}
