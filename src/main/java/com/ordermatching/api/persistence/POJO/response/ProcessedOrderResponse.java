package com.ordermatching.api.persistence.POJO.response;

import com.ordermatching.api.persistence.POJO.Transaction;
import com.ordermatching.api.persistence.entity.OrderBookEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProcessedOrderResponse {
    OrderBookEntity orderBookEntity;
    List<Transaction> txns;
    Boolean orderMatched;
}
