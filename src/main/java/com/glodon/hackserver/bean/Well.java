package com.glodon.hackserver.bean;

public class Well {
    private String id;
    private Long objectId;
    private Double rhumid;
    private Double temp;
    private Integer status;
    private Position position;

    public Well() {
    }

    public Well(String id, Long objectId, Double rhumid, Double temp, Integer status, Position position) {
        this.id = id;
        this.objectId = objectId;
        this.rhumid = rhumid;
        this.temp = temp;
        this.status = status;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Double getRhumid() {
        return rhumid;
    }

    public void setRhumid(Double rhumid) {
        this.rhumid = rhumid;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
