package com.okta.beyondid.application.controller;


import com.okta.beyondid.application.config.OktaClientBuilder;
import com.okta.beyondid.application.model.dto.ApiResponseDto;
import com.okta.beyondid.application.model.dto.UserCountDto;
import com.okta.sdk.resource.group.GroupList;
import com.okta.sdk.resource.user.UserList;
import com.okta.sdk.resource.user.UserStatus;
import com.okta.spring.config.OktaClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("${spring.data.rest.base-path}/dashboard/")
public class DashboardController {
    @Autowired
    OktaClientBuilder oktaClientBuilder;
    @GetMapping("userStatusCount")
  public ResponseEntity<?> getAllUserCountByStatus() {
          try{

               AtomicInteger activeUserCount = new AtomicInteger(0);
              AtomicInteger provisionedUserCount = new AtomicInteger(0);
              AtomicInteger totalUser = new AtomicInteger(0);

              AtomicInteger stageUserCount = new AtomicInteger(0);
              AtomicInteger suspendedUserCount = new AtomicInteger(0);
              AtomicInteger passwordExperiedUserCount = new AtomicInteger(0);

             oktaClientBuilder.getOktaClient().listUsers().stream()
                      .forEach(user -> {
                                totalUser.getAndIncrement();
                            if(user.getStatus().equals(UserStatus.ACTIVE))
                                activeUserCount.getAndIncrement();
                             else if(user.getStatus().equals(UserStatus.PROVISIONED))
                                provisionedUserCount.getAndIncrement();
                             else  if(user.getStatus().equals(UserStatus.STAGED))
                                 stageUserCount.getAndIncrement();
                             else if(user.getStatus().equals(UserStatus.SUSPENDED))
                                 suspendedUserCount.getAndIncrement();
                             else if(user.getStatus().equals(UserStatus.PASSWORD_EXPIRED))
                                 passwordExperiedUserCount.getAndIncrement();
                      });
              UserCountDto userCountDto = new UserCountDto(Long.valueOf(activeUserCount.get()),Long.valueOf(provisionedUserCount.get()),Long.valueOf(stageUserCount.get()),Long.valueOf(suspendedUserCount.get()),Long.valueOf(passwordExperiedUserCount.get()));

              /*long activeUserList = oktaClientBuilder.getOktaClient().listUsers(null, "status eq \"ACTIVE\"", null, null, null).stream().count();
              long provisionedUserList = oktaClientBuilder.getOktaClient().listUsers(null, "status eq \"PROVISIONED\"", null, null, null).stream().count();
              long stagedUserList = oktaClientBuilder.getOktaClient().listUsers(null, "status eq \"STAGED\"", null, null, null).stream().count();
              long suspenedUserList = oktaClientBuilder.getOktaClient().listUsers(null, "status eq \"SUSPENDED\"", null, null, null).stream().count();
              long passwordExperiedUserList = oktaClientBuilder.getOktaClient().listUsers(null, "status eq \"PASSWORD_EXPIRED\"", null, null, null).stream().count();

            */  return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(HttpStatus.OK.value(), userCountDto, "Get User Count"));
            }catch (Exception e){
                return null;
            }
    }

}
