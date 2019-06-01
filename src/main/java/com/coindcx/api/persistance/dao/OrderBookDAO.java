package com.coindcx.api.persistance.dao;

import com.coindcx.api.constants.Action;
import com.coindcx.api.persistance.entity.OrderBookEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderBookDAO extends CrudRepository<OrderBookEntity, Long>, OrderBookDaoCustom {

    @Query("Select or from OrderBookEntity or where or.side = :side order by or.price desc")
    List<OrderBookEntity> getAllSellOrdersSortedByPrice(@Param("side") Action side);

    @Query("Select or from OrderBookEntity or where or.side = :side order by or.price asc")
    List<OrderBookEntity> getAllBuyOrdersSortedByPrice(@Param("side") Action side);
}
