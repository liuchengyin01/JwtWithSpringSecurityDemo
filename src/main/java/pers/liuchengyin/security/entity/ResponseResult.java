package pers.liuchengyin.security.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ResponseResult
 * @Description 返回结果对象
 * @Author 柳成荫
 * @Date 2021/3/25
 */
@Data
public class ResponseResult {
    private ResponseResult(){}
    /** 是否成功 */
    private Boolean success;
    /** 状态码 */
    private Integer code;
    /** 返回消息 */
    private String message;
    /** 返回的数据 */
    private Map<String,Object> data = new HashMap<>();

    /**
     * 提供工具方法
     * @return responseResult
     */
    public static ResponseResult ok(){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(true);
        responseResult.setCode(ResultCode.SUCCESS);
        responseResult.setMessage("成功");
        return responseResult;
    }

    /**
     * 失败/错误
     * @return 失败
     */
    public static ResponseResult error(){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(false);
        responseResult.setCode(ResultCode.ERROR);
        responseResult.setMessage("请求失败");
        return responseResult;
    }

    /**
     * 未认证
     * @return 错误
     */
    public static ResponseResult unauthorized(){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(false);
        responseResult.setCode(ResultCode.UNAUTHORIZED);
        responseResult.setMessage("未认证");
        return responseResult;
    }

    /**
     * 无权访问
     * @return 错误
     */
    public static ResponseResult noPermission(){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(false);
        responseResult.setCode(ResultCode.NO_PERMISSION);
        responseResult.setMessage("无权访问");
        return responseResult;
    }

    public ResponseResult success(Boolean success){
        this.setSuccess(success);
        return this;
    }
    public ResponseResult message(String message){
        this.setMessage(message);
        return this;
    }
    public ResponseResult code(Integer code){
        this.setCode(code);
        return  this;
    }
    public ResponseResult data(String key,Object value){
        this.data.put(key,value);
        return this;
    }
    public ResponseResult data(Map<String,Object> map){
        this.setData(map);
        return this;
    }

}