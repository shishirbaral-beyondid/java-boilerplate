package com.okta.beyondid.application.controller;

import com.okta.beyondid.application.model.dto.ApiResponseDto;
import com.okta.beyondid.application.model.dto.GroupRequestDto;
import com.okta.beyondid.application.model.dto.UserAddToGroupRequestDto;
import com.okta.beyondid.application.service.OktaGroupService;
import com.okta.sdk.resource.ResourceException;
import com.okta.sdk.resource.group.Group;
import com.okta.sdk.resource.group.GroupList;
import com.okta.sdk.resource.user.UserList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${spring.data.rest.base-path}/groups")
public class GroupController {
    Logger logger = LoggerFactory.getLogger(GroupController.class);
    @Autowired
    OktaGroupService groupService;

    /**
     *
     * @return
     */
    @GetMapping("")
    public ResponseEntity<?> getAllGroups() {
        try {
            GroupList groups = groupService.getAllGroup();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<GroupList>(HttpStatus.OK.value(), groups, "Gell All Groups"));

        } catch (Exception e) {
            logger.error("Error occur fetching grous from okta due to {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));


        }
    }
    @PostMapping("")
    public ResponseEntity<?> addGroup(@Valid @RequestBody GroupRequestDto group, BindingResult result) {
            try {

                if(result.hasErrors()){
                    logger.error("Error occur while adding group due to {{}}",result.getAllErrors());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), null, result.getAllErrors().toString()));

                }

                Group newGroup = groupService.addGroup(group);

                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto<Group>(HttpStatus.CREATED.value(), newGroup, "Group added successfully"));

            } catch (Exception e) {
                logger.error("Error occur adding group okta due to {}", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));


            }
        }
    @PutMapping("")
    public ResponseEntity<?> updateGroup(@Valid @RequestBody GroupRequestDto group, BindingResult result) {
        try {

            if(result.hasErrors()){
                logger.error("Error occur while updating group due to {{}}",result.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), null, result.getAllErrors().toString()));

            }

            Group newGroup = groupService.updateGroup(group);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<Group>(HttpStatus.OK.value(), newGroup, "Group added successfully"));

        } catch (Exception e) {
            logger.error("Error occur updating group okta due to {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));


        }
    }

    @GetMapping("/searchByName/{groupName}")
    public ResponseEntity<?> searchGroupByName(@PathVariable("groupName") String groupName) {
        try {
            GroupList groups = groupService.searchGroupByName(groupName);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<GroupList>(HttpStatus.OK.value(), groups, "Search group by name =" +groupName));


        } catch(ResourceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(),null,e.getMessage()));

        }catch (Exception e) {
            logger.error("Error occur searching  group okta due to {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));


        }
    }
    @GetMapping("{groupId}")
    public ResponseEntity<?> getGroupById(@PathVariable("groupId") String groupId) {
        try {
            Group group = groupService.getById(groupId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<Group>(HttpStatus.OK.value(), group, "Gell group by id =" +groupId));


        } catch(ResourceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDto(HttpStatus.NOT_FOUND.value(),null,e.getMessage()));

        }catch (Exception e) {
            logger.error("Error occur adding group okta due to {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));


        }
    }
    @GetMapping("{groupId}/users")
    public ResponseEntity<?> getAllUsersToGroupById(@PathVariable("groupId") String groupId) {
        try {
            UserList users = groupService.getAllUserToGroup(groupId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<UserList>(HttpStatus.OK.value(), users, "Get all user to  group by id =" +groupId));


        } catch(ResourceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(),null,e.getMessage()));

        }catch (Exception e) {
            logger.error("Error occur fetching all users to group on okta due to {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));


        }
    }
    @PostMapping("{groupId}/users")
    public ResponseEntity<?> addUserToGroup(@PathVariable("groupId") String groupId, @RequestBody List<UserAddToGroupRequestDto> users) {
        try {
             groupService.addUsersToGroup(groupId,users);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<UserList>(HttpStatus.OK.value(), null, "Get all user to  group by id =" +groupId));


        } catch(ResourceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(),null,e.getMessage()));

        }catch (Exception e) {
            logger.error("Error occur fetching all users to group on okta due to {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));


        }
    }
    @DeleteMapping("{groupId}")

    public ResponseEntity<?> deleteGroupById(@PathVariable("groupId")String groupId){
        try{
            logger.info("Deleting group by id ",groupId);
            Group group =   groupService.deleteById(groupId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<Group>(HttpStatus.OK.value(), group, "Successfully deleted group by id =" +groupId));


        }catch (Exception e) {
            logger.error("Error occur while deleting group okta due to {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));


        }
    }


    @DeleteMapping("{groupId}/users/{userId}")
    public ResponseEntity<?> removeUserFromGroup(@PathVariable("groupId")String groupId,@PathVariable("userId") String userId){
        try{
            logger.info("Deleting user from group by id ",groupId);
             groupService.deleteUserFromGroupById(groupId,userId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<Group>(HttpStatus.OK.value(), null, "Successfully deleted group by id =" +groupId));


        }catch (Exception e) {
            logger.error("Error occur while deleting user from group okta due to {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));


        }
    }

    @GetMapping("{groupId}/applications")
    public ResponseEntity<?> getAllApplicationsToGroupById(@PathVariable("groupId") String groupId) {
        try {
            groupService.getAllApplicationsToGroup(groupId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(HttpStatus.OK.value(), null, "Get all user to  group by id =" +groupId));


        } catch(ResourceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(),null,e.getMessage()));

        }catch (Exception e) {
            logger.error("Error occur fetching all users to group on okta due to {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));


        }
    }


}
