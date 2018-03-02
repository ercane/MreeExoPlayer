package mree.cloud.music.player.common.ref;

/**
 * Created by mree on 20.04.2017.
 */

public enum ItemType {
    FOLDER(0, "Folder"),
    FILE(1, "File");

    private Integer code;
    private String desc;

    ItemType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ItemType get(Integer code) {

        if (code == null) {
            return null;
        }

        for (ItemType ot : values()) {
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
