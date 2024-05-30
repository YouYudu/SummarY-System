package com.summarization.dto;

import lombok.Data;

import java.time.LocalDateTime;

public class ActivationDTO {
    private String activationCode;

    private LocalDateTime activateTime;

    private LocalDateTime deactivateTime;

    private Integer state;


    public ActivationDTO(String activateCode) {
        this.activationCode = activateCode;
    }

    public ActivationDTO() {

    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public LocalDateTime getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(LocalDateTime activateTime) {
        this.activateTime = activateTime;
    }

    public LocalDateTime getDeactivateTime() {
        return deactivateTime;
    }

    public void setDeactivateTime(LocalDateTime deactivateTime) {
        this.deactivateTime = deactivateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
