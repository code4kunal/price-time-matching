package com.coindcx.api.services;

import com.coindcx.api.constants.Action;
import com.coindcx.api.constants.Errors;
import com.coindcx.api.exception.CustomException;
import com.coindcx.api.persistance.POJO.response.ProcessOrderResponse;
import com.coindcx.api.persistance.POJO.request.AddOrderRequest;
import com.coindcx.api.persistance.dao.OrderBookDAO;
import com.coindcx.api.persistance.entity.OrderBookEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private OrderBookDAO orderBookDAO;

    public ProcessOrderResponse processOrder(AddOrderRequest req) throws CustomException {
        String action = req.getSide();
        String side = action.equalsIgnoreCase(Action.BUY.toString()) ? Action.BUY.toString() : Action.SELL.toString();
        validateOrderRequest(req);
        switch (side) {
            case "BUY":
           List<OrderBookEntity> sellOrders = orderBookDAO.getAllSellOrdersSortedByPrice(Action.SELL);


                return null;
        }

    private void validateOrderRequest(AddOrderRequest req) throws CustomException {
        if (StringUtils.isBlank(req.getSide()) || (req.getSide().equalsIgnoreCase("BUY")
                && req.getSide().equalsIgnoreCase("SELL"))) {
            throw new CustomException(Errors.INVALID_SIDE, "400");
        } else if (req.getPrice() == null) {
            throw new CustomException(Errors.INVALID_PRICE, "400");
        } else if (req.getSize() == null) {
            throw new CustomException(Errors.INVALID_SIZE, "400");
        } else if (req.getTime() == null) {
            throw new CustomException(Errors.INVALID_TIME, "400");
        }
    }

    public List<OrderBookEntity> getAllOrders() {
        return new ArrayList<>();
    }

    public OrderBookEntity getOrder(Long orderId) {
        return null;
    }
}
