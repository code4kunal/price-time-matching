package com.ordermatching.api.persistence.POJO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Time;

@Getter
@Setter
public class Transaction {
    private BigDecimal size;
    private BigDecimal price;
    private Time time;
    private Long orderID;
}
