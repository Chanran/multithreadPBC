package util;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisUtil
{
    private static JedisPool pool = null;
    private static String host = "localhost";
    private static int port = 6379;


    /**
     * 获取jedis连接池
     */
    public static JedisPool getPool() {
        if(pool == null)
        {
            //创建jedis连接池配置
            JedisPoolConfig config = new JedisPoolConfig();
            //最大连接数
            config.setMaxTotal(100);
            //最大空闲连接
            config.setMaxIdle(5);
            //创建redis连接池
            pool = new JedisPool(config,host,port);
        }
        return pool;
    }

    /**
     * 获取jedis连接
     */
    public static Jedis getConn() {
        return getPool().getResource();
    }

    /**
     * 关闭redis连接
     */
    public static void close(Jedis jedis){
        try{
            getPool().returnResource(jedis);
        }catch (Exception e){
            if (jedis.isConnected()){
                jedis.quit();
                jedis.disconnect();
            }
        }
    }

    /**
     * 获取数据
     * @param key(byte[])
     * @return
     */
    public static byte[] get(byte[] key){
        byte[] value = null;
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            //释放redis对象
            getPool().returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }

        return value;
    }
    /**
     * 插入数据
     * @param key(byte[]),value(byte[)
     */
    public static void set(byte[] key, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = getPool().getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            //释放redis对象
            getPool().returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }

    /**
     * 存储REDIS队列 顺序存储
     * @param  key reids键名
     * @param  value 键值
     */
    public static void lpush(byte[] key, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = getPool().getResource();
            jedis.lpush(key, value);
        } catch (Exception e) {
            //释放redis对象
            getPool().returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }

    /**
     * 存储REDIS队列 反向存储
     * @param  key reids键名
     * @param  value 键值
     */
    public static void rpush(byte[] key, byte[] value) {

        Jedis jedis = null;
        try {

            jedis = getPool().getResource();
            jedis.rpush(key, value);

        } catch (Exception e) {

            //释放redis对象
            getPool().returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {

            //返还到连接池
            close(jedis);

        }
    }

    /**
     * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端
     * @param  key reids键名
     * @param  destination 键值
     */
    public static void rpoplpush(byte[] key, byte[] destination) {

        Jedis jedis = null;
        try {

            jedis = getPool().getResource();
            jedis.rpoplpush(key, destination);

        } catch (Exception e) {

            //释放redis对象
            getPool().returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {

            //返还到连接池
            close(jedis);

        }
    }

    /**
     * 获取队列数据
     * @param  key 键名
     * @return
     */
    public static List lpopList(byte[] key) {

        List list = null;
        Jedis jedis = null;
        try {

            jedis = getPool().getResource();
            list = jedis.lrange(key, 0, -1);

        } catch (Exception e) {

            //释放redis对象
            getPool().returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {

            //返还到连接池
            close(jedis);

        }
        return list;
    }

    /**
     * 获取队列数据
     * @param  key 键名
     * @return
     */
    public static byte[] rpop(byte[] key) {

        byte[] bytes = null;
        Jedis jedis = null;
        try {

            jedis = getPool().getResource();
            bytes = jedis.rpop(key);

        } catch (Exception e) {

            //释放redis对象
            getPool().returnBrokenResource(jedis);
            e.printStackTrace();

        } finally {

            //返还到连接池
            close(jedis);

        }
        return bytes;
    }

}