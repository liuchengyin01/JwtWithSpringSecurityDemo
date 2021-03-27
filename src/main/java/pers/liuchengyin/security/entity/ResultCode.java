package pers.liuchengyin.security.entity;

/**
 * @ClassName ResultCode
 * @Description 状态码
 * @Author 柳成荫
 * @Date 2021/3/25
 */
public interface ResultCode {
    /** 成功状态码 */
    Integer SUCCESS = 20000;
    /** 失败的状态码 */
    Integer ERROR = 20001;
    /** 未认证的状态码 */
    Integer UNAUTHORIZED = 401;
    /** 无权限访问 */
    Integer NO_PERMISSION = 403;
}
