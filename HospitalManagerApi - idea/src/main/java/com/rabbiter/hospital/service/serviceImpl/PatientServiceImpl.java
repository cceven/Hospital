package com.rabbiter.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.hospital.mapper.PatientMapper;
import com.rabbiter.hospital.pojo.Patient;
import com.rabbiter.hospital.service.PatientService;
import com.rabbiter.hospital.utils.Md5Util;
import com.rabbiter.hospital.utils.TodayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("PatientService")
public class PatientServiceImpl implements PatientService {
    protected static final Logger Log = LoggerFactory.getLogger(PatientServiceImpl.class);//protected static final Logger Log = LoggerFactory.getLogger(PatientServiceImpl.class);: 使用SLF4J记录日志，可以在方法中记录操作日志或异常信息。

    @Resource
    private PatientMapper patientMapper;


    /**
     * 登录数据校验
     * 通过 pId 查找患者信息，如果找不到或状态为0（可能表示无效账户），则返回 null。
     * 使用 Md5Util.getMD5(pPassword) 将输入的密码进行MD5加密，
     * 然后与数据库中存储的密码进行比较，如果匹配则返回患者信息，否则返回 null
     * */
    @Override
    public Patient login(int pId, String pPassword){
        Patient patient = this.patientMapper.selectById(pId);
        if(patient == null || 0 == patient.getPState()) {
            return null;
        }
        String password = Md5Util.getMD5(pPassword);
        if ((patient.getPPassword()).equals(password)) {
            return patient;
        }
        return null;
    }
    /**
     * 分页模糊查询所有患者信息
     * 通过 Page 对象和 QueryWrapper 实现分页和模糊查询，条件是 p_name 字段包含查询字符串，并按 p_state 状态降序排列。
     * 返回一个包含总记录数、总页数、当前页数以及查询结果的哈希表
     */
    @Override//表示这个方法是实现 PatientService 接口中定义的方法
/*    HashMap<String, Object>: 方法返回类型是一个 HashMap，其中键是 String 类型，值是 Object 类型。这个哈希表将包含分页查询的结果，包括总记录数、总页数、当前页数以及查询到的患者记录。
    findAllPatients: 方法名称，表示查询所有患者信息。*/
/*int pageNumber: 指定查询的页码。
    int size: 指定每页显示的记录数。
    String query: 模糊查询的关键字，用于匹配患者的名字。*/
    public HashMap<String, Object> findAllPatients(int pageNumber, int size, String query) {
        Page<Patient> page = new Page<>(pageNumber, size);
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.like("p_name", query).orderByDesc("p_state");//wrapper.like("p_name", query): like 方法用于生成模糊查询条件，表示查询 p_name 字段包含 query 关键字的患者
        IPage<Patient> iPage = this.patientMapper.selectPage(page, wrapper);//传入分页对象 page 和查询条件 wrapper，执行查询操作并返回查询结果 iPage
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //将查询结果的总记录数 total 放入哈希表中，键为 "total"
        hashMap.put("pages", iPage.getPages());       //将总页数 pages 放入哈希表中，键为 "pages"
        hashMap.put("pageNumber", iPage.getCurrent());//将当前页码 pageNumber 放入哈希表中，键为 "pageNumber"
        hashMap.put("patients", iPage.getRecords()); //将查询到的患者记录 patients 放入哈希表中，键为 "patients"
        return hashMap;
    }

    /**
     * 删除患者信息
     * 将患者的 p_state 字段设置为0（表示删除或停用），并更新数据
     */
    @Override
    public Boolean deletePatient(int pId) {
        Patient patient = new Patient();
        patient.setPId(pId);
        patient.setPState(0);
        this.patientMapper.updateById(patient);
        return true;
    }
    /**
     * 根据患者id查询患者信息
     */
    @Override
    public Patient findPatientById(int pId){
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("p_id", pId);//通过 pId 使用 QueryWrapper 查询单个患者信息。
        return this.patientMapper.selectOne(wrapper);
    }

    /**
     * 增加患者信息
     * 首先检查数据库中是否已存在相同ID或邮箱的患者，如果存在返回 false。
     * 计算患者的年龄（根据出生年份和当前年份）。
     * 使用 Md5Util 对密码进行MD5加密，然后将加密后的密码、年龄以及状态信息设置到患者对象中，最后将患者信息插入数据库。
     * 如果成功插入，返回 true。
     */
    @Override
    public Boolean addPatient(Patient patient) {
        //如果账号已存在则返回false
        List<Patient> patients = this.patientMapper.selectList(null);
        for (Patient patient1 : patients) {
            if (patient.getPId() == patient1.getPId()) {
                return false;
            }
            if ((patient.getPEmail()).equals(patient1.getPEmail()) ){
                return false;
            }
        }
        int yourYear = Integer.parseInt(patient.getPBirthday().substring(0, 4));
        int todayYear = Integer.parseInt(TodayUtil.getTodayYmd().substring(0,4));
        //密码md5加密
        String password = Md5Util.getMD5(patient.getPPassword());
        patient.setPPassword(password);
        patient.setPAge(todayYear-yourYear);
        patient.setPState(1);
        this.patientMapper.insert(patient);
        return true;
    }
    /**
     * 统计患者男女人数
     */
    public List<Integer> patientAge(){
        List<Integer> ageList = new ArrayList<>();
        Integer age1 = this.patientMapper.patientAge(0, 9);
        Integer age2 = this.patientMapper.patientAge(10, 19);
        Integer age3 = this.patientMapper.patientAge(20, 29);
        Integer age4 = this.patientMapper.patientAge(30, 39);
        Integer age5 = this.patientMapper.patientAge(40, 49);
        Integer age6 = this.patientMapper.patientAge(50, 59);
        Integer age7 = this.patientMapper.patientAge(60, 69);
        Integer age8 = this.patientMapper.patientAge(70, 79);
        Integer age9 = this.patientMapper.patientAge(80, 89);
        Integer age10 = this.patientMapper.patientAge(90, 99);
        ageList.add(age1);
        ageList.add(age2);
        ageList.add(age3);
        ageList.add(age4);
        ageList.add(age5);
        ageList.add(age6);
        ageList.add(age7);
        ageList.add(age8);
        ageList.add(age9);
        ageList.add(age10);
        return ageList;

    }


    }


