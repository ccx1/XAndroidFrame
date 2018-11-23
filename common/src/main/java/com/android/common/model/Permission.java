package com.android.common.model;

/**
 * @author ccx
 * @date 2018/11/16
 */
public class Permission {

    private String permission;
    private boolean granted;

    public Permission(String permission, boolean granted) {
        this.permission = permission;
        this.granted = granted;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }
}
