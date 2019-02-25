package com.phantoms.framework.cloudbase.util;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

public class RedisClient {

    private static final Logger      logger             = LoggerFactory.getLogger(RedisClient.class);
    public static final String       accessTokenLock    = "accessTokenLock";
    public static final String       comAccessTokenLock = "comAccessTokenLock";

    private static JedisPool         jedisPool          = null;
    private static JedisSentinelPool sentinelPool       = null;
    private static String            prefKey            = "MY_REDIS_";
    public static final int          TEN_MINUTE         = 60 * 10;                                   // 十分钟
    public static final int          TWENTY_MINUTE      = 60 * 10 * 2;                               // 二十分钟
    public static final int          MIDDLE_HOUR        = 1800;                                      // 半小时
    public static final int          TWO_HOUR           = 60 * 60 * 2;                               // 2个小时
    public static final int          ONE_DAY            = 60 * 60 * 24;                              // 1天

    public static void initRedisClient(String clusterName, String password, Set<String> sentinels, String prefKey) {
        if (null != sentinelPool) {
            return;
        }
        RedisClient.prefKey = prefKey;
        synchronized (RedisClient.class) {
            if (null == sentinelPool) {
                sentinelPool = new JedisSentinelPool(clusterName, sentinels, password);
            }
        }
        
    }

    public static void initRedisClient(String ip, int port, String prefKey) {
        if (null != jedisPool) {
            return;
        }
        RedisClient.prefKey = prefKey;
        synchronized (RedisClient.class) {
            if (null == jedisPool) {
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(300);
                config.setMaxIdle(20);
                config.setMaxWaitMillis(3000);
                jedisPool = new JedisPool(config, ip, port);
            }
        }
    }

    /**
     * 获得锁
     * 
     * @param key
     * @param value
     * @return
     */
    public static boolean getLock(String key, String value, int seconds) {
        key = getCompleteKey(key);
        Jedis jedis = null;
        try {
            jedis = getJedis();
            // 1 if the key was set ,0 if the key was not set,
            // 返回0代表，之前没设置过，即获得锁成功
            Long identifier = jedis.setnx(key, value);
            if (identifier.longValue() == 0) {
                return false;
            }
            jedis.expire(key, seconds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }

    /**
     * 释放lock
     * 
     * @param key
     * @return
     */
    public static boolean delLock(String key) {
        key = getCompleteKey(key);
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.del(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }

    /**
     * 设置缓存对象值，并设置有效时间
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean set(String key, String value, int seconds) {
        key = getCompleteKey(key);
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, value);
            jedis.expire(key, seconds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }

    /**
     * 根据key 获取内容
     * 
     * @param key
     * @return
     */
    public static String get(String key) {
        key = getCompleteKey(key);
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String value = jedis.get(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }

        return null;
    }

    public static Jedis getJedis() throws Exception {
        if (sentinelPool != null) {
            return sentinelPool.getResource();
        } else if (jedisPool != null) {
            return jedisPool.getResource();
        }
        throw new Exception("jedisPool and sentinelPool are both null ");
    }

    /**
     * 如果key不存在就设置。存在即无效操作。用于一些不可以修改的默认值 TODO Add comments here.
     * 
     * @param lockName
     * @param lockValue
     * @return 1 if the key was set 0 if the key was not set
     */
    public static Long setnx(String lockName, String lockValue) {
        lockName = getCompleteKey(lockName);
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.setnx(lockName, lockValue);
        } catch (Exception e) {
            logger.error("setnx error");
        } finally {
            jedis.close();
        }
        return -1L;
    }

    /**
     * @param lockName 锁 key
     * @param timeOut 单位秒
     */
    public static void lockExpire(String lockName, int timeOut) {
        lockName = getCompleteKey(lockName);
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.expire(lockName, timeOut);
        } catch (Exception e) {
            logger.error("setnx error");
        } finally {
            jedis.close();
        }
    }

    private static String getCompleteKey(String key) {
        return prefKey + "_" + key;
    }

    public static Long publish(String channel, String content) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.publish(channel, content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * Atomically return and remove the first (LPOP) or last (RPOP) element of
     * the list. For example if the list contains the elements "a","b","c" LPOP
     * will return "a" and the list will become "b","c". If the key does not
     * exist or the list is already empty the special value 'nil' is returned.
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String rpop(String key) throws Exception {
        key = getCompleteKey(key);
        Jedis jedis = getJedis();
        try {
            return jedis.rpop(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list
     * stored at key. If the key does not exist an empty list is created just
     * before the append operation. If the key exists but is not a List an error
     * is returned.
     *
     * @param key
     * @param strings
     */
    public static Long lpush(String key, String... strings) {
        key = getCompleteKey(key);
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.lpush(key, strings);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * Increment the number stored at key by one. If the key does not exist or
     * contains a value of a wrong type, set the key to the value of "0" before
     * to perform the increment operation. INCR commands are limited to 64 bit
     * signed integers. Note: this is actually a string operation, that is, in
     * Redis there are not "integer" types. Simply the string stored at the key
     * is parsed as a base 10 64 bit signed integer, incremented, and then
     * converted back as a string.
     *
     * @param key
     * @return
     */
    public static Long incr(String key) {
        key = getCompleteKey(key);
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.incr(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            jedis.close();
        }
    }

    public static Long decr(String key) {
        key = getCompleteKey(key);
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.decr(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            jedis.close();
        }
    }

}
