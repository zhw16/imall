package com.zhang.imall.model.pojo;

public class TableName {
    private Integer id;

    private String username;

    private String username1;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1 == null ? null : username1.trim();
    }
}