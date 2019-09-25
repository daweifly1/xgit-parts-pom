package cn.com.xgit.gw.enums;

public enum SystemEnum {
    BASE(1, "基础平台"),
    SHOP(2, "电商平台"),
    STORAGE(3, "仓储平台");

    private int code;
    private String name;

    SystemEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public long getLongCode() {
        return code + 0L;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
