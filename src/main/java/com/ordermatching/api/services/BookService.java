package com.ordermatching.api.services;

import com.ordermatching.api.constants.Action;
import com.ordermatching.api.constants.Errors;
import com.ordermatching.api.exception.CustomException;
import com.ordermatching.api.persistence.POJO.Transaction;
import com.ordermatching.api.persistence.POJO.response.ProcessedOrderResponse;
import com.ordermatching.api.persistence.POJO.request.AddOrderRequest;
import com.ordermatching.api.persistence.dao.OrderBookDAO;
import com.ordermatching.api.persistence.entity.OrderBookEntity;
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

   /*
    * This function process incoming order based on the given side, i.e buy and sell.
    *
    * */
    public ProcessedOrderResponse processOrder(AddOrderRequest req) throws CustomException {
        Multimap<BigDecimal, OrderBookEntity> mapOfOrders = ArrayListMultimap.create();
        String action = req.getSide();
        String side = action.equalsIgnoreCase(BUY) ? BUY : SELL;
        validateOrderRequest(req);

        switch (side) {
            case "BUY":
                List<OrderBookEntity> sellOrders = orderBookDAO.getAllSellOrdersSortedByPriceAndActive(Action.SELL);
                mapOfOrders = sellOrders.isEmpty() ? ArrayListMultimap.create() : getOrderMap(sellOrders, SELL);
                ProcessedOrderResponse buyOrderResponse = matchOrders(req, mapOfOrders, SELL);
                return buyOrderResponse;

            case "SELL":
                List<OrderBookEntity> buyOrders = orderBookDAO.getAllBuyOrdersSortedByPriceAndActive(Action.BUY);
                mapOfOrders = buyOrders.isEmpty() ? ArrayListMultimap.create() : getOrderMap(buyOrders, BUY);
                ProcessedOrderResponse sellOrderResponse = matchOrders(req, mapOfOrders, BUY);
                return sellOrderResponse;
            default:
                throw new CustomException(Errors.INVALID_SIDE, "400");
        }
    }
    /*
    * This function matches the incoming order with existing ones based on price-time priority matching.
    *
    * */
    private ProcessedOrderResponse matchOrders(AddOrderRequest req, Multimap<BigDecimal, OrderBookEntity> mapOfOrders, String side) {
        AddOrderRequest originalReq = req;
        List<Transaction> listOfTxns = new ArrayList<>();
        List<Transaction> listOfTxnToadd = new ArrayList<>();
        OrderBookEntity savedCompletedOrder = new OrderBookEntity();
        String action = req.getSide().equalsIgnoreCase("BUY") ? "SELL" : "BUY";
        Set<BigDecimal> listOfSelectedOrders = reArrangeOrderByprice(req, mapOfOrders, action);

        if (listOfSelectedOrders.isEmpty()) {
            OrderBookEntity orderToSave = new OrderBookEntity();
            orderToSave.setPrice(req.getPrice());
            orderToSave.setSide(Action.valueOf(req.getSide().toUpperCase()));
            orderToSave.setSize(req.getSize());
            orderToSave.setTime(req.getTime());
            OrderBookEntity savedOrder = orderBookDAO.save(orderToSave);
            return new ProcessedOrderResponse(savedOrder, new ArrayList<>(), Boolean.FALSE);
        } else {
            if (side.equalsIgnoreCase(SELL)) {
                for (BigDecimal price : listOfSelectedOrders) {
                    Collection<OrderBookEntity> ordersOfPrice = mapOfOrders.get(price);
                    for (OrderBookEntity or : ordersOfPrice) {
                        if (req.getSize().compareTo(BigDecimal.ZERO) > 0 && req.getSize().compareTo(or.getSize()) > 0) {
                            req.setSize(req.getSize().subtract(or.getSize()));
                            or.setIsActive(Boolean.FALSE);
                            or.setSize(BigDecimal.ZERO);
                            orderBookDAO.save(or);
                            listOfTxnToadd = getTxnList(or);
                            listOfTxns.addAll(listOfTxnToadd);
                        } else if (req.getSize().compareTo(BigDecimal.ZERO) > 0
                                && (req.getSize().compareTo(or.getSize()) < 0) || req.getSize().compareTo(or.getSize()) == 0) {
                            or.setSize(or.getSize().subtract(req.getSize()));
                            orderBookDAO.save(or);
                            req.setSize(BigDecimal.ZERO);
                            listOfTxnToadd = getTxnList(or);
                            listOfTxns.addAll(listOfTxnToadd);
                        }else if (req.getSize().compareTo(BigDecimal.ZERO) == 0){
                            OrderBookEntity completedOrder = new OrderBookEntity(originalReq.getTime(), originalReq.getSize(), originalReq.getPrice(), Action.SELL, Boolean.FALSE);
                            savedCompletedOrder = orderBookDAO.save(completedOrder);
                        }
                    }
                }

                ProcessedOrderResponse response = new ProcessedOrderResponse(savedCompletedOrder, listOfTxns, Boolean.TRUE);
                return response;
            } else {
                for (BigDecimal price : listOfSelectedOrders) {
                    Collection<OrderBookEntity> ordersOfPrice = mapOfOrders.get(price);
                    for (OrderBookEntity or : ordersOfPrice) {
                        if (req.getSize().compareTo(BigDecimal.ZERO) > 0 && req.getSize().compareTo(or.getSize()) > 0) {
                            req.setSize(req.getSize().subtract(or.getSize()));
                            or.setIsActive(Boolean.FALSE);
                            or.setSize(BigDecimal.ZERO);
                            orderBookDAO.save(or);
                            listOfTxnToadd = getTxnList(or);
                            listOfTxns.addAll(listOfTxnToadd);
                        } else if (req.getSize().compareTo(BigDecimal.ZERO) > 0 && req.getSize().compareTo(or.getSize()) < 0) {
                            req.setSize(BigDecimal.ZERO);
                            or.setSize(or.getSize().subtract(req.getSize()));
                            orderBookDAO.save(or);
                            listOfTxnToadd = getTxnList(or);
                            listOfTxns.addAll(listOfTxnToadd);
                        } else if (req.getSize().compareTo(BigDecimal.ZERO) == 0){
                            OrderBookEntity completedOrder = new OrderBookEntity(originalReq.getTime(), originalReq.getSize(), originalReq.getPrice(), Action.SELL, Boolean.FALSE);
                            savedCompletedOrder = orderBookDAO.save(completedOrder);
                        }
                    }

                }
            }

            ProcessedOrderResponse response = new ProcessedOrderResponse(savedCompletedOrder, listOfTxns, Boolean.TRUE);
            return response;
        }
    }

    private Set<BigDecimal> reArrangeOrderByprice(AddOrderRequest req, Multimap<BigDecimal, OrderBookEntity> mapOfOrders, String action) {
        if (action.equalsIgnoreCase("SELL")) {
            return mapOfOrders.keys().stream()
                    .filter(key -> key.compareTo(req.getPrice()) < 0)
                    .sorted()
                    .collect(Collectors.toSet());
        } else {
            return mapOfOrders.keys().stream()
                    .filter(key -> key.compareTo(req.getPrice()) > 0)
                    .sorted()
                    .collect(Collectors.toSet());

        }
    }
    /*
    * This function generates a txn object to provide information on the orders consumed
    * while processing/matching any incoming order.
    *
    * */
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
    /*
     * This function creates a multimap for existing orders which has sorted
     * set based on time in the values for each key.
     *
     * */
    private Multimap<BigDecimal, OrderBookEntity> getOrderMap(List<OrderBookEntity> sellOrders, String side) throws CustomException {
        Multimap<BigDecimal, OrderBookEntity> mapOfOrders = ArrayListMultimap.create();
        switch (side) {
            case "SELL":
                List<OrderBookEntity> sortedSellOrders = sellOrders.stream().sorted(Comparator.comparing(or -> or.getTime())).collect(Collectors.toList());
                sortedSellOrders.stream()
                        .sorted((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()))
                        .forEach(or -> mapOfOrders.put(or.getPrice(), or));
                return mapOfOrders;
            case "BUY":
                List<OrderBookEntity> sortedBuyOrders = sellOrders.stream().sorted(Comparator.comparing(or -> or.getTime())).collect(Collectors.toList());
                sortedBuyOrders.stream()
                        .sorted((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()))
                        .forEach(or -> mapOfOrders.put(or.getPrice(), or));
                return mapOfOrders;
            default:
                throw new CustomException(Errors.INVALID_SIDE, "400");
        }

    }
    /*
    * A simple validation wrapper for the add order request.
    *
    * */
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

    public List<OrderBookEntity> getAllOrders(Boolean active ) {
        List<OrderBookEntity> orders = new ArrayList<>();
            orders = orderBookDAO.findByIsActive(active);
        return orders;
    }
}
