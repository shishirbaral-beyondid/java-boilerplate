package com.okta.beyondid.application.service;

import com.okta.beyondid.application.model.dto.UserResponseDto;

public interface UserConverterService {

    public UserResponseDto convertOktaUserToApiUser();
}
