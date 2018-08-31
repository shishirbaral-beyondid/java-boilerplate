package com.okta.beyondid.application.service;

import com.okta.sdk.resource.application.Application;
import com.okta.sdk.resource.application.ApplicationList;

public interface ApplicationService {

    public ApplicationList getAllApplications();
    public Application getApplicaitonById(String applicaitonId);
}
