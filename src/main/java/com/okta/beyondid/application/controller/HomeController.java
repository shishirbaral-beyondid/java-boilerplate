package com.okta.beyondid.application.controller;

import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.user.UserList;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/users")
    public UserList getUsers (){
        Client client = Clients.builder()
                .setOrgUrl("https://domain name.com")
                .setClientCredentials(new TokenClientCredentials("apiToken"))
                .build();
        UserList users = client.listUsers();
        return users;
    }
}
