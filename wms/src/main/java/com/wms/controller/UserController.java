package com.wms.controller;


import com.wms.entity.User;
import com.wms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2024-11-15
 */
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

}
