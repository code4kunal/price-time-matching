package com.ordermatching.api.persistence.entity;


import com.ordermatching.api.constants.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_book")
public class OrderBookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderID;

    @Column(name = "time")
    private Time time;

    @Column(name = "size")
    private BigDecimal size;

    @Column(name = "price")
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name="side")
    private Action side = Action.BUY;

    @Column(name = "is_active")
    private Boolean isActive = Boolean.TRUE;

    public OrderBookEntity(Time time,  BigDecimal size, BigDecimal price, Action side, Boolean isActive){
        this.time = time;
        this.size = size;
        this.price = price;
        this.side = side;
        this.isActive = isActive;
    }

}
