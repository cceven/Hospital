//名为 OrderMapper 的接口，用于处理与医院挂号系统相关的数据库操作。利用了 MyBatis-Plus 框架来简化数据库交互。
package com.rabbiter.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rabbiter.hospital.pojo.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;
//定义了一个名为 OrderMapper 的接口，它继承自 BaseMapper<T> 泛型接口，其中 T 是 Orders 类。这使得 OrderMapper 继承了 BaseMapper 提供的一系列基本数据库操作方法，如增删改查。
public interface OrderMapper extends BaseMapper<Orders> {
    /**
     * 统计从 oStart 指定的日期开始，今天的挂号人数
     */
    int orderPeople(String oStart);
    /**
     * 统计某个医生 (d_id) 从 oStart 开始的挂号人数。使用 @Param 注解来映射方法参数到SQL语句中的占位符
     */
    int orderPeopleByDid(@Param("o_start") String oStart, @Param("d_id") int dId);
    /**
     * 统计挂号人的性别分布，并返回一个包含统计结果的字符串列表
     */
    List<String> orderGender();
    /**
     * 根据挂号单号 (oId) 查询挂号信息
     */
    Orders findOrderByOid(int oId);
    /**
     * 更新挂号信息，增加诊断及医生意见
     */
    Integer updateOrderByAdd(Orders order);
    /**
     * 统计在指定时间区间内，各科室的挂号人数
     */
    List<String> orderSection(@Param("startTime") String startTime, @Param("endTime") String endTime);
    /**
     * 查询在 oStart 指定日期，某医生 (dId) 的所有挂号信息
     */
    List<Orders> findOrderByNull(@Param("dId") int dId, @Param("oStart") String oStart);
    /**
     * 根据患者ID (pId) 查询挂号信息
     */
    List<Orders> findOrderByPid(int pId);

}
