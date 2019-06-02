package com.coindcx.api.services;

import com.coindcx.api.constants.Action;
import com.coindcx.api.constants.Errors;
import com.coindcx.api.exception.CustomException;
import com.coindcx.api.persistance.POJO.Transaction;
import com.coindcx.api.persistance.POJO.response.ProcessOrderResponse;
import com.coindcx.api.persistance.POJO.request.AddOrderRequest;
import com.coindcx.api.persistance.dao.OrderBookDAO;
import com.coindcx.api.persistance.entity.OrderBookEntity;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private OrderBookDAO orderBookDAO;

    private static final String BUY = Action.BUY.toString();
    private static final String SELL = Action.SELL.toString();

    public ProcessOrderResponse processOrder(AddOrderRequest req) throws CustomException {
        Multimap<BigDecimal, OrderBookEntity> mapOfOrders = ArrayListMultimap.create();
        String action = req.getSide();
        String side = action.equalsIgnoreCase(BUY) ? BUY : SELL;
        validateOrderRequest(req);

        switch (side) {
            case "BUY":
                List<OrderBookEntity> sellOrders = orderBookDAO.getAllSellOrdersSortedByPrice(Action.SELL);
                mapOfOrders = sellOrders.isEmpty() ? ArrayListMultimap.create() : getOrderMap(sellOrders, SELL);
                ProcessOrderResponse buyOrderResponse = mapOfOrders.isEmpty() ? new ProcessOrderResponse() : matchOrders(req, mapOfOrders, SELL);
                return buyOrderResponse;
            case "SELL":
                List<OrderBookEntity> buyOrders = orderBookDAO.getAllBuyOrdersSortedByPrice(Action.BUY);
                mapOfOrders = buyOrders.isEmpty() ? ArrayListMultimap.create() : getOrderMap(buyOrders, BUY);
                ProcessOrderResponse sellOrderResponse = mapOfOrders.isEmpty() ? new ProcessOrderResponse() : matchOrders(req, mapOfOrders, BUY);
                return sellOrderResponse;
            default:
                throw new CustomException(Errors.INVALID_SIDE, "400");
        }
    }

    private ProcessOrderResponse matchOrders(AddOrderRequest req, Multimap<BigDecimal, OrderBookEntity> mapOfOrders, String side) {
        List<Transaction> listOfTxns = new ArrayList<>();
        OrderBookEntity savedCompletedOrder = new OrderBookEntity();
        Set<BigDecimal> listOfSelectedSellOrders = mapOfOrders.keys().stream().filter(key -> key.compareTo(req.getPrice()) > 1).collect(Collectors.toSet());
        if (listOfSelectedSellOrders.isEmpty()) {
            OrderBookEntity orderToSave = new OrderBookEntity();
            orderToSave.setPrice(req.getPrice());
            orderToSave.setSide(Action.valueOf(req.getSide().toUpperCase()));
            orderToSave.setSize(req.getSize());
            orderToSave.setTime(req.getTime());
            OrderBookEntity savedOrder = orderBookDAO.save(orderToSave);
            return new ProcessOrderResponse(savedOrder, new ArrayList<>(), Boolean.FALSE);
        } else {
            if (side.equalsIgnoreCase(SELL)) {
                for (BigDecimal price : listOfSelectedSellOrders) {
                    Collection<OrderBookEntity> ordersOfPrice = mapOfOrders.get(price);
                    for (OrderBookEntity or : ordersOfPrice) {
                        if (req.getSize().compareTo(BigDecimal.ZERO) > 1 &&
                                req.getPrice().compareTo(or.getPrice()) > 1 && req.getSize().compareTo(or.getSize()) > 1) {
                            req.setSize(req.getSize().subtract(or.getSize()));
                            or.setIsActive(Boolean.FALSE);
                            or.setSize(BigDecimal.ZERO);
                            orderBookDAO.save(or);
                            OrderBookEntity completedOrder = new OrderBookEntity(req.getTime(), req.getSize(), req.getPrice(), Action.BUY, Boolean.FALSE);
                            savedCompletedOrder = orderBookDAO.save(completedOrder);
                            listOfTxns = getTxnList(or);
                        } else if (req.getSize().compareTo(BigDecimal.ZERO) > 1 &&
                                req.getPrice().compareTo(or.getPrice()) > 1 && req.getSize().compareTo(or.getSize()) < 1) {
                            req.setSize(BigDecimal.ZERO);
                            or.setSize(or.getSize().subtract(req.getSize()));
                            orderBookDAO.save(or);
                            OrderBookEntity completedOrder = new OrderBookEntity(req.getTime(), req.getSize(), req.getPrice(), Action.BUY, Boolean.FALSE);
                            savedCompletedOrder = orderBookDAO.save(completedOrder);
                            listOfTxns = getTxnList(or);
                        }
                    }
                }

                ProcessOrderResponse response = new ProcessOrderResponse(savedCompletedOrder, listOfTxns, Boolean.TRUE);
                return response;
            } else {
                for (BigDecimal price : listOfSelectedSellOrders) {
                    Collection<OrderBookEntity> ordersOfPrice = mapOfOrders.get(price);
                    for (OrderBookEntity or : ordersOfPrice) {
                        if (req.getSize().compareTo(BigDecimal.ZERO) > 1 &&
                                req.getPrice().compareTo(or.getPrice()) < 1 && req.getSize().compareTo(or.getSize()) > 1) {
                            req.setSize(req.getSize().subtract(or.getSize()));
                            or.setIsActive(Boolean.FALSE);
                            or.setSize(BigDecimal.ZERO);
                            orderBookDAO.save(or);
                            OrderBookEntity completedOrder = new OrderBookEntity(req.getTime(), req.getSize(), req.getPrice(), Action.SELL, Boolean.FALSE);
                            savedCompletedOrder = orderBookDAO.save(completedOrder);
                            listOfTxns = getTxnList(or);
                        } else if (req.getSize().compareTo(BigDecimal.ZERO) > 1 &&
                                req.getPrice().compareTo(or.getPrice()) > 1 && req.getSize().compareTo(or.getSize()) < 1) {
                            req.setSize(BigDecimal.ZERO);
                            or.setSize(or.getSize().subtract(req.getSize()));
                            orderBookDAO.save(or);
                            OrderBookEntity completedOrder = new OrderBookEntity(req.getTime(), req.getSize(), req.getPrice(), Action.SELL, Boolean.FALSE);
                            savedCompletedOrder = orderBookDAO.save(completedOrder);
                            listOfTxns = getTxnList(or);
                        }
                    }

                }
            }
        }
    }

    private List<Transaction> getTxnList(OrderBookEntity or) {
        List<Transaction> listOfTxns = new ArrayList<>();
        Transaction txn = new Transaction();
        txn.setOrderID(or.getOrderID());
        txn.setPrice(or.getPrice());
        txn.setTime(or.getTime());
        txn.setSize(or.getSize());
        listOfTxns.add(txn);
        return listOfTxns;
    }

    private Multimap<BigDecimal, OrderBookEntity> getOrderMap(List<OrderBookEntity> sellOrders, String side) throws CustomException {
        Multimap<BigDecimal, OrderBookEntity> mapOfOrders = ArrayListMultimap.create();
        switch (side) {
            case "SELL":
                List<OrderBookEntity> sortedSellOrders = sellOrders.stream().sorted(Comparator.comparing(or -> or.getTime())).collect(Collectors.toList());
                sortedSellOrders.forEach(or -> mapOfOrders.put(or.getPrice(), or));
                return mapOfOrders;
            case "BUY":
                List<OrderBookEntity> sortedBuyOrders = sellOrders.stream().sorted(Comparator.comparing(or -> or.getTime())).collect(Collectors.toList());
                sortedBuyOrders.forEach(or -> mapOfOrders.put(or.getPrice(), or));
                return mapOfOrders;
            default:
                throw new CustomException(Errors.INVALID_SIDE, "400");
        }

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
