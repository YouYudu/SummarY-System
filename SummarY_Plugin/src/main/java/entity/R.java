package entity;


import java.io.Serializable;

/**
 * 后端统一返回结果
 */
public class R {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private String data; //数据

    public R(Integer code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public R() {

    }
    public static R error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
