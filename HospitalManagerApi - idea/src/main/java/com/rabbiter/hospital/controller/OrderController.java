package com.rabbiter.hospital.controller;

import com.rabbiter.hospital.pojo.Orders;
import com.rabbiter.hospital.service.OrderService;
import com.rabbiter.hospital.utils.ResponseData;
import com.rabbiter.hospital.utils.TodayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController// 表明该类是一个控制器，它会处理 HTTP 请求并返回 JSON 或 XML 格式的响应
@RequestMapping("order")// 指定该控制器处理所有以 /order 开头的请求
public class OrderController {
    @Autowired  //使用依赖注入的方式将 OrderService 注入到 OrderController 中，OrderService 是处理订单相关业务逻辑的服务类
    private OrderService orderService;
    /**
     * 根据id更新挂号信息
     */
    @PostMapping("updateOrder")// 处理 /order/updateOrder 的 POST 请求
    @ResponseBody
    public ResponseData updateOrder(@RequestBody Orders orders) { //@RequestBody Orders orders：从请求体中获取一个 Orders 对象，用于更新订单
        if (this.orderService.updateOrder(orders)) // 调用 OrderService 的 updateOrder 方法更新订单信息，并根据结果返回成功或失败的响应
            return ResponseData.success("更新挂号信息成功");

        return ResponseData.fail("更新挂号信息失败！");
    }
    /**
     * 根据id设置缴费状态
     */
    @RequestMapping("updatePrice") // 处理 /order/updatePrice 请求
    public ResponseData updatePrice(int oId){ // 根据订单 ID (oId) 设置订单的缴费状态
        if (this.orderService.updatePrice(oId))
        return ResponseData.success("根据id设置缴费状态成功");
        return ResponseData.success("根据id设置缴费状态失败");
    }
    /**
     * 查找医生已完成的挂号单
     */
    @RequestMapping("findOrderFinish") // 处理 /order/findOrderFinish 请求
    public ResponseData findOrderFinish(int pageNumber, int size, String query, int dId){ // 分页查询指定医生的已完成挂号单
        return ResponseData.success("查找医生已完成的挂号单完成！", this.orderService.findOrderFinish(pageNumber, size, query, dId));
    }
    /**
     * 根据dId查询挂号
     */
    @RequestMapping("findOrderByDid") // 处理 /order/findOrderByDid 请求
    public ResponseData findOrderByDid(int pageNumber, int size, String query, int dId){ // 分页查询指定医生的挂号信息
        return ResponseData.success("返回挂号信息成功", this.orderService.findOrderByDid(pageNumber, size, query, dId)) ;
    }
    /**
     * 统计今天挂号人数
     */
    @RequestMapping("orderPeople") // 处理 /order/orderPeople 请求
    public ResponseData oderPeople(){ // 统计当天的挂号人数
        String oStart = TodayUtil.getTodayYmd();
        return ResponseData.success("统计今天挂号人数成功", this.orderService.orderPeople(oStart));
    }
    /**
     * 统计今天某个医生挂号人数
     */
    @RequestMapping("orderPeopleByDid") // 处理 /order/orderPeopleByDid 请求
    public ResponseData orderPeopleByDid(int dId){
        String oStart = TodayUtil.getTodayYmd();
        return ResponseData.success("统计今天挂号人数成功", this.orderService.orderPeopleByDid(oStart, dId));
    }
    /**
     * 获取近二十天的挂号人数
     */
    @RequestMapping("orderSeven")
    public ResponseData orderSeven(){
        ArrayList<Integer> list = new ArrayList<>();
        String oStart = null;
        for(int i = 10; i > -10; i--){
            oStart = TodayUtil.getPastDate(i);
            int people = this.orderService.orderPeople(oStart);
            list.add(people);
        }
        return ResponseData.success("获取近20天的挂号人数成功", list);
    }
    /**
     * 统计挂号男女人数
     */
    @RequestMapping("orderGender")
    public ResponseData orderGender(){
        return ResponseData.success("统计挂号男女人数", this.orderService.orderGender());
    }
    /**
     * 增加诊断及医生意见
     */
    @PostMapping("updateOrderByAdd")
    @ResponseBody
    public ResponseData updateOrderByAdd(@RequestBody Orders order){
        if (this.orderService.updateOrderByAdd(order))
            return ResponseData.success("增加诊断及医生意见成功");
        return ResponseData.fail("增加诊断及医生意见失败");
    }
    /**
     * 判断诊断之后再次购买药物是否已缴费
     */
    @RequestMapping("findTotalPrice")
    public ResponseData findTotalPrice(int oId){
       if(this.orderService.findTotalPrice(oId))
           return ResponseData.success("未缴费");
       return ResponseData.fail("无需缴费");
    }
    /**
     * 请求挂号时间段
     */
    @RequestMapping("findOrderTime")
    public ResponseData findOrderTime(String arId){
        return ResponseData.success("请求挂号时间段成功", this.orderService.findOrderTime(arId));

    }
    /**
     * 统计过去20天挂号科室人数
     */
    @RequestMapping("orderSection")
    public ResponseData orderSection(){
        return ResponseData.success("统计近20天挂号科室人数成功", this.orderService.orderSection());
    }

}
