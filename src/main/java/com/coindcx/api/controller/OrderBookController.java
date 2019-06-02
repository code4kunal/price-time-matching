package com.coindcx.api.controller;

import com.coindcx.api.exception.CustomException;
import com.coindcx.api.persistence.POJO.response.ApiResponse;
import com.coindcx.api.persistence.POJO.response.ApiResponseSuccess;
import com.coindcx.api.persistence.POJO.response.ProcessedOrderResponse;
import com.coindcx.api.persistence.POJO.request.AddOrderRequest;
import com.coindcx.api.persistence.entity.OrderBookEntity;
import com.coindcx.api.services.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping(value = "/book")
public class OrderBookController {

    @Autowired
    private BookService bookService;

    @RequestMapping(method = POST, value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addOrder(@RequestBody AddOrderRequest addOrderRequest,
                                                HttpServletRequest request) throws JsonProcessingException,
            CustomException, UnsupportedEncodingException, GeneralSecurityException {

        ProcessedOrderResponse response = bookService.processOrder(addOrderRequest);
        return new ResponseEntity<>(new ApiResponseSuccess(response, "Order processed successfully."), HttpStatus.OK);
    }

    @RequestMapping(method = GET, value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse>fetchAllOrders(@RequestParam(name = "active", required = false, defaultValue = "true") Boolean active,
                                                             HttpServletRequest request) throws JsonProcessingException,
            CustomException, UnsupportedEncodingException, GeneralSecurityException {

        List<OrderBookEntity> response = bookService.getAllOrders(active);
        return new ResponseEntity<>(new ApiResponseSuccess(response, "Order Book fetched successfully."), HttpStatus.OK);
    }
}
