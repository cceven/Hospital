package com.rabbiter.hospital.service.serviceImpl;

import com.rabbiter.hospital.mapper.ArrangeMapper;
import com.rabbiter.hospital.pojo.Arrange;
import com.rabbiter.hospital.service.ArrangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;

@Service("ArrangeService")
public class ArrangeServiceImpl implements ArrangeService {
    @Autowired
    private ArrangeMapper arrangeMapper;//用于数据库操作
    @Autowired
    private JedisPool jedisPool;//用于Redis 缓存操作

    /**
     * 根据日期查询排班信息
     * 根据日期（arTime）和科室（dSection）查询排班信息，
     * 返回一个 List<Arrange> 列表，表示所有符合条件的排班记录
     */
    @Override
    public List<Arrange> findByTime(String arTime, String dSection) {
        return this.arrangeMapper.findByTime(arTime, dSection);
    }
    /**
     * 增加排班信息
     */
    public Boolean addArrange(Arrange arrange){
        Arrange arrange1 = this.arrangeMapper.selectById(arrange.getArId());
        Jedis jedis = jedisPool.getResource();
        HashMap<String, String> map = new HashMap<>();//使用 HashMap<String, String> 创建一个键值对映射 map，用于存储排班相关的信息
        map.put("eTOn","10");
        map.put("nTOt","10");
        map.put("tTOe","10");
        map.put("fTOf","10");
        map.put("fTOs","10");
        map.put("sTOs","10");
        if (arrange1 == null) {//如果数据库中不存在同 ID 的记录
            //redis操作开始
//            jedis.hset(arrange.getArId(), map);
            // 或者使用hmset设置整个哈希表的值
            jedis.hmset(arrange.getArId(), map);//将排班信息以哈希表形式存储在 Redis 中。
            jedis.expire(arrange.getArId(), 604800);//设置该键的过期时间为 7 天。
            //redis操作结束
            this.arrangeMapper.insert(arrange);//将排班信息插入到数据库中
            return true;
        }
        return false;
    }

    /**
     * 删除排班信息
     */
    public Boolean deleteArrange(String arId){
        Arrange arrange = this.arrangeMapper.selectById(arId);
        Jedis jedis = jedisPool.getResource();
        if (arrange != null) {
            jedis.del(arId);//从 Redis 缓存中删除该排班信息
            this.arrangeMapper.deleteById(arId);//从数据库中删除对应记录
            return true;
        }
        return false;
    }

}
