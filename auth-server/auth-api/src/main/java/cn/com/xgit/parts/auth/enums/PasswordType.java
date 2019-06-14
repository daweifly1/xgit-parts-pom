package cn.com.xgit.parts.auth.enums;

/**
 * 密码类型
 */
public enum PasswordType {
    NORMAL(0, "普通登录"),
    QUERY(1, "查询"),
    DYNAMIC(2, "手机动态密码，可以仅存缓存"),
    DYNAMIC_EMAIL(3, "email动态密码，可以仅存缓存");

    private int type;
    private String desc;


    PasswordType(int type, String desc) {
        this.desc = desc;
        this.type = type;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getType() {
        return this.type;
    }

    public boolean contain(Integer type) {
        if (null == type) {
            return false;
        }
        for (PasswordType t : PasswordType.values()) {
            if (type.intValue() == t.type) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是动态密码校验
     *
     * @param type
     * @return
     */
    public boolean isDynamicType(Integer type) {
        if (null == type) {
            return false;
        }
        return PasswordType.DYNAMIC.type == type || PasswordType.DYNAMIC_EMAIL.type == type;
    }
}
