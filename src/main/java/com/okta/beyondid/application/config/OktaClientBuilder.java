package com.okta.beyondid.application.config;

import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.spring.config.OktaClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)

public class OktaClientBuilder {
    @Autowired
    OktaClientProperties oktaClientProperties;
    public Client getOktaClient(){
       return Clients.builder()
                .setOrgUrl(oktaClientProperties.getOrgUrl())
                .setClientCredentials(new TokenClientCredentials(oktaClientProperties.getToken()))
                .build();
    }
}
