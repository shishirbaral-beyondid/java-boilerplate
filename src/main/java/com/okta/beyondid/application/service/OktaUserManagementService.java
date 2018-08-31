package com.okta.beyondid.application.service;

import com.okta.beyondid.application.model.dto.GroupRequestDto;
import com.okta.beyondid.application.model.dto.UserChangePasswordRequest;
import com.okta.beyondid.application.model.dto.UserRequestDto;
import com.okta.sdk.resource.application.ApplicationList;
import com.okta.sdk.resource.user.*;

import java.util.LinkedHashMap;
import java.util.List;

public interface OktaUserManagementService {

    public UserList getAllUsers();
    public User getUserById(String userId);
    public User addUser(UserRequestDto userRequestDto);
    public User deleteUserById(String userId);
    public User suspendUserById(String userId);
    public AppLinkList getAllApplicationByUserId(String userId);
    public Boolean checkUserExists(String userName );
    public UserCredentials changePasswordWithUserId(String userId, UserChangePasswordRequest passwordRequest);

    void addUserToGroup(List<GroupRequestDto> groups, String userId);

    void removeGroupFromUser(String userId,String groupId);

    public UserList searchUserByEmail(String email);

    public User updateUser(LinkedHashMap<String,Object> userUpdateRequest,String userId);
}
