package com.coindcx.api.persistance.POJO.response;

import com.coindcx.api.persistance.POJO.Transaction;
import com.coindcx.api.persistance.entity.OrderBookEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProcessOrderResponse {
    OrderBookEntity orderBookEntity;
    List<Transaction> txns;
    Boolean orderMatched;
}
