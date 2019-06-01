package com.coindcx.api.controller;

import com.coindcx.api.exception.CustomException;
import com.coindcx.api.persistance.POJO.ApiResponse;
import com.coindcx.api.persistance.POJO.ApiResponseSuccess;
import com.coindcx.api.persistance.POJO.request.AddOrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/*
*
*
*
*/
@RequestMapping(value = "/book")
public class OrderBookController
{
//    @RequestMapping(method = POST, value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ApiResponse> addOrder(@RequestBody AddOrderRequest createHubRequest,
//                                                @PathVariable("userID") Long userID, HttpServletRequest request) throws JsonProcessingException,
//            CustomException, UnsupportedEncodingException, GeneralSecurityException {
//
//        userService.checkIfUserExists(userID, request);
//        HubEntity hub = entityService.createHub(createHubRequest);
//        return new ResponseEntity<>(new ApiResponseSuccess(hub, "Hub created successfully."), HttpStatus.OK);
//    }

//    @RequestMapping(method = PUT, value = "/{userID}/hub/{hubID}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ApiResponse> updateHub(@RequestBody CreateHubRequest createHubRequest,
//                                                 @PathVariable("userID") Long userID, @PathVariable("hubID") Long hubID, HttpServletRequest request)
//            throws JsonProcessingException, CustomException, CustomResponseException, UnsupportedEncodingException,
//            GeneralSecurityException {
//
//        authService.validateJWTToken(request);
//        userService.checkIfUserExists(userID, request);
//        HubEntity hub = entityService.updateHub(createHubRequest, hubID);
//        return new ResponseEntity<>(new ApiResponseSuccess(hub, "Hub updated successfully."), HttpStatus.OK);
//    }
//
//    @RequestMapping(method = DELETE, value = "/{userID}/hub/{hubID}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ApiResponse> deleteHub(@PathVariable("userID") Long userID, @PathVariable("hubID") Long
//            hubID,
//                                                 HttpServletRequest request) throws JsonProcessingException, CustomException, CustomResponseException,
//            UnsupportedEncodingException, GeneralSecurityException {
//
//        authService.validateJWTToken(request);
//        userService.checkIfUserExists(userID, request);
//        entityService.deleteHub(hubID);
//        return new ResponseEntity<>(new ApiResponseSuccess(null, "Hub deleted successfully."), HttpStatus.OK);
//    }
//
//    @RequestMapping(method = GET, value = "/{userID}/truck", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ApiResponse> getAllTrucksOfClient(@PathVariable("userID") Long userID,
//                                                            @RequestParam Long clientId,
//                                                            @RequestParam(required = false) String model,
//                                                            @RequestParam(name = "limit", required = false, defaultValue = "50") String limit,
//                                                            @RequestParam(name = "offset", required = false, defaultValue = "0") String offset,
//                                                            HttpServletRequest request)
//            throws JsonProcessingException, CustomException, UnsupportedEncodingException, GeneralSecurityException, CustomResponseException {
//
//
//        authService.validateJWTToken(request);
//        userService.checkIfUserExists(userID, request);
//        PaginatedResponse response = entityService.getAllTrucksForThisClient(clientId, limit, offset, model);
//        return new ResponseEntity<>(new ApiResponseSuccess(response, ""), HttpStatus.OK);
//    }
}