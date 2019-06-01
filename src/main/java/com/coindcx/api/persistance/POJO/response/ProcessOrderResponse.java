package com.coindcx.api.persistance.POJO.response;

import com.coindcx.api.persistance.POJO.Transactions;
import com.coindcx.api.persistance.entity.OrderBookEntity;

import java.util.List;

public class ProcessOrderResponse {
    OrderBookEntity orderBookEntity;
    List<Transactions> txns;
}
