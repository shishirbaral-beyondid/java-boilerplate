package com.okta.beyondid.application.model.dto;

import javax.validation.constraints.NotEmpty;

public class UserChangePasswordRequest {

    @NotEmpty
    private String oldPassword;
    @NotEmpty
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
