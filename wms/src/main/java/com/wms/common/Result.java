package com.wms.common;

import lombok.Data;

@Data
public class Result {
    private int code;
    private String message;
    private Long total;
    private Object data;

    public static Result fail(){
        return result(500, "失败", 0L, null);
    }

    public static Result suc(){
        return result(200, "成功", 0L, null);
    }

    public static Result suc(Object data){
        return result(200, "成功", 0L, data);
    }

    public static Result suc(Long total, Object data){
        return result(200, "成功", total, data);
    }

    private static Result result(int code, String message, Long total, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setTotal(total);
        result.setData(data);
        return result;
    }


}
