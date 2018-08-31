package com.okta.beyondid.application.controller;

import com.okta.beyondid.application.model.dto.ApiResponseDto;
import com.okta.beyondid.application.model.dto.UserResponseDto;
import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.user.UserList;
import com.okta.sdk.resource.user.UserStatus;
import com.okta.spring.config.OktaClientProperties;
import com.okta.spring.config.OktaOAuth2Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:8080")

public class HomeController {

    @GetMapping("")
    public String index(){
        return "index";
    }

    @GetMapping("/userProfile")
    @PreAuthorize("#oauth2.hasScope('profile')")
    public Map<String, Object> getUserDetails(OAuth2Authentication authentication) {
        return (Map<String, Object>) authentication.getUserAuthentication().getDetails();
    }


}
