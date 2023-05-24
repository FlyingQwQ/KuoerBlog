package top.kuoer.enums;

public enum ResultCode {
    SUCCESS(1, "成功"),
    NOAUTH(2, "权限不足"),
    OPERATIONFAIL(3, "操作失败"),
    NOTFOUND(4, "找不到数据");

    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
