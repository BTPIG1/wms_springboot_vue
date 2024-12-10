package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Menu;
import com.wms.entity.User;
import com.wms.service.MenuService;
import com.wms.service.UserService;
import freemarker.template.utility.StringUtil;
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

    @Autowired
    private MenuService menuService;



    @GetMapping("/list")
    public List<User> list(){
        return userService.list();
    }

    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no){
        List list = userService.lambdaQuery().eq(User::getNo,no).list();
        System.out.println("Records: " + list);
        return list.size()>0?Result.suc(list):Result.fail();
    }

    // 新增
    @PostMapping("/save")
    public Result save(@RequestBody User user){
        return userService.save(user)?Result.suc():Result.fail();
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        List list = userService.lambdaQuery()
                .eq(User::getNo,user.getNo())
                .eq(User::getPassword,user.getPassword()).list();

        if(list.size()>0){
            User user1 = (User) list.get(0);
            List menuList = menuService.lambdaQuery().like(Menu::getMenuright,user1.getRoleId()).list();
            HashMap map = new HashMap();
            map.put("user",user1);
            map.put("menu",menuList);
            return Result.suc(map);
        }

        return Result.fail();
    }

    // 修改
    @PostMapping("/update")
    public Result update(@RequestBody User user){
        return userService.updateById(user)?Result.suc():Result.fail();
    }

    // 新增或修改
    @PostMapping("/saveOrUpdate")
    public boolean saveOrUpdate(@RequestBody User user){
        return userService.saveOrUpdate(user);
    }

    // 删除
    @GetMapping("/del")
    public Result remove(@RequestParam Integer id){
        return userService.removeById(id)?Result.suc():Result.fail();
    }

    // 模糊查询
    @PostMapping("/listP")
    public Result listP(@RequestBody User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(user.getName())){
            lambdaQueryWrapper.like(User::getName,user.getName());
        }
        if(user.getAge()!=null)lambdaQueryWrapper.like(User::getAge, user.getAge());
        return Result.suc(userService.list(lambdaQueryWrapper));
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
        if(params.get("age")!=null && !"".equals(params.get("age"))) lambdaQueryWrapper.like(User::getAge, params.get("age"));
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
    @PostMapping("/listPageLR")
    public Result listPageLR(@RequestBody QueryPageParam query) {

        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap params = query.getParams();
        String name = (String)params.get("name");
        String sex = (String)params.get("sex");
        String roleId = (String)params.get("roleId");

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(User::getName,name);
        }
        if(StringUtils.isNotBlank(sex)){
            lambdaQueryWrapper.eq(User::getSex,sex);
        }
        if(StringUtils.isNotBlank(roleId)){
            lambdaQueryWrapper.eq(User::getRoleId,roleId);
        }

        //IPage result = userService.pageC(page);
        IPage result = userService.pageLR(page,lambdaQueryWrapper);
        System.out.println("total=="+result.getTotal());
        return Result.suc(result.getRecords(),result.getTotal());
    }

}
