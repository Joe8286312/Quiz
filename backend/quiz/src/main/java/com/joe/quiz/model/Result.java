package com.joe.quiz.model;

public class Result {
    private Integer code; //1是成功，0是失败；
    private String msg; //成功消息或者失败消息；
    private Object data; //放数据；
    //...get,set,toString
    //构造方法；
    //静态success方法和error方法；

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result success(Object data) {
        return new Result(1, "success", data);
    }

    public static Result success(String msg) {
        return new Result(1, msg, null);
    }

    public static Result success() {
        return new Result(1, "success", null);
    }

    public static Result error() {
        return new Result(0, "error", null);
    }
    public static Result error(String msg) {
        return new Result(0, msg, null);
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code = " + code +
                ",msg = " + msg + '\'' +
                ",data = " + data +
                "}";
    }

}


//package com.joe.quiz.model;
//
//import lombok.Data;
//
//@Data
//public class Result {
//    private Integer code; // 响应码，200表示成功，其他表示失败
//    private String msg;   // 响应信息
//    private Object data;  // 响应数据
//
//    public static Result success() {
//        Result result = new Result();
//        result.setCode(200);
//        result.setMsg("success");
//        return result;
//    }
//
//    public static Result success(Object data) {
//        Result result = new Result();
//        result.setCode(200);
//        result.setMsg("success");
//        result.setData(data);
//        return result;
//    }
//
//    public static Result success(String message) {
//        Result result = new Result();
//        result.setCode(200);
//        result.setMsg(message);
//        return result;
//    }
//
//    public static Result success(String message, Object data) {
//        Result result = new Result();
//        result.setCode(200);
//        result.setMsg(message);
//        result.setData(data);
//        return result;
//    }
//
//    public static Result error(String message) {
//        Result result = new Result();
//        result.setCode(500);
//        result.setMsg(message);
//        return result;
//    }
//}