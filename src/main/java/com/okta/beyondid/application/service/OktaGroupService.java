package com.okta.beyondid.application.service;

import com.okta.beyondid.application.model.dto.GroupRequestDto;
import com.okta.beyondid.application.model.dto.UserAddToGroupRequestDto;
import com.okta.sdk.resource.group.Group;
import com.okta.sdk.resource.group.GroupList;
import com.okta.sdk.resource.user.AppLinkList;
import com.okta.sdk.resource.user.UserList;

import javax.validation.Valid;
import java.util.List;

public interface OktaGroupService
{

    public Group addGroup(GroupRequestDto group);
    public GroupList getAllGroup();
    public Group getById(String groutId);
    public Group deleteById(String groupId);
    public GroupList searchGroupByName(String groupName);
    public UserList getAllUserToGroup(String groupId);
    Group updateGroup( GroupRequestDto group);

    void addUsersToGroup(String groupId, List<UserAddToGroupRequestDto> users);

    void deleteUserFromGroupById(String groupId, String userId);

    AppLinkList getAllApplicationsToGroup(String groupId);
}
