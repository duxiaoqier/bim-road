package com.glodon.hackserver.bean.zl;

import java.util.Date;

public class ShadowsAttribute {
    private String code;
    private Double value;
    private Date timestamp;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ShadowsAttribute{" +
                "code='" + code + '\'' +
                ", value=" + value +
                ", timestamp=" + timestamp +
                '}';
    }
}
