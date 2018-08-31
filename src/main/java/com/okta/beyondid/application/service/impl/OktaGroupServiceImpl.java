package com.okta.beyondid.application.service.impl;

import com.okta.beyondid.application.config.OktaClientBuilder;
import com.okta.beyondid.application.model.dto.GroupRequestDto;
import com.okta.beyondid.application.model.dto.UserAddToGroupRequestDto;
import com.okta.beyondid.application.service.OktaGroupService;
import com.okta.sdk.client.Client;
import com.okta.sdk.resource.group.Group;
import com.okta.sdk.resource.group.GroupBuilder;
import com.okta.sdk.resource.group.GroupList;
import com.okta.sdk.resource.user.AppLinkList;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OktaGroupServiceImpl implements OktaGroupService {
    @Autowired
    OktaClientBuilder oktaClientBuilder;

    @Override
    public Group addGroup(GroupRequestDto group) {
       return GroupBuilder.instance().setName(group.getName()).setDescription(group.getDescription())
            .buildAndCreate(oktaClientBuilder.getOktaClient());

    }

    @Override
    public GroupList getAllGroup() {
        return oktaClientBuilder.getOktaClient().listGroups();
    }

    @Override
    public Group getById(String groupId) {
        return oktaClientBuilder.getOktaClient().getGroup(groupId);
    }

    @Override
    public Group deleteById(String groupId) {

        Group group = oktaClientBuilder.getOktaClient().getGroup(groupId);
        if(group!=null){
            group.delete();
        }
        return group;
    }

    @Override
    public GroupList searchGroupByName(String groupName) {
        return oktaClientBuilder.getOktaClient().listGroups(groupName,null,null);
    }

    @Override
    public Group updateGroup(GroupRequestDto group) {
        Group  newGroup = oktaClientBuilder.getOktaClient().getGroup(group.getId());
            newGroup.getProfile().setDescription(group.getDescription()).setName(group.getName());
            newGroup.update();

            return newGroup;
    }

    @Override
    public UserList getAllUserToGroup(String groupId) {
        return oktaClientBuilder.getOktaClient().getGroup(groupId).listUsers();
    }

    @Override
    public void addUsersToGroup(String groupId, List<UserAddToGroupRequestDto> users) {
        Client client = oktaClientBuilder.getOktaClient();
        users.forEach(user->{
                    client.getUser(user.getId()).addToGroup(groupId);
        });
    }

    @Override
    public void deleteUserFromGroupById(String groupId, String userId) {
         oktaClientBuilder.getOktaClient().getGroup(groupId).removeUser(userId);
    }

    @Override
    public AppLinkList getAllApplicationsToGroup(String groupId) {
        return null;
    }
}
