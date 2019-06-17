package cn.com.xgit.gw.social.enums;

public enum OAuthTypes {
    GITHUB(1, "github"), QQ(2, "QQ"), WX(3, "wx");
    private int id;
    private String code;

    OAuthTypes(int i, String code) {
        id = i;
        code = code;
    }

    public int getId() {
        return this.id;
    }


    public String getCode() {
        return this.code;
    }
}
