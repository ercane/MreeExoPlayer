package mree.cloud.music.player.common.ref;

/**
 * Created by eercan on 12.12.2017.
 */

public enum CoverSourceType {
    RESOURCE(0, "Resource"),
    BYTE_ARRAY(1, "Byte array"),
    BITMAP(2, "Bitmap"),
    URL(3, "Url"),
    NONE(-1, "NONE");

    private Integer code;
    private String desc;

    CoverSourceType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CoverSourceType get(Integer code) {

        if (code == null) {
            return null;
        }

        for (CoverSourceType ot : values()) {
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
