package com.glodon.hackserver.bean.zl;

public class Token {
    private String jwtToken;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public String toString() {
        return "Token{" +
                "jwtToken='" + jwtToken + '\'' +
                '}';
    }
}
