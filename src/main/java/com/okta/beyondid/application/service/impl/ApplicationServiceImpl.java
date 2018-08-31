package com.okta.beyondid.application.service.impl;

import com.okta.beyondid.application.config.OktaClientBuilder;
import com.okta.beyondid.application.service.ApplicationService;
import com.okta.sdk.resource.application.Application;
import com.okta.sdk.resource.application.ApplicationList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ApplicationServiceImpl  implements ApplicationService{

    @Autowired
    OktaClientBuilder oktaClientBuilder;
    @Override
    public ApplicationList getAllApplications() {
        return oktaClientBuilder.getOktaClient().listApplications();
    }

    @Override
    public Application getApplicaitonById(String applicaitonId) {
        return null;
    }
}
