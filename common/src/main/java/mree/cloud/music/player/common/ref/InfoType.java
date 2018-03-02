package mree.cloud.music.player.common.ref;

/**
 * Created by eercan on 20.10.2017.
 */

public enum InfoType {

    INFO(0, "Info"),
    TUTORIAL(1, "Tutorial"),
    POLICY(2, "Policy"),
    OTHER(3, "Other");


    private Integer code;
    private String desc;

    InfoType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static InfoType get(Integer code) {

        if (code == null) {
            return null;
        }

        for (InfoType ot : values()) {
            if (ot.code == code) {
                return ot;
            }
        }
        throw new IllegalArgumentException("No matching type: " + code);
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
