package com.coindcx.api.persistance.POJO.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Time;

@Getter
@Setter
public class AddOrderRequest {
    private BigDecimal size;
    private BigDecimal price;
    private Time time;
    private String side;
}
