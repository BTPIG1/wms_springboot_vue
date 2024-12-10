package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.*;
import com.wms.entity.Record;
import com.wms.service.GoodsService;
import com.wms.service.GoodstypeService;
import com.wms.service.RecordService;
import com.wms.service.StorageService;
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
 * @since 2024-12-04
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RecordService recordService;

    @GetMapping("/list")
    public List<Goods> list(){
        return goodsService.list();
    }



    // 新增
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods){
        return goodsService.save(goods)?Result.suc():Result.fail();
    }

    // 入库
    @PostMapping("/inGoods")
    public Result inGoods(@RequestBody RecordRes recordRes){

        if("2".equals(recordRes.getAction())){
            recordRes.setCount(recordRes.getCount()*(-1));
        }
        recordService.save(recordRes);
        Goods goods = goodsService.getById(recordRes.getGoods());
        goods.setCount(goods.getCount()+recordRes.getCount());
        return goodsService.updateById(goods)?Result.suc():Result.fail();
    }


    // 修改
    @PostMapping("/update")
    public Result update(@RequestBody Goods goods){
        return goodsService.updateById(goods)?Result.suc():Result.fail();
    }

    // 新增或修改
    @PostMapping("/saveOrUpdate")
    public boolean saveOrUpdate(@RequestBody Goods goods){
        return goodsService.saveOrUpdate(goods);
    }

    // 删除
    @GetMapping("/del")
    public Result remove(@RequestParam Integer id){
        return goodsService.removeById(id)?Result.suc():Result.fail();
    }

    // lambdaQueryWrapper + 分页
    @PostMapping("/listPageLR")
    public Result listPageLR(@RequestBody QueryPageParam query) {
        HashMap param = query.getParams();
        String name = (String)param.get("name");
        String goodstype = (String)(param.get("goodstype"));
        String storage = (String)(param.get("storage"));

        Page<Goods> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(Goods::getName,name);
        }
        if(StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)){
            lambdaQueryWrapper.eq(Goods::getGoodstype,goodstype);
        }
        if(StringUtils.isNotBlank(storage) && !"null".equals(storage)){
            lambdaQueryWrapper.eq(Goods::getStorage,storage);
        }

        IPage result = goodsService.pageLR(page,lambdaQueryWrapper);
        return Result.suc(result.getRecords(),result.getTotal());
    }
}
