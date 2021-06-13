package com.hly.july.common.biz.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

//    public RedisTemplate getRedisTemplate() {
//        return redisTemplate;
//    }
//
//    public void setRedisTemplate(RedisTemplate template) {
//        redisTemplate = template;
//    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time) {

        if (key == null || "".equals(key) || time < 0) {
            return false;
        }

        try {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 是否包含key
     *
     * @param key
     * @return
     */
    public boolean hasKey(String key) {

        if (key == null || "".equals(key)) {
            return false;
        }

        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        if (key == null || "".equals(key)) {
            return null;
        } else {
            return redisTemplate.opsForValue().get(key);
        }
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value) {
        if (key == null || "".equals(key)) {
            return false;
        }

        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 设置缓存以及过期时间
     *
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean set(String key, Object value, long time) {

        if (key == null || "".equals(key) || time < 0) {
            return false;
        }

        try {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 判断一个Key是否存在
     *
     * @param key
     * @return
     */
    public boolean exist(String key) {
        try {
            return  redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 删除Key
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }


    /**
     * 获取hash表项
     *
     * @param key
     * @param field
     * @return
     */
    public Object hGet(String key, String field) {

        if (key == null || "".equals(key)) {
            return null;
        } else {
            return redisTemplate.opsForHash().get(key, field);
        }
    }

    /**
     * 获取hash表
     *
     * @param key
     * @return
     */
    public Map<Object, Object> hmGet(String key) {
        if (key == null || "".equals(key)) {
            return null;
        } else {
            return redisTemplate.opsForHash().entries(key);
        }
    }

    /**
     * 设置hash表
     *
     * @param key
     * @param map
     * @return
     */
    public boolean hmSet(String key, Map<String, Object> map) {

        if (key == null || "".equals(key)) {
            return false;
        }

        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 设置hash表及过期时间
     *
     * @param key
     * @param map
     * @param time
     * @return
     */
    public boolean hmSet(String key, Map<String, Object> map, long time) {

        if (key == null || "".equals(key) || time < 0) {
            return false;
        }

        try {
            redisTemplate.opsForHash().putAll(key, map);
            expire(key, time);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 设置hash表项
     *
     * @param key
     * @param item
     * @param value
     * @return
     */
    public boolean hSet(String key, String item, Object value) {

        if (key == null || "".equals(key) || item == null || "".equals(item)) {
            return false;
        }

        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 设置hash表项及过期时间
     *
     * @param key
     * @param field
     * @param value
     * @param time
     * @return
     */
    public boolean hSet(String key, String field, Object value, long time) {

        if (key == null || "".equals(key) || field == null || "".equals(field) || time < 0) {
            return false;
        }

        try {
            redisTemplate.opsForHash().put(key, field, value);
            expire(key, time);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 删除hash表项
     *
     * @param key
     * @param fields
     */
    public void hDel(String key, Object... fields) {

        if (key != null && !"".equals(key)) {
            redisTemplate.opsForHash().delete(key, fields);
        }
    }

    /**
     * 判断hash表是否存在某项
     *
     * @param key
     * @param item
     * @return
     */
    public boolean hHasKey(String key, String field) {
        if (key == null || "".equals(key)) {
            return false;
        }
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 获取set中的所有值
     *
     * @param key
     * @return
     */
    public Set<Object> sGet(String key) {

        if (key == null || "".equals(key)) {
            return null;
        }

        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 判断set是否存在某值
     *
     * @param key
     * @param value
     * @return
     */
    public boolean sHasValue(String key, Object value) {

        if (key == null || "".equals(key) || value == null) {
            return false;
        }

        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 设置set缓存
     *
     * @param key
     * @param values
     * @return
     */
    public long sSet(String key, Object... values) {

        if (key == null || "".equals(key)) {
            return 0;
        }

        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 设置set缓存及过期时间
     *
     * @param key
     * @param time
     * @param values
     * @return
     */
    public long sSet(String key, long time, Object... values) {

        if (key == null || "".equals(key) || time < 0) {
            return 0;
        }

        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            expire(key, time);
            return count;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 获取set大小
     *
     * @param key
     * @return
     */
    public long sGetSize(String key) {

        if (key == null || "".equals(key)) {
            return 0;
        }

        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 删除set里面的值
     *
     * @param key
     * @param values
     * @return
     */
    public long sDel(String key, Object... values) {

        if (key == null || "".equals(key)) {
            return 0;
        }

        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 获取list缓存
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {

        if (key == null || "".equals(key)) {
            return null;
        }

        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 获取list长度
     *
     * @param key
     * @return
     */
    public long lGetSize(String key) {

        if (key == null || "".equals(key)) {
            return 0;
        }

        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 获取list下标处的值
     *
     * @param key
     * @param index
     * @return
     */
    public Object lGetIndex(String key, long index) {

        if (key == null || "".equals(key)) {
            return null;
        }

        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 设置list缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, Object value) {

        if (key == null || "".equals(key)) {
            return false;
        }

        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 设置list缓存以及过期时间
     *
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {

        if (key == null || "".equals(key) || time < 0) {
            return false;
        }

        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            expire(key, time);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 设置list缓存下标处的值
     *
     * @param key
     * @param index
     * @param value
     * @return
     */
    public boolean lsetIndex(String key, long index, Object value) {

        if (key == null || "".equals(key)) {
            return false;
        }

        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 开启事务
     * 开启事务，后续所有指令加入到事务序列，但不立即执行。待exec后执行
     * @return
     */
    public boolean multi() {

        try {
            redisTemplate.multi();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 事务结束
     * 事物结束，同时执行事务序列，需与multi成对出现、使用。
     *
     * @return
     */
    public boolean exec() {

        try {
            redisTemplate.exec();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 取消事务
     * 终止当前事务定义，发生在multi之后，exec之前。
     *
     * @return
     */
    public boolean discard() {

        try {
            redisTemplate.discard();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * string递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long stringIncr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }
    /**
     * string递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long stringDecr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * hash递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long hashIncr(String key,String field, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForHash().increment(key,field, delta);
    }
    /**
     * hash递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long hashDecr(String key,String field, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForHash().increment(key,field, -delta);
    }

}
