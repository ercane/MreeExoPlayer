package mree.cloud.music.player.common.ref;

/**
 * Created by mree on 06.11.2015.
 */
public enum SourceType {

    LOCAL(0, "Local", "local"),
    ONEDRIVE(1, "Microsoft OneDrive", "onedrive"),
    DROPBOX(2, "Dropbox", "dropbox"),
    GOOGLE_DRIVE(3, "Google Drive", "google-drive"),
    SPOTIFY(4, "Spotify", "spotify"),
    YANDEX_DISK(5, "YandexDisk", "yandex-disk"),
    BOX(6, "Box", "box");

    private Integer code;
    private String desc;
    private String link;

    SourceType(int code, String desc, String link) {
        this.code = code;
        this.desc = desc;
        this.link = link;
    }

    public static SourceType get(Integer code) {

        if (code == null) {
            return null;
        }

        for (SourceType ot : values()) {
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

    public String getLink() {
        return link;
    }
}
