package com.coindcx.api.persistance.POJO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
public class Transactions {
    private Long size;
    private Long price;
    private Time time;
    private Long orderID;
}
