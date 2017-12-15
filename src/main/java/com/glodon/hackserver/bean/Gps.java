package com.glodon.hackserver.bean;

public class Gps {
    private Double jd;
    private Double wd;

    public Gps() {
    }

    public Gps(Double jd, Double wd) {
        this.jd = jd;
        this.wd = wd;
    }

    public Double getJd() {
        return jd;
    }

    public void setJd(Double jd) {
        this.jd = jd;
    }

    public Double getWd() {
        return wd;
    }

    public void setWd(Double wd) {
        this.wd = wd;
    }
}
