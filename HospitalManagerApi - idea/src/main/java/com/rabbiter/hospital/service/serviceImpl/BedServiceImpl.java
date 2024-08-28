package com.rabbiter.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.hospital.mapper.BedMapper;
import com.rabbiter.hospital.pojo.Bed;
import com.rabbiter.hospital.service.BedService;
import com.rabbiter.hospital.utils.TodayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service("BedService")
public class BedServiceImpl implements BedService {

    @Resource
    private BedMapper bedMapper;//自动注入 BedMapper，用于执行与床位相关的数据库操作

    /**
     * 查找所有空床位
     */
    @Override
    public List<Bed> findNullBed(){
        QueryWrapper<Bed> wrapper = new QueryWrapper<>();
        wrapper.select("b_id").eq("b_state", 0);//查找所有空床位（即状态为 b_state = 0 的床位）
        return this.bedMapper.selectList(wrapper);
    }

    @Override
    /**
     * 更新床位信息
     */
    public Boolean updateBed(Bed bed){
        Bed bed1 = this.bedMapper.selectById(bed.getBId());
        if (bed1.getBState() == 1)//如果床位已被占用（b_state = 1），则返回 false
            return false;
        //更新床位的开始日期、状态和版本号
        bed.setBStart(TodayUtil.getTodayYmd());
        bed.setBState(1);
        bed.setVersion(bed1.getVersion());
        //将修改后的床位信息保存到数据库中
        this.bedMapper.updateById(bed);
        return true;
    }
    /**
     * 根据pId查询挂号
     */
    public List<Bed> findBedByPid(int pId){
        QueryWrapper<Bed> wrapper = new QueryWrapper<>();
        wrapper.eq("p_id", pId);
        return this.bedMapper.selectList(wrapper);
    }
    /**
     * 分页模糊查询所有检查信息
     * int pageNumber: 当前页码，决定从哪一页开始查询。
     * int size: 每页显示的记录数。
     * String query: 查询条件，用于模糊匹配 p_id（病人 ID）。
     */
    @Override
    public HashMap<String, Object> findAllBeds(int pageNumber, int size, String query) {
        Page<Bed> page = new Page<>(pageNumber, size);
        QueryWrapper<Bed> wrapper = new QueryWrapper<>();
        wrapper.like("p_id", query);
        IPage<Bed> iPage = this.bedMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("size", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("beds", iPage.getRecords()); //查询到的记录
        return hashMap;
    }
    /**
     * 根据id查找检查
     */
    @Override
    public Bed findBed(int bId){
        return this.bedMapper.selectById(bId);
    }
    /**
     * 增加床位信息
     */
    @Override
    public Boolean addBed(Bed bed){
        //如果账号已存在则返回false
        List<Bed> beds = this.bedMapper.selectList(null);//使用 bedMapper 的 selectList(null) 方法从数据库中获取所有的床位记录，并将这些记录存储在一个 List<Bed> 中。
        for (Bed bed1 : beds) {//遍历 beds 列表，检查新添加的床位 bed 的 ID 是否与已有床位的 ID 重复。如果找到相同的 bId，则返回 false，表示床位已经存在，无法添加
            if (bed1.getBId() == bed.getBId()) {
                return false;
            }
        }
        bed.setBState(0);//将新床位的状态设置为 0，表示该床位是空闲的
        this.bedMapper.insert(bed);
        return true;
    }
    /**
     * 删除床位信息
     */
    @Override
    public Boolean deleteBed(int bId) {
        this.bedMapper.deleteById(bId);
        return true;
    }
    /**
     * 清空床位信息
     */
    public Boolean emptyBed(int bId){
        UpdateWrapper<Bed> wrapper = new UpdateWrapper<>();
        wrapper.set("p_id", -1).set("d_id", -1).set("b_reason", null).set("b_start", null).set("b_state", 0).eq("b_id", bId);
        this.bedMapper.update(null, wrapper);
        return true;

    }
    /**
     * 统计今天住院人数
     */
    @Override
    public int bedPeople(String bStart){
        return this.bedMapper.bedPeople(bStart);
    }

}
