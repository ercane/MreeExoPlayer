package mree.cloud.music.player.common.model;

import mree.cloud.music.player.common.MarkedInfo;
import mree.cloud.music.player.common.ref.ItemType;
import mree.cloud.music.player.common.ref.SourceType;

/**
 * Created by mree on 20.04.2017.
 */

public class FolderInfo extends MarkedInfo {
    private String name;
    private String accountId;
    private SourceType accountType;
    private String parentId;
    private String path;
    private boolean offlineState;
    private ItemType itemType;

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public boolean isOfflineState() {
        return offlineState;
    }

    public void setOfflineState(boolean offlineState) {
        this.offlineState = offlineState;
    }

    public SourceType getAccountType() {
        return accountType;
    }

    public void setAccountType(SourceType accountType) {
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            FolderInfo other = (FolderInfo) obj;
            if ((id != null && id.equals(other.id)) &&
                    (path != null && path.equals(other.path)) &&
                    (parentId != null && parentId.equals(other.parentId)) &&
                    (accountId != null && accountId.equals(other.accountId))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return super.equals(obj);
        }
    }
}
