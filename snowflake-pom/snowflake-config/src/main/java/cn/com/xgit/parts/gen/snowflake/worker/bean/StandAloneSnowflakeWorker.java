package cn.com.xgit.parts.gen.snowflake.worker.bean;

import cn.com.xgit.parts.gen.snowflake.worker.ISnowflakeIdWorker;
import cn.com.xgit.parts.gen.snowflake.worker.SnowflakeIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 单点的雪花算法生成实力
 */
public class StandAloneSnowflakeWorker implements ISnowflakeIdWorker {

    private static final Logger log = LoggerFactory.getLogger(StandAloneSnowflakeWorker.class);
    /**
     * 机器id
     */
    public static Integer machineId;
    /**
     * 本地ip地址
     */
    private static String localIp;
    /**
     * 雪花算法生成
     */
    private SnowflakeIdWorker snowflakeIdWorker;


    public StandAloneSnowflakeWorker() {
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
        log.info("StandAloneSnowflakeWorker====");
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
    public Integer initMachineId() throws UnknownHostException {
        localIp = getIPAddress();
        Long ip = Long.parseLong(localIp.replaceAll("\\.", ""));
        machineId = ip.hashCode() % 32;
        return machineId;
    }

}
