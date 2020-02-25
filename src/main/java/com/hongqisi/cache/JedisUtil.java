package com.hongqisi.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

import java.util.Set;

public class JedisUtil {


    /** 操作key的方法**/
    public Keys keys;
    /** 操作value的方法**/
    public Strings strings;

    //redis连接池对象
    private JedisPool jedisPool;

    //获取连接池对象
    public JedisPool getJedisPool(){
        return jedisPool;
    }

    //设置redis连接池
    public void setJedisPool(JedisPoolWriper jedisPoolWriper){
        this.jedisPool=jedisPoolWriper.getJedisPool();
    }

    /**
     * 从jedis连接池中获取连接池对象
     * @return
     */
    public Jedis getJedis(){
        return jedisPool.getResource();
    }


    /**
     * 内部类
     */
    public class Keys{

        /**
         * 清空所有的key
         * @return
         */
        public String flushAll(){
            Jedis jedis=getJedis();
            //将所有的key删除，同时返回所有的key数据
            String stata=jedis.flushAll();
            jedis.close();
            return stata;
        }

        /**
         * 删除keys对应的记录，可以是多个key
         * @param keys
         * @return
         */
        public long del(String... keys){
            Jedis jedis=getJedis();
            //返回删除的记录数
            long count=jedis.del(keys);
            jedis.close();
            return count;
        }

        /**
         * 判断key是否存在
         * @param key
         * @return
         */
        public boolean exists(String key){
           Jedis jedis=getJedis();
           boolean exist=jedis.exists(key);
           jedis.close();
           return exist;
        }

        /**
         * 查找所有匹配给定的模式的键
         * @param pattern
         * @return
         */
        public Set<String> keys(String pattern){
            Jedis jedis=getJedis();
            Set<String> set=jedis.keys(pattern);
            jedis.close();
            return set;
        }
    }

    /**
     * 内部类
     */
    public class Strings{
        /**
         * 根据key返回相应的值
         * @param key
         * @return
         */
        public String get(String key){
            Jedis jedis=getJedis();
            String value=jedis.get(key);
            jedis.close();
            return value;
        }


        /**
         * 添加记录，如果记录已经存在，则覆盖原有的记录
         * @param key
         * @param value
         * @return  状态码
         */
        public String set(String key,String value){
            return set(SafeEncoder.encode(key),SafeEncoder.encode(value));
        }

        /**
         * 添加记录，如果记录已经存在，则覆盖原有的记录
         * @param key
         * @param value
         * @return
         */
        public String set(byte[] key,byte[] value){
            Jedis jedis=getJedis();
            String status=jedis.set(key,value);
            jedis.close();
            return status;
        }
    }

}
