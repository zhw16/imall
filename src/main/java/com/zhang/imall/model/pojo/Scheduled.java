package com.zhang.imall.model.pojo;

public class Scheduled {
    private String cronId;

    private String cronName;

    private String cron;

    public String getCronId() {
        return cronId;
    }

    public void setCronId(String cronId) {
        this.cronId = cronId == null ? null : cronId.trim();
    }

    public String getCronName() {
        return cronName;
    }

    public void setCronName(String cronName) {
        this.cronName = cronName == null ? null : cronName.trim();
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron == null ? null : cron.trim();
    }
}