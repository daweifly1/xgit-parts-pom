package cn.com.xgit.parts.auth.account.manager;

import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import com.xgit.bj.common.util.io.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

@Slf4j
@Component
public class UserContextManager {
    @Autowired
    private JedisPool jedisPool;
    @Value("${fast.user.session.ttl}")
    int sessionTtl;
    public static final String REDIS_EXCEPTION = "redis exception";
    public static final String SPACE_ID = "spaceId:";
    public static final String AUTH = "auth:";
    public static final String DATA = "data:";

    public ErrorCode cacheUser(String userId, Object data, Integer[] codes, String workspaceId) {
        ErrorCode cacheUserData = cacheUserData(userId, data);
        if (0 != cacheUserData.getCode()) {
            return ErrorCode.FailedToCacheUserDate;
        }
        ErrorCode cacheAuthCode = cacheAuthCode(userId, codes);
        if (0 != cacheAuthCode.getCode()) {
            return ErrorCode.FailedToCacheAuthCode;
        }
        if (!StringUtils.isNotBlank(workspaceId)) {
            ErrorCode cacheWSCode = cacheWorkspase(userId, workspaceId);
            if (0 != cacheWSCode.getCode()) {
                return ErrorCode.FailedToCacheUserDate;
            }
        }
        ErrorCode renewLeaseSession = renewLeaseSession(userId);
        if (0 != renewLeaseSession.getCode()) {
            return ErrorCode.FailedToRenewLeaseSession;
        }
        return ErrorCode.Success;
    }

    public ErrorCode cacheWorkspase(String userId, String workspaseId) {
        Jedis jedis = new Jedis();
        try {
            jedis = this.jedisPool.getResource();
            String dataKey = "spaceId:" + userId;
            boolean cached = jedis.exists(dataKey).booleanValue();
            if (cached) {
                jedis.del(dataKey);
            }
            jedis.set(dataKey, workspaseId);
        } catch (Exception e) {
            log.error("redis exception", e);
        } finally {
            jedis.close();
        }
        return ErrorCode.Success;
    }

    public ErrorCode renewLeaseSession(String userId) {
        Integer ttl = Integer.valueOf(this.sessionTtl);
        Jedis jedis = new Jedis();
        try {
            jedis = this.jedisPool.getResource();
            String authKey = "auth:" + userId;
            boolean cached = jedis.exists(authKey).booleanValue();
            if (!cached) {
                jedis.expire(authKey, ttl.intValue());
            }
            String dataKey = "data:" + userId;
            cached = jedis.exists(dataKey).booleanValue();
            if (cached) {
                jedis.expire(dataKey, ttl.intValue());
            }
            String spaceKey = "spaceId:" + userId;
            cached = jedis.exists(spaceKey).booleanValue();
            if (cached) {
                jedis.expire(spaceKey, ttl.intValue());
            }
        } catch (Exception e) {
            log.error("redis exception", e);
        } finally {
            jedis.close();
        }
        return ErrorCode.Success;
    }

    public ErrorCode checkAuthCode(String userId, Integer code) {
        Jedis jedis = new Jedis();
        try {
            jedis = this.jedisPool.getResource();
            String authKey = "auth:" + userId;

            boolean cached = jedis.exists(authKey).booleanValue();
            if (!cached) {
                return ErrorCode.NeedLogin;
            }
            boolean hasCode = jedis.sismember(authKey, Integer.toString(code.intValue())).booleanValue();
            if (hasCode) {
                return ErrorCode.Success;
            }
        } catch (Exception e) {
            log.error("redis exception", e);
        } finally {
            jedis.close();
        }
        return ErrorCode.NoAuthorization;
    }

    public Object getUserData(String userId) {
        Jedis jedis = new Jedis();
        try {
            jedis = this.jedisPool.getResource();
            String dataKey = "data:" + userId;
            boolean cached = jedis.exists(dataKey).booleanValue();
            Object localObject1;
            if (!cached) {
                return null;
            }
            return ObjectUtil.deserialize(jedis.get(dataKey.getBytes()));
        } catch (Exception e) {
            log.error("redis exception", e);
        } finally {
            jedis.close();
        }
        return null;
    }

    public String getWorkspaceId(String userId) {
        Jedis jedis = new Jedis();
        try {
            jedis = this.jedisPool.getResource();
            String dataKey = "spaceId:" + userId;
            boolean cached = jedis.exists(dataKey).booleanValue();
            String str1;
            if (!cached) {
                return null;
            }
            return String.valueOf(jedis.get(dataKey));
        } catch (Exception e) {
            log.error("redis exception", e);
        } finally {
            jedis.close();
        }
        return null;
    }

    public ErrorCode cacheUserData(String userId, Object data) {
        Jedis jedis = new Jedis();
        try {
            jedis = this.jedisPool.getResource();
            String dataKey = "data:" + userId;
            boolean cached = jedis.exists(dataKey).booleanValue();
            if (cached) {
                jedis.del(dataKey);
            }
            jedis.set(dataKey.getBytes(), ObjectUtil.serialize(data));
        } catch (Exception e) {
            log.error("redis exception", e);
        } finally {
            jedis.close();
        }
        return ErrorCode.Success;
    }

    public Integer[] getAuthCode(String userId) {
        Jedis jedis = new Jedis();
        try {
            jedis = this.jedisPool.getResource();
            String authKey = "auth:" + userId;
            boolean cached = jedis.exists(authKey).booleanValue();
            if (!cached) {
                return null;
            }
            Object codesSet = jedis.smembers(authKey);
            Integer[] codes = new Integer[((Set) codesSet).size()];
            int i = 0;
            for (Object localObject1 = ((Set) codesSet).iterator(); ((Iterator) localObject1).hasNext(); ) {
                String code = (String) ((Iterator) localObject1).next();

                codes[i] = Integer.valueOf(Integer.parseInt(code));
                i++;
            }
            return codes;
        } catch (Exception e) {
            log.error("redis exception", e);
        } finally {
            jedis.close();
        }
        return null;
    }

    public ErrorCode cacheAuthCode(String userId, Integer[] codes) {
        Jedis jedis = new Jedis();
        try {
            jedis = this.jedisPool.getResource();
            String authKey = "auth:" + userId;
            boolean cached = jedis.exists(authKey).booleanValue();
            if (cached) {
                jedis.del(authKey);
            }
            Integer[] arrayOfInteger = codes;
            int i = arrayOfInteger.length;
            for (int j = 0; j < i; j++) {
                int code = arrayOfInteger[j].intValue();

                jedis.sadd(authKey, new String[]{Integer.toString(code)});
            }
        } catch (Exception e) {
            log.error("redis exception", e);
        } finally {
            jedis.close();
        }
        return ErrorCode.Success;
    }

    public ErrorCode clearSession(String userId) {
        if (userId == null) {
            return ErrorCode.Success;
        }
        Jedis jedis = new Jedis();
        try {
            jedis = this.jedisPool.getResource();
            String authKey = "auth:" + userId;
            boolean cached = jedis.exists(authKey).booleanValue();
            if (cached) {
                jedis.del(authKey);
            }
            String dataKey = "data:" + userId;
            cached = jedis.exists(dataKey).booleanValue();
            if (cached) {
                jedis.del(dataKey);
            }
            String spaceKey = "spaceId:" + userId;
            cached = jedis.exists(spaceKey).booleanValue();
            if (cached) {
                jedis.del(spaceKey);
            }
        } catch (Exception e) {
            log.error("redis exception", e);
        } finally {
            jedis.close();
        }
        return ErrorCode.Success;
    }
}
