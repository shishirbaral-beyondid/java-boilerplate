package com.okta.beyondid.application.service.impl;


import com.okta.beyondid.application.config.OktaClientBuilder;
import com.okta.beyondid.application.constant.OktaUserProfileAttributeConstant;
import com.okta.beyondid.application.controller.UserController;
import com.okta.beyondid.application.model.dto.GroupRequestDto;
import com.okta.beyondid.application.model.dto.UserChangePasswordRequest;
import com.okta.beyondid.application.model.dto.UserRequestDto;
import com.okta.beyondid.application.service.OktaUserManagementService;
import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.application.ApplicationList;
import com.okta.sdk.resource.group.Group;
import com.okta.sdk.resource.user.*;
import com.okta.spring.config.OktaClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.awt.image.ImageWatched;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class OktaUserManagementServiceImpl implements OktaUserManagementService {
    Logger logger = LoggerFactory.getLogger(OktaUserManagementServiceImpl.class);


        @Autowired
        OktaClientBuilder oktaClientBuilder;

    @Override
    public UserList getAllUsers() {
        logger.info("Fetching user list  from kta api ");

        Client client = oktaClientBuilder.getOktaClient();

       return client.listUsers();
    }

    @Override
    public User addUser(UserRequestDto user) {
        List<String> integerArray = new ArrayList<String>();
        integerArray.add("1");
        Map<String,Object> userProfileMap = new HashMap<String,Object>();
        userProfileMap.put("firstName",user.getFirstName());
        userProfileMap.put("lastName",user.getLastName());
        userProfileMap.put("email",user.getEmail());
        userProfileMap.put("login",user.getLogin());

        userProfileMap.put("IntergerArray",integerArray);






        Client client = oktaClientBuilder.getOktaClient();

        System.out.println(client.toString());
        User userResp = UserBuilder.instance()

              /*  .setFirstName("Dinesh")
                .setLastName("Test")

                .setEmail("dineshbade1992+1@gmail.com")
                .setLogin("dineshbade1992+1@gmail.com")*/
                .setProfileProperties(userProfileMap)
                .buildAndCreate(client);



        return userResp;
    }

    @Override
    public User getUserById(String userId) {

        return oktaClientBuilder.getOktaClient().getUser(userId);

    }

    @Override
    public User deleteUserById(String userId) {
        User user  = oktaClientBuilder.getOktaClient().getUser(userId);
        if(user!=null){
            user.delete();
        }
        return user;

    }

    @Override
    public User suspendUserById(String userId) {
        return null;
    }

    @Override
    public AppLinkList getAllApplicationByUserId(String userId) {

     AppLinkList applications = oktaClientBuilder.getOktaClient().getUser(userId).listAppLinks(true);

     return applications;

    }

    @Override
    public Boolean checkUserExists(String userName) {
        return oktaClientBuilder.getOktaClient().listUsers(userName,null,null,null,null).stream().count()>=1;
    }

    @Override
    public UserCredentials changePasswordWithUserId(String userId, UserChangePasswordRequest passwordRequest) {
            User user = getUserById(userId);
              return  user.changePassword(oktaClientBuilder.getOktaClient().instantiate(ChangePasswordRequest.class)
                      .setOldPassword(oktaClientBuilder.getOktaClient().instantiate(PasswordCredential.class).setValue(passwordRequest.getOldPassword().toCharArray()))
                       .setNewPassword(oktaClientBuilder.getOktaClient().instantiate(PasswordCredential.class).setValue(passwordRequest.getNewPassword().toCharArray()))
              );

    }

    @Override
    public void addUserToGroup(List<GroupRequestDto> groups, String userId) {
        try{
            User user = getUserById(userId);
            groups.stream().forEach(group->{
                user.addToGroup(group.getId());
            });

        }catch(Exception e){
            System.out.println(e);
        }


    }

    @Override
    public void removeGroupFromUser(String userId, String groupId) {

        oktaClientBuilder.getOktaClient().getGroup(groupId).removeUser(userId);

    }

    @Override
    public UserList searchUserByEmail(String email) {
        return oktaClientBuilder.getOktaClient().listUsers(email,null,null,null,null);
    }

    @Override
    public User updateUser( LinkedHashMap<String,Object> userUpdateRequest,String userId) {
        User user = getUserById(userId);
        user.getProfile().setFirstName(String.valueOf(userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_FIRST_NAME)));
        user.getProfile().setMobilePhone(String.valueOf(userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_MOBILE_PHONE)));
        user.getProfile().setLastName(String.valueOf(userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_LAST_NAME)));
        user.getProfile().setEmail(String.valueOf(userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_PRIMARY_EMAIL)));

        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_MIDDLE_NAME,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_MIDDLE_NAME.equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_MIDDLE_NAME)));


        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_HONORIFICPREFIX,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_HONORIFICPREFIX.equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_HONORIFICPREFIX)));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_HONORIFICSUFFIX,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_HONORIFICPREFIX).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_HONORIFICPREFIX));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_DISPLAY_NAME,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_DISPLAY_NAME).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_DISPLAY_NAME));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_DEPARTMENT,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_DEPARTMENT).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_DEPARTMENT));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_DIVISION,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_DIVISION).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_DIVISION));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_TITLE,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_TITLE).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_TITLE));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_TIME_ZONE,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_TIME_ZONE).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_TIME_ZONE));

        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_CITY,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_CITY).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_CITY));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_STREETADDRESS,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_STREETADDRESS).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_STREETADDRESS));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_POSTAL_ADDRESS,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_POSTAL_ADDRESS).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_POSTAL_ADDRESS));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_ZIPCODE,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_ZIPCODE).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_ZIPCODE));

        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_STATE,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_STATE).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_STATE));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_COUNTRY_CODE,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_COUNTRY_CODE).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_COUNTRY_CODE));

        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_MANAGER_ID,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_MANAGER_ID).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_MANAGER_ID));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_MANAGER,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_MANAGER).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_MANAGER));

        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_PREFERRED_LANGUAGE,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_PREFERRED_LANGUAGE).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_PREFERRED_LANGUAGE));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_LOCALE,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_LOCALE).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_LOCALE));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_USER_TYPE,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_USER_TYPE).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_USER_TYPE));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_ORGANIZATION,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_ORGANIZATION).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_ORGANIZATION));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_PROFILEURL,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_PROFILEURL).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_PROFILEURL));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_NICKNAME,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_NICKNAME).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_NICKNAME));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_PRIMARY_PHONE,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_PRIMARY_PHONE).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_PRIMARY_PHONE));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_EMPLOYEE_NUMBER,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_EMPLOYEE_NUMBER).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_EMPLOYEE_NUMBER));
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_COST_CENTER,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_COST_CENTER).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_COST_CENTER));


        System.out.println(userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_INTEGER_ARRAY));
        String intArrString =String.valueOf( userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_INTEGER_ARRAY));

        List<String> intArr = new ArrayList<String>(Arrays.asList(intArrString.split(",")));

        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_INTEGER_ARRAY,intArr);
/*
        user.getProfile().put(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_SAMPLE_BOOL,userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_SAMPLE_BOOL).equals("")?null:userUpdateRequest.get(OktaUserProfileAttributeConstant.OKTA_USER_PROFILE_ATTRIBUTE_SAMPLE_BOOL));
*/

        user.update();

        /*for (String key: userUpdateRequest.keySet()) {
            System.out.println(userUpdateRequest.get(key));
            System.out.println(key);

        }*/

        return user;
    }

}
