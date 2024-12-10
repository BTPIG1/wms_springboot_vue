package com.wms.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RecordRes extends Record{
    @ApiModelProperty(value = "申请人名")
    private String username;

    @ApiModelProperty(value = "操作人名")
    private String adminname;

    @ApiModelProperty(value = "商品")
    private String goodsname;

    @ApiModelProperty(value = "仓库名")
    private String storagename;

    @ApiModelProperty(value = "商品分类")
    private String goodstypename;

    @ApiModelProperty(value = "出入库")
    private String action;
}

