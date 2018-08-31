package com.okta.beyondid.application.controller;


import com.okta.beyondid.application.model.dto.ApiResponseDto;
import com.okta.beyondid.application.model.dto.UserRequestDto;
import com.okta.beyondid.application.service.OktaUserManagementService;
import com.okta.beyondid.application.service.impl.OktaUserManagementServiceImpl;
import com.okta.sdk.resource.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${spring.data.rest.base-path}/")
@CrossOrigin(origins = "*")

public class RegistrationController {
    Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    OktaUserManagementService oktaUserManagementService;
    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDto userRequest){
        try{
            if(oktaUserManagementService.checkUserExists(userRequest.getLogin())){

                logger.info("User already exists with username = " , userRequest.getLogin());
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(HttpStatus.CONFLICT.value(),userRequest,"User already exists with username = "+ userRequest.getLogin()));

            };
            User user = oktaUserManagementService.addUser(userRequest);
/*
            logger.info("User created successfully with resp data {{}}",objectMapper.writeValueAsString(user) );
*/
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto(HttpStatus.CREATED.value(),userRequest,"Registration Successful"));

        }catch (Exception e){
            logger.error("Error occur while registering user to okta due to {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }





    }
}
