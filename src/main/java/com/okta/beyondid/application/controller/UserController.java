package com.okta.beyondid.application.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okta.beyondid.application.model.dto.*;
import com.okta.beyondid.application.service.OktaUserManagementService;
import com.okta.beyondid.application.util.OktaErrorCode;
import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.error.ErrorCause;
import com.okta.sdk.resource.ResourceException;
import com.okta.sdk.resource.application.ApplicationList;
import com.okta.sdk.resource.group.GroupList;
import com.okta.sdk.resource.user.*;
import com.okta.spring.config.OktaClientProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.okta.sdk.resource.application.ApplicationSignOnMode;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;

@RestController()
/*
@CrossOrigin(origins = "http://localhost:8080",methods = {RequestMethod.POST,RequestMethod.GET,RequestMethod.DELETE,RequestMethod.PUT})
*/

@Api(value = "userOperation" , description = "All operation for user in okta api")
@RequestMapping("${spring.data.rest.base-path}/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private OktaClientProperties oktaClientProperties;

    @Autowired
    private OktaUserManagementService oktaUserManagementService;

    ObjectMapper om = new ObjectMapper();

    @ApiOperation(value = "Get all users from okta", response = ApiResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    }
    )
    @GetMapping("")
     public ResponseEntity<?> getUsers (){
        try {

            UserList users = oktaUserManagementService.getAllUsers();


            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(HttpStatus.OK.value(),users,"Get All users list"));


        }catch (Exception e){
            logger.error("Error occur while fetching user list due to {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,"INTERNAL SERVER ERROR"));

        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable ("userId")String userId){
        try{

            System.out.println(userId);
            User user = oktaUserManagementService.getUserById(userId);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(HttpStatus.OK.value(),user,"Get All users list"));

        }catch (ResourceException e){
            logger.error("Error occur while fetching user by id {}",e);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDto(HttpStatus.NOT_FOUND.value(),null,e.getMessage()));

           /* throw new ResourceException(e);*/

        }catch(Exception e){
            logger.error("Error occur while adding user to okta due to {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }
    }

    @GetMapping("/{userId}/schema")
    public ResponseEntity<?> getUserSchemaById(@PathVariable ("userId")String userId,Principal principal){
        try{
            System.out.println(principal.getName());
            User user = oktaUserManagementService.getUserById(userId);
            //method to get custom property of user
            
            Map<String ,String> schemaMap = new HashMap<String ,String>();
            for(String key: user.getProfile().keySet()){
                schemaMap.put(key,user.getProfile().get(key)!=null?user.getProfile().get(key).toString():null);
                System.out.println(user.getProfile().get(key)!=null?user.getProfile().get(key).toString():null );
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(HttpStatus.OK.value(),schemaMap,"Get user schema by user Id"));

        }catch (ResourceException e){
            logger.error("Error occur while fetching user schema by id {}",e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDto(HttpStatus.NOT_FOUND.value(),null,e.getMessage()));

        }catch(Exception e){
            logger.error("Error occur while fetching user  schema from okta due to {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }
    }


    @PostMapping("")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDto userRequest, BindingResult result){
        try{
            ObjectMapper objectMapper = new ObjectMapper();


                if(result.hasErrors()){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(result.getAllErrors());
                }
                User user = oktaUserManagementService.addUser(userRequest);
                logger.info("User created successfully with resp data {{}}",objectMapper.writeValueAsString(user) );
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto(HttpStatus.CREATED.value(),userRequest,"User created Successfully"));

        }catch (Exception e){
            logger.error("Error occur while adding user to okta due to {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }


    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody Object userUpdateRequest,@PathVariable("userId") String userId){
        try{
            logger.info("user update dto {{}}" , om.writeValueAsString(userUpdateRequest));

           User user =     oktaUserManagementService.updateUser(( LinkedHashMap<String,Object>)userUpdateRequest,userId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(HttpStatus.OK.value(),user,"User updated Successfully"));

        }catch(ResourceException ex){
            System.out.println(ex.getCauses());
            for (ErrorCause cause: ex.getCauses()) {
                System.out.println(cause.getSummary());

            }
            logger.error("Error occur while updating user to okta due to {}",ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(),null,ex.getMessage()));

        }
        catch (Exception e){
            logger.error("Error occur while updating user to okta due to {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable("userId") String userId){
        try{
            logger.info("Delete user with id = ", userId);
            User user =    oktaUserManagementService.deleteUserById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(HttpStatus.OK.value(),user,"User deleted successfully with id="+userId));

        }catch (ResourceException e){
            logger.error("Error occur while fetching user by id {}",e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDto(HttpStatus.NOT_FOUND.value(),null,e.getMessage()));

        }catch(Exception e){
            logger.error("Error occur while adding user to okta due to {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }

    }

    @GetMapping("/{userId}/suspend")
    public ResponseEntity<?> suspendUserById(@PathVariable ("userId")String userId){
        try{

            System.out.println(userId);
            User user = oktaUserManagementService.getUserById(userId);
            if(user!=null){
                user.suspend();

            }
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(HttpStatus.OK.value(),user,"Suspend user with id = "+userId));

        }catch (ResourceException e){
            logger.error("Error occur while fetching user by id {}",e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDto(HttpStatus.NOT_FOUND.value(),null,e.getMessage()));

        }catch(Exception e){
            logger.error("Error occur while adding user to okta due to {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }
    }
    @GetMapping("/{userId}/deactivate")
    public ResponseEntity<?> deactivateUserById(@PathVariable ("userId")String userId){
        try{

            System.out.println(userId);
            User user = oktaUserManagementService.getUserById(userId);
            if(user!=null){
                user.deactivate();

            }
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(HttpStatus.OK.value(),user,"Deactive user with id = "+userId));

        }catch (ResourceException e){
            logger.error("Error occur while fetching user by id {}",e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDto(HttpStatus.NOT_FOUND.value(),null,e.getMessage()));

        }catch(Exception e){
            logger.error("Error occur while adding user to okta due to {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }
    }
    @GetMapping("/{userId}/groups")
    public ResponseEntity<?> getAllGroupsForUser(@PathVariable ("userId")String userId){
        try{

            System.out.println(userId);
            User user = oktaUserManagementService.getUserById(userId);
                GroupList groups =null;
            if(user!=null){
                  groups =user.listGroups();
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(HttpStatus.OK.value(),groups,"Get All Group for  user with id = "+userId));

        }catch (ResourceException e){
            logger.error("Error occur while fetching groups for user  by id {}",e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDto(HttpStatus.NOT_FOUND.value(),null,e.getMessage()));

        }catch(Exception e){
            logger.error("Error occur while fetching groups for user  due to{}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }
    }
    @GetMapping("/{userId}/applications")
    public ResponseEntity<?> getAllApplicaitonsForUser(@PathVariable ("userId")String userId){
        try{
                logger.info("Fetching all application assigned to user with id = {{}}",userId);
            System.out.println(userId);
            AppLinkList applicationList = oktaUserManagementService.getAllApplicationByUserId(userId);

            ObjectMapper ob = new ObjectMapper();

            System.out.println("applicationlist :::::::::::::" +  ob.writeValueAsString(applicationList));

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<AppLinkList>(HttpStatus.OK.value(),applicationList,"Get All Applications for  user with id = "+userId));

        }catch(Exception e){
            logger.error("Error occur while fetching Applications for user  due to{}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }
    }


    @PostMapping("/{userId}/changePassword")
    public ResponseEntity<?> changePassword(@PathVariable ("userId")String userId, @RequestBody UserChangePasswordRequest changePasswordRequest){
        try{
            logger.info("change password for user with id  = ", userId);

            UserCredentials userCredential =    oktaUserManagementService.changePasswordWithUserId(userId,changePasswordRequest);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(HttpStatus.OK.value(),null,"User password changed successfully with id="+userId));

        }catch (ResourceException e){
            logger.error("Error occur while changing  user  password by id {}",e);
            StringBuilder sb = new StringBuilder();
            for (ErrorCause cause: e.getCauses()) {
                sb.append(cause.getSummary());
                sb.append('\n');
                System.out.println(cause.getSummary());

            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(),null,sb.toString()));



        }catch(Exception e){
            logger.error("Error occur while while changing  user  password to okta due to {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }
    }


    @PostMapping("/{userId}/groups")
    public ResponseEntity<?> addGroupsToUser(@PathVariable("userId") String userId, @RequestBody List<GroupRequestDto> groups){
        try{
                oktaUserManagementService.addUserToGroup(groups,userId);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(HttpStatus.OK.value(),null,"User added to groups successfully"));

        }catch(Exception e){
            logger.error("Error occur while adding user to groups to okta due to {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }
    }

    @DeleteMapping("/{userId}/groups/{groupId}")
    public ResponseEntity<?> removeGroupFromUserById(@PathVariable ("userId") String userId,@PathVariable("groupId") String groupId){

        try{
            oktaUserManagementService.removeGroupFromUser(userId,groupId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(HttpStatus.OK.value(),null,"Successfully remove group from user"));

        }catch(Exception e){
            logger.error("Error occur while removing group from user due to {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }
    }


    @GetMapping("/searchByEmail/{email}")
    public ResponseEntity<?>  searchUserByEmail(@PathVariable("email") String email){
        try{
                    UserList users = oktaUserManagementService.searchUserByEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(HttpStatus.OK.value(),users,"Get all users by email"));

        }catch(Exception e){
            logger.error("Error occur while searching user by email  due to {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }
    }
}
