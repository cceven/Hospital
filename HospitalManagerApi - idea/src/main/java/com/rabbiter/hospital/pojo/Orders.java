package com.rabbiter.hospital.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

//将此类映射到数据库中的 orders 表。
@TableName("orders")
public class Orders {
    @TableId(value = "o_id")//  指定该字段为表中的主键，对应数据库中的 o_id 列。
    @JsonProperty("oId")//    指定字段在序列化为 JSON 时的属性名为 oId
    private int oId;//    订单ID，对应数据库中的 o_id
    @JsonProperty("pId")
//    患者ID，对应数据库中的 p_id
    private int pId;
    @JsonProperty("dId")
//    医生ID，对应数据库中的 d_id
    private int dId;
    @JsonProperty("oRecord")
//    订单记录，对应数据库中的 o_record
    private String oRecord;
    @JsonProperty("oStart")
//    订单开始时间，对应数据库中的 o_start
    private String oStart;
    @JsonProperty("oEnd")
//    订单结束时间，对应数据库中的 o_end
    private String oEnd;
    @JsonProperty("oState")
//    订单状态，对应数据库中的 o_state。
    private Integer oState;
    @JsonProperty("oDrug")
//    药物信息，对应数据库中的 o_drug
    private String oDrug;
    @JsonProperty("oCheck")
//    检查信息，对应数据库中的 o_check。
    private String oCheck;
    @JsonProperty("oTotalPrice")
//    订单总价，对应数据库中的 o_total_price。
    private Double oTotalPrice;
    @JsonProperty("oPriceState")
//    支付状态，对应数据库中的 o_price_state。
    private Integer oPriceState;
    @JsonProperty("countGender")
    @TableField(exist = false)
//    用于存储统计数据的临时字段，在数据库中不存在
    private Integer countGender;
    @JsonProperty("oAdvice")
//    医嘱信息，对应数据库中的 o_advice
    private String oAdvice;
    //多表查询用
    @TableField(exist = false)//声明不是数据库里面的字段
//    private Doctor doctor;：医生信息对象，在数据库中不存在的临时字段。
    private Doctor doctor;
    //多表查询用
    @TableField(exist = false)//声明不是数据库里面的字段
//    private Patient patient;：患者信息对象，在数据库中不存在的临时字段。
    private Patient patient;
    @TableField(exist = false)
//    用于统计部分数据的临时字段，在数据库中不存在。
    private Integer countSection;
    @JsonProperty("dName")
    @TableField(exist = false)
//    医生名字，在数据库中不存在
    private String dName;
    @JsonProperty("pName")
    @TableField(exist = false)
//    患者名字，在数据库中不存在。
    private String pName;

//    默认构造方法
    public Orders() {
    }

//    全参构造方法，用于初始化对象的所有字段。
    public Orders(int oId, int pId, int dId, String oRecord, String oStart, String oEnd, Integer oState, String oDrug, String oCheck, Double oTotalPrice, Integer oPriceState, Integer countGender, String oAdvice, Doctor doctor, Patient patient, Integer countSection, String dName, String pName) {
        this.oId = oId;
        this.pId = pId;
        this.dId = dId;
        this.oRecord = oRecord;
        this.oStart = oStart;
        this.oEnd = oEnd;
        this.oState = oState;
        this.oDrug = oDrug;
        this.oCheck = oCheck;
        this.oTotalPrice = oTotalPrice;
        this.oPriceState = oPriceState;
        this.countGender = countGender;
        this.oAdvice = oAdvice;
        this.doctor = doctor;
        this.patient = patient;
        this.countSection = countSection;
        this.dName = dName;
        this.pName = pName;
    }

//    为每个字段提供了相应的 get 和 set 方法，用于访问和修改字段的值。
    public int getOId() {
        return oId;
    }

    public void setOId(int oId) {
        this.oId = oId;
    }

    public int getPId() {
        return pId;
    }

    public void setPId(int pId) {
        this.pId = pId;
    }

    public int getdId() {
        return dId;
    }

    public void setdId(int dId) {
        this.dId = dId;
    }

    public String getORecord() {
        return oRecord;
    }

    public void setORecord(String oRecord) {
        this.oRecord = oRecord;
    }

    public String getOStart() {
        return oStart;
    }

    public void setOStart(String oStart) {
        this.oStart = oStart;
    }

    public String getOEnd() {
        return oEnd;
    }

    public void setOEnd(String oEnd) {
        this.oEnd = oEnd;
    }

    public Integer getOState() {
        return oState;
    }

    public void setOState(Integer oState) {
        this.oState = oState;
    }

    public String getODrug() {
        return oDrug;
    }

    public void setODrug(String oDrug) {
        this.oDrug = oDrug;
    }

    public String getOCheck() {
        return oCheck;
    }

    public void setOCheck(String oCheck) {
        this.oCheck = oCheck;
    }

    public Double getOTotalPrice() {
        return oTotalPrice;
    }

    public void setOTotalPrice(Double oTotalPrice) {
        this.oTotalPrice = oTotalPrice;
    }

    public Integer getOPriceState() {
        return oPriceState;
    }

    public void setOPriceState(Integer oPriceState) {
        this.oPriceState = oPriceState;
    }

    public Integer getCountGender() {
        return countGender;
    }

    public void setCountGender(Integer countGender) {
        this.countGender = countGender;
    }

    public String getOAdvice() {
        return oAdvice;
    }

    public void setOAdvice(String oAdvice) {
        this.oAdvice = oAdvice;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Integer getCountSection() {
        return countSection;
    }

    public void setCountSection(Integer countSection) {
        this.countSection = countSection;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

//    @Override 的 toString 方法将该对象转换为字符串，主要用于调试或日志输出，输出格式为：Orders{...}
    @Override
    public String toString() {
        return "Orders{" +
                "oId=" + oId +
                ", pId=" + pId +
                ", dId=" + dId +
                ", oRecord='" + oRecord + '\'' +
                ", oStart='" + oStart + '\'' +
                ", oEnd='" + oEnd + '\'' +
                ", oState=" + oState +
                ", oDrug='" + oDrug + '\'' +
                ", oCheck='" + oCheck + '\'' +
                ", oTotalPrice=" + oTotalPrice +
                ", oPriceState=" + oPriceState +
                ", countGender=" + countGender +
                ", oAdvice='" + oAdvice + '\'' +
                ", doctor=" + doctor +
                ", patient=" + patient +
                ", countSection=" + countSection +
                ", dName='" + dName + '\'' +
                ", pName='" + pName + '\'' +
                '}';
    }
}
