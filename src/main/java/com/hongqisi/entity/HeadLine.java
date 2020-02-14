package com.hongqisi.entity;

import java.util.Date;

/**
 * 头条
 */
public class HeadLine {

    private Long lineId;
    private String lineName;
    //链接
    private String lineLink;
    private String lineImg;
    //权重,头条按照优先级的大小去展示
    private Integer priority;
    //0不可以用，1可用
    private Integer enableStatus;
    private Date createTime;
    private Date lastEditTime;

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineLink() {
        return lineLink;
    }

    public void setLineLink(String lineLink) {
        this.lineLink = lineLink;
    }

    public String getLineImg() {
        return lineImg;
    }

    public void setLineImg(String lineImg) {
        this.lineImg = lineImg;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }
}
