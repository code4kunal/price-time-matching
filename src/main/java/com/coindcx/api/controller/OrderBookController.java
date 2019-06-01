package com.coindcx.api.controller;

import com.coindcx.api.exception.CustomException;
import com.coindcx.api.persistance.POJO.response.ApiResponse;
import com.coindcx.api.persistance.POJO.response.ApiResponseSuccess;
import com.coindcx.api.persistance.POJO.response.ProcessOrderResponse;
import com.coindcx.api.persistance.POJO.request.AddOrderRequest;
import com.coindcx.api.persistance.entity.OrderBookEntity;
import com.coindcx.api.services.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;


@RequestMapping(value = "/book")
public class OrderBookController {
    @Autowired
    private BookService bookService;


    @RequestMapping(method = POST, value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addOrder(@RequestBody AddOrderRequest addOrderRequest,
                                                HttpServletRequest request) throws JsonProcessingException,
            CustomException, UnsupportedEncodingException, GeneralSecurityException {

        ProcessOrderResponse response = bookService.processOrder(addOrderRequest);
        return new ResponseEntity<>(new ApiResponseSuccess(response, "Order processed successfully."), HttpStatus.OK);
    }

    @RequestMapping(method = GET, value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getOrderById(@RequestBody AddOrderRequest addOrderRequest,
                                                    @RequestParam(required = true) Long orderId,
                                                    HttpServletRequest request) throws JsonProcessingException,
            CustomException, UnsupportedEncodingException, GeneralSecurityException {

        OrderBookEntity response = bookService.getOrder(orderId);
        return new ResponseEntity<>(new ApiResponseSuccess(response, "Order fethced successfully."), HttpStatus.OK);
    }

    @RequestMapping(method = GET, value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAllPaginatedOrders(@RequestBody AddOrderRequest addOrderRequest,
                                                             @RequestParam Long orderId,
                                                             @RequestParam(name = "limit", required = false, defaultValue = "50") String limit,
                                                             @RequestParam(name = "offset", required = false, defaultValue = "0") String offset,
                                                             HttpServletRequest request) throws JsonProcessingException,
            CustomException, UnsupportedEncodingException, GeneralSecurityException {

        List<OrderBookEntity> response = bookService.getAllOrders();
        return new ResponseEntity<>(new ApiResponseSuccess(response, "Orders fetched successfully."), HttpStatus.OK);
    }
}
