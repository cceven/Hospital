//这段代码定义了一个名为 Patient 的 Java 类，用于表示医院系统中的患者信息。类中的各个字段对应数据库中 patient 表的列，用于存储患者的各种属性
package com.rabbiter.hospital.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;


@TableName("patient")//这个注解告诉 MyBatis-Plus，该类对应数据库中的 patient 表
public class Patient {
    @TableId(value = "p_id")//指定 pId 是表 patient 中的主键字段，字段名为 p_id
    @JsonProperty("pId")//用于指定序列化和反序列化时的 JSON 字段名为 pId
    private int pId;
    @JsonProperty("pPassword")
    //@TableField(select = false)
    private String pPassword;//患者的密码
    @JsonProperty("pName")
    private String pName;//患者的姓名
    @JsonProperty("pGender")
    private String pGender;//患者的性别
    @JsonProperty("pCard")
    private String pCard;//患者的身份证号或医疗卡号
    @JsonProperty("pEmail")
    private String pEmail;//患者的电子邮件地址
    @JsonProperty("pPhone")
    private String pPhone;//患者的电话号码
    @JsonProperty("pState")
    private Integer pState;//患者的状态（可能表示是否激活或某种状态）
    @JsonProperty("pBirthday")
    private String pBirthday;//患者的生日
    @JsonProperty("pAge")
    private Integer pAge;//患者的年龄

    public Patient() {//无参构造函数：默认构造函数，允许创建一个空的 Patient 对象
    }

    //全参构造函数：用于初始化 Patient 对象的所有字段
    public Patient(int pId, String pPassword, String pName, String pGender, String pCard, String pEmail, String pPhone, Integer pState, String pBirthday, Integer pAge) {
        this.pId = pId;
        this.pPassword = pPassword;
        this.pName = pName;
        this.pGender = pGender;
        this.pCard = pCard;
        this.pEmail = pEmail;
        this.pPhone = pPhone;
        this.pState = pState;
        this.pBirthday = pBirthday;
        this.pAge = pAge;
    }

    //提供了每个字段的 getter 和 setter 方法，用于获取和设置对应字段的值
    public int getPId() {
        return pId;
    }

    public void setPId(int pId) {
        this.pId = pId;
    }

    public String getPPassword() {
        return pPassword;
    }

    public void setPPassword(String pPassword) {
        this.pPassword = pPassword;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getPGender() {
        return pGender;
    }

    public void setPGender(String pGender) {
        this.pGender = pGender;
    }

    public String getPCard() {
        return pCard;
    }

    public void setPCard(String pCard) {
        this.pCard = pCard;
    }

    public String getPEmail() {
        return pEmail;
    }

    public void setPEmail(String pEmail) {
        this.pEmail = pEmail;
    }

    public String getPPhone() {
        return pPhone;
    }

    public void setPPhone(String pPhone) {
        this.pPhone = pPhone;
    }

    public Integer getPState() {
        return pState;
    }

    public void setPState(Integer pState) {
        this.pState = pState;
    }

    public String getPBirthday() {
        return pBirthday;
    }

    public void setPBirthday(String pBirthday) {
        this.pBirthday = pBirthday;
    }

    public Integer getPAge() {
        return pAge;
    }

    public void setPAge(Integer pAge) {
        this.pAge = pAge;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "pId=" + pId +
                ", pPassword='" + pPassword + '\'' +
                ", pName='" + pName + '\'' +
                ", pGender='" + pGender + '\'' +
                ", pCard='" + pCard + '\'' +
                ", pEmail='" + pEmail + '\'' +
                ", pPhone='" + pPhone + '\'' +
                ", pState=" + pState +
                ", pBirthday='" + pBirthday + '\'' +
                ", pAge=" + pAge +
                '}';
    }
}
