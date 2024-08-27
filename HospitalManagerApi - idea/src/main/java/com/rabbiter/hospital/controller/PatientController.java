/*用于处理与患者相关的HTTP请求。
这个控制器类提供了一些API接口，供客户端进行
患者登录、查询医生信息、添加挂号信息、查询挂号、添加患者信息、生成PDF文件以及统计患者年龄分布等操作*/
package com.rabbiter.hospital.controller;

import com.rabbiter.hospital.mapper.OrderMapper;
import com.rabbiter.hospital.pojo.Orders;
import com.rabbiter.hospital.pojo.Patient;
import com.rabbiter.hospital.service.DoctorService;
import com.rabbiter.hospital.service.OrderService;
import com.rabbiter.hospital.service.PatientService;
import com.rabbiter.hospital.utils.JwtUtil;
import com.rabbiter.hospital.utils.PdfUtil;
import com.rabbiter.hospital.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController//这是一个组合注解，相当于@Controller和@ResponseBody的结合，表示这个类的所有方法都会返回数据而不是视图
@RequestMapping("patient")//将该控制器映射到/patient路径下，所有的方法将会根据具体的URL路径和HTTP方法来映射对应的请求
public class PatientController {

    @Autowired//用于自动注入DoctorService、PatientService、OrderService和JedisPool对象，这些服务类和Redis连接池是通过Spring容器管理的
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;
    @Resource
    private OrderMapper orderMapper;

    /**
     * 该方法用于验证患者的登录信息。
     * 接收 pId 和 pPassword 参数，并调用 PatientService 的 login 方法进行验证。
     * 如果登录成功，生成一个 JWT Token 并返回给客户端；否则返回失败消息
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData login(@RequestParam(value = "pId") int pId, @RequestParam(value = "pPassword") String pPassword) {
        Patient patient = this.patientService.login(pId, pPassword);
        if (patient != null) {
            Map<String,String> map = new HashMap<>();
            map.put("pName", patient.getPName());
            map.put("pId", String.valueOf(patient.getPId()));
            map.put("pCard", patient.getPCard());
            String token = JwtUtil.getToken(map);
            map.put("token", token);
            //response.setHeader("token", token);
            return ResponseData.success("登录成功", map);
        } else {
            return ResponseData.fail("登录失败，密码或账号错误");
        }
    }
    /**
     * 根据科室查询所有医生信息
     * 该方法接收科室名称 dSection 作为参数，并调用 DoctorService 的 findDoctorBySection 方法获取医生信息
     */
    @RequestMapping("findDoctorBySection")
    public ResponseData findDoctorBySection(@RequestParam(value = "dSection") String dSection){
        return ResponseData.success("根据科室查询所有医生信息成功", this.doctorService.findDoctorBySection(dSection));
    }
    /**
     * 增加挂号信息
     * 该方法接收挂号信息（Orders 对象）和预约 ID（arId），
     * 调用 OrderService 的 addOrder 方法来添加挂号信息，并返回操作结果
     */
    @RequestMapping("addOrder")//映射到/patient/addOrder路径，接受POST请求
    @ResponseBody
    public ResponseData addOrder(Orders order, String arId){//方法接收挂号信息（Orders对象）和一个预约ID（arId），调用OrderService的addOrder方法来添加挂号信息，并返回操作结果
        System.out.println(arId);
        if (this.orderService.addOrder(order, arId))
        return ResponseData.success("插入挂号信息成功");
        return ResponseData.fail("插入挂号信息失败");
    }
    /**
     * 根据pId查询挂号
     * 该方法接收患者 ID pId 作为参数，调用 OrderService 的 findOrderByPid 方法查询挂号信息
     */
    @RequestMapping("findOrderByPid")
    public ResponseData findOrderByPid(@RequestParam(value = "pId") int pId){
        return ResponseData.success("返回挂号信息成功", this.orderService.findOrderByPid(pId)) ;
    }

    /**
     * 增加患者信息
     * 该方法接收一个 Patient 对象，调用 PatientService 的 addPatient 方法将患者信息插入数据库，并返回操作结果
     */
    @RequestMapping("addPatient")
    @ResponseBody
    public ResponseData addPatient(Patient patient) {
        Boolean bo = this.patientService.addPatient(patient);
        if (bo) {
            return ResponseData.success("注册成功");
        }
        return ResponseData.fail("注册失败！账号或邮箱已被占用");
    }
    @GetMapping("/pdf")
    public void downloadPDF(HttpServletRequest request, HttpServletResponse response, int oId) throws Exception {
        Orders order = this.orderMapper.findOrderByOid(oId);
        PdfUtil.ExportPdf(request, response, order);
    }
    /**
     * 统计患者男女人数
     */
    @RequestMapping("patientAge")
    public ResponseData patientAge(){
        return  ResponseData.success("统计患者男女人数成功", this.patientService.patientAge());

    }
}
