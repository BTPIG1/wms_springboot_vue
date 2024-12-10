package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goodstype;
import com.wms.service.GoodstypeService;
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
@RequestMapping("/goodstype")
public class GoodstypeController {
    @Autowired
    private GoodstypeService goodstypeService;



    @GetMapping("/list")
    public Result list(){
        List list = goodstypeService.list();
        return Result.suc(list);
    }


    // 新增
    @PostMapping("/save")
    public Result save(@RequestBody Goodstype goodstype){
        return goodstypeService.save(goodstype)?Result.suc():Result.fail();
    }



    // 修改
    @PostMapping("/update")
    public Result update(@RequestBody Goodstype goodstype){
        return goodstypeService.updateById(goodstype)?Result.suc():Result.fail();
    }

    // 新增或修改
    @PostMapping("/saveOrUpdate")
    public boolean saveOrUpdate(@RequestBody Goodstype goodstype){
        return goodstypeService.saveOrUpdate(goodstype);
    }

    // 删除
    @GetMapping("/del")
    public Result remove(@RequestParam Integer id){
        return goodstypeService.removeById(id)?Result.suc():Result.fail();
    }

    // lambdaQueryWrapper + 分页
    @PostMapping("/listPageLR")
    public Result listPageLR(@RequestBody QueryPageParam query) {

        Page<Goodstype> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Goodstype> lambdaQueryWrapper = new LambdaQueryWrapper();
        HashMap params = query.getParams();

        String name = (String) params.get("name");
        System.out.println(name);

        if(StringUtils.isNotBlank(name) && !"null".equals(name))lambdaQueryWrapper.like(Goodstype::getName, params.get("name"));

        IPage result = goodstypeService.pageLR(page, lambdaQueryWrapper);
        System.out.println("Total: " + result.getTotal());
        System.out.println("Records: " + result.getRecords());
        return Result.suc(result.getRecords(),result.getTotal() );
    }

}
