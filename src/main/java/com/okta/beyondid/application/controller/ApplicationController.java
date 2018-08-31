package com.okta.beyondid.application.controller;


import com.okta.beyondid.application.model.dto.ApiResponseDto;
import com.okta.beyondid.application.service.ApplicationService;
import com.okta.sdk.resource.ResourceException;
import com.okta.sdk.resource.application.ApplicationList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${spring.data.rest.base-path}/applications")
public class ApplicationController {
    Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    ApplicationService applicationService;
    @GetMapping("")
    public ResponseEntity<?> getAllApplications(){
        try{
            logger
                    .info("Fetch all applications");
            ApplicationList applications = applicationService.getAllApplications();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(HttpStatus.OK.value(),applications,"Get All application list"));


        }catch (ResourceException ex){
            logger.error("Error occur at okta api {}",ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(),null,ex.getMessage()));

        }
        catch (Exception e){
            logger.error("Error occur while fetching application list due to {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),null,e.getMessage()));

        }
    }
 }
