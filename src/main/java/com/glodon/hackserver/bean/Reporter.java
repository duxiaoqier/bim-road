package com.glodon.hackserver.bean;

public class Reporter {
    private String name;
    private Long phoneNum;
    private String role;

    public Reporter() {
    }

    public Reporter(String name, Long phoneNum, String role) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(Long phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
