package com.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wms
 * @since 2024-11-15
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 不用管这个爆红，返回类型是一个entity对象的list集合
    IPage pageC(IPage<User> page);

    // @Param(Constants.WRAPPER) Wrapper wrapper 固定写法
    IPage pageL(IPage<User> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
