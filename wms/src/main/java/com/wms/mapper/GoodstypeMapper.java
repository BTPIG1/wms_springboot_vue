package com.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wms.entity.Goodstype;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.entity.Storage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wms
 * @since 2024-12-04
 */
@Mapper
public interface GoodstypeMapper extends BaseMapper<Goodstype> {
    // @Param(Constants.WRAPPER) Wrapper wrapper 固定写法
    IPage pageLR(IPage<Goodstype> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
