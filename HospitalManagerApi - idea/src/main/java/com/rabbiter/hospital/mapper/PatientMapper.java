package com.rabbiter.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rabbiter.hospital.pojo.Patient;
import com.sun.org.apache.xpath.internal.operations.Plus;
import org.apache.ibatis.annotations.Param;

/*这段代码定义了一个名为 PatientMapper 的接口，用于与数据库进行交互。
该接口继承自 BaseMapper<Patient>，其中 Patient 是患者信息类。
这段代码的主要功能是利用 MyBatis-Plus 框架提供的基础 CRUD 操作，以及自定义查询功能。以下是对代码的详细*/

//一个自定义的方法，用于统计在某个年龄段内的患者数量
public interface PatientMapper extends BaseMapper<Patient> {//@Param("startAge") int startAge：使用 @Param 注解为 startAge 参数命名，使得在 XML 映射文件或注解 SQL 中可以通过名字来引用这个参数
    /**
     * 统计患者男女人数
     */
    Integer patientAge(@Param("startAge") int startAge, @Param("endAge") int endAge);//这个方法返回一个整数，表示符合指定年龄范围的患者人数。startAge 和 endAge 分别表示查询的年龄范围的起始和结束值
}
