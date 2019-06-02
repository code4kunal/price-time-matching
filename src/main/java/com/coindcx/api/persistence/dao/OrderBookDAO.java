package com.coindcx.api.persistence.dao;

import com.coindcx.api.constants.Action;
import com.coindcx.api.persistence.entity.OrderBookEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderBookDAO extends CrudRepository<OrderBookEntity, Long> {

    @Query("Select ord from OrderBookEntity ord where ord.side = :side and ord.isActive = true order by ord.price asc")
    List<OrderBookEntity> getAllSellOrdersSortedByPriceAndActive(@Param("side") Action side);

    @Query("Select ord from OrderBookEntity ord where ord.side = :side and ord.isActive = true order by ord.price desc")
    List<OrderBookEntity> getAllBuyOrdersSortedByPriceAndActive(@Param("side") Action side);

    List<OrderBookEntity> findByIsActive(Boolean isActive);
}
