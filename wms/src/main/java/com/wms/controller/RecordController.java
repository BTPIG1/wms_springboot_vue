package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Record;
import com.wms.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2024-12-05
 */
@RestController
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private RecordService recordService;



    @GetMapping("/list")
    public Result list(){
        List list = recordService.list();
        return Result.suc(list);
    }


    // 新增
    @PostMapping("/save")
    public Result save(@RequestBody Record record){
        return recordService.save(record)?Result.suc():Result.fail();
    }



    // 修改
    @PostMapping("/update")
    public Result update(@RequestBody Record record){
        return recordService.updateById(record)?Result.suc():Result.fail();
    }



    // 删除
    @GetMapping("/del")
    public Result remove(@RequestParam Integer id){
        return recordService.removeById(id)?Result.suc():Result.fail();
    }

    // lambdaQueryWrapper + 分页
    @PostMapping("/listPageLR")
    public Result listPageLR(@RequestBody QueryPageParam query) {
        HashMap params = query.getParams();
        String goodsname = (String)params.get("goodsname"); // 商品名
        String storage = (String)params.get("storage"); // 仓库id
        String goodstype = (String)params.get("goodstype"); // 类型id
        String roleId = (String)params.get("roleId"); // 类型id
        String userid = (String)params.get("userid"); // 类型id

        Page<Record> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        // 由于sql语句使用了多表联查，这里不适合用LambdaQueryWrapper，应用其子类
//        LambdaQueryWrapper<Record> lambdaQueryWrapper = new LambdaQueryWrapper();
        QueryWrapper<Record> queryWrapper = new QueryWrapper(); // LambdaQueryWrapper实际上是一个替换动态where的功能  用法：lambda/like/eq(数据库属性名,类属性)
        queryWrapper.apply("g.id=a.goods and g.storage = s.id and g.goodsType = gt.id");

        if("2".equals(roleId)){
            queryWrapper.apply("a.userId= "+userid);
        }

        if(StringUtils.isNotBlank(goodsname) && !"null".equals(goodsname)) queryWrapper.like("g.name",goodsname);
        if(StringUtils.isNotBlank(storage) && !"null".equals(storage)) queryWrapper.eq("s.id",storage);
        if(StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)) queryWrapper.eq("gt.id",goodstype);

        IPage result = recordService.pageLR(page, queryWrapper);
        return Result.suc(result.getRecords(),result.getTotal() );
    }
}
