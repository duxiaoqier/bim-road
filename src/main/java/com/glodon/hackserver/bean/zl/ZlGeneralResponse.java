package com.glodon.hackserver.bean.zl;

public class ZlGeneralResponse<T> {
    private String returnCode;
    private T result;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ZlGeneralResponse{" +
                "returnCode='" + returnCode + '\'' +
                ", result=" + result +
                '}';
    }
}