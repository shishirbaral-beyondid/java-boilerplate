package com.okta.beyondid.application.model.dto;

public class UserCountDto {
    private Long activeUserCount;
    private Long provisionedUserCount;
    private Long stagedUserCount;
    private Long suspendedUserCount;
    private Long passwordExpireUserCount;


    public UserCountDto(Long activeUserCount, Long provisionedUserCount, Long stagedUserCount, Long suspendedUserCount, Long passwordExpireUserCount) {
        this.activeUserCount = activeUserCount;
        this.provisionedUserCount = provisionedUserCount;
        this.stagedUserCount = stagedUserCount;
        this.suspendedUserCount = suspendedUserCount;
        this.passwordExpireUserCount = passwordExpireUserCount;
    }

    public Long getActiveUserCount() {
        return activeUserCount;
    }

    public void setActiveUserCount(Long activeUserCount) {
        this.activeUserCount = activeUserCount;
    }

    public Long getProvisionedUserCount() {
        return provisionedUserCount;
    }

    public void setProvisionedUserCount(Long provisionedUserCount) {
        this.provisionedUserCount = provisionedUserCount;
    }

    public Long getStagedUserCount() {
        return stagedUserCount;
    }

    public void setStagedUserCount(Long stagedUserCount) {
        this.stagedUserCount = stagedUserCount;
    }

    public Long getSuspendedUserCount() {
        return suspendedUserCount;
    }

    public void setSuspendedUserCount(Long suspendedUserCount) {
        this.suspendedUserCount = suspendedUserCount;
    }

    public Long getPasswordExpireUserCount() {
        return passwordExpireUserCount;
    }

    public void setPasswordExpireUserCount(Long passwordExpireUserCount) {
        this.passwordExpireUserCount = passwordExpireUserCount;
    }
}
