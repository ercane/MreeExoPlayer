package mree.cloud.music.player.common.model.yandex;

/**
 * Created by eercan on 20.12.2017.
 */

public class Folder {
    private String created;

    private String name;

    private String path;

    private String custom_properties;

    private String public_key;

    private String type;

    private String public_url;

    private FolderList _embedded;

    private String modified;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCustom_properties() {
        return custom_properties;
    }

    public void setCustom_properties(String custom_properties) {
        this.custom_properties = custom_properties;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPublic_url() {
        return public_url;
    }

    public void setPublic_url(String public_url) {
        this.public_url = public_url;
    }

    public FolderList get_embedded() {
        return _embedded;
    }

    public void set_embedded(FolderList _embedded) {
        this._embedded = _embedded;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
