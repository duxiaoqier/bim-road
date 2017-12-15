package com.glodon.hackserver.bean;

import java.util.Date;

public class Problem {
    private Long id;
    private String name;
    private String content;
    private String imageUrl;
    private Boolean solved;
    private Reporter reporter;
    private Position position;
    private Gps gps;
    private Date reportTime;

    public Problem() {
    }

    public Problem(Long id, String name, String content, String imageUrl, Boolean solved, Reporter reporter, Position position, Gps gps, Date reportTime) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.imageUrl = imageUrl;
        this.solved =solved;
        this.reporter = reporter;
        this.position = position;
        this.gps = gps;
        this.reportTime = reportTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getSolved() {
        return solved;
    }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }

    public Reporter getReporter() {
        return reporter;
    }

    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Gps getGps() {
        return gps;
    }

    public void setGps(Gps gps) {
        this.gps = gps;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }
}
