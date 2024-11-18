package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.User;
import com.wms.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
 * @since 2024-11-15
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;



    @GetMapping("/list")
    public List<User> list(){
        return userService.list();
    }

    // 新增
    @PostMapping("/save")
    public boolean save(@RequestBody User user){
        return userService.save(user);
    }

    // 修改
    @PostMapping("/update")
    public boolean update(@RequestBody User user){
        return userService.updateById(user);
    }

    // 新增或修改
    @PostMapping("/saveOrUpdate")
    public boolean saveOrUpdate(@RequestBody User user){
        return userService.saveOrUpdate(user);
    }

    // 删除
    @GetMapping("/delete")
    public boolean remove(Integer id){
        return userService.removeById(id);
    }

    // 模糊查询
    @PostMapping("/listP")
    public List<User> listP(@RequestBody User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(User::getAge, user.getAge());
        return userService.list(lambdaQueryWrapper);
    }

    // 分页实现：必须要添加拦截器
    @PostMapping("/listPage")
//    public List<User> listPage(@RequestBody hashmap param) {  // 这种是无封装的写法，可以直接用param.get("pageSize")获取值
    public List<User> listPage(@RequestBody QueryPageParam query) {

        // 获取前端传递的参数
        /*System.out.println(param);
        System.out.println(param.getPageSize());
        System.out.println(param.getPageNum());
        HashMap params = param.getParams();
        System.out.println(params);*/


        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        HashMap params = query.getParams();
        /*
        * 思考如果不使用lambdaQueryWrapper，如何实现动态sql？
        *   那么只能在mapper层中编写动态sql
        * */
        if(params.get("age")!=null) lambdaQueryWrapper.like(User::getAge, params.get("age"));
        if(params.get("name")!=null)lambdaQueryWrapper.like(User::getName, params.get("name"));

        // 这里使用的IService的方法
        IPage result = userService.page(page, lambdaQueryWrapper);
        System.out.println(result.getTotal());
        return result.getRecords();
    }

    // 不想用IService的方法，自定义方法实现的分页（非lambdaQueryWrapper实现，需要mapper层编写动态sql）
    @PostMapping("/listPageC")
    public List<User> listPageC(@RequestBody QueryPageParam query) {

        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        // 实际上这里的分页操作被拦截器拦截了，所以这里的分页操作是自动实现的
        IPage result = userService.pageC(page);
        System.out.println(result.getTotal());
        return result.getRecords();
    }

    // lambdaQueryWrapper + 分页
    @PostMapping("/listPageL")
    public List<User> listPageL(@RequestBody QueryPageParam query) {

        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        HashMap params = query.getParams();
        if(params.get("age")!=null) lambdaQueryWrapper.like(User::getAge, params.get("age"));
        if(params.get("name")!=null)lambdaQueryWrapper.like(User::getName, params.get("name"));

        IPage result = userService.pageL(page, lambdaQueryWrapper);
        System.out.println(result.getTotal());
        return result.getRecords();
    }

    // lambdaQueryWrapper + 分页
    @PostMapping("/listPageLR")
    public Result listPageLR(@RequestBody QueryPageParam query) {

        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        HashMap params = query.getParams();
        if(params.get("age")!=null) lambdaQueryWrapper.like(User::getAge, params.get("age"));
        if(params.get("name")!=null)lambdaQueryWrapper.like(User::getName, params.get("name"));

        IPage result = userService.pageL(page, lambdaQueryWrapper);
        System.out.println(result.getTotal());
        return Result.suc(result.getTotal(), result.getRecords());
    }

}
