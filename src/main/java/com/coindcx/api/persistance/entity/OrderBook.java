package com.coindcx.api.persistance.entity;


import com.coindcx.api.constants.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_book")
public class OrderBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Long driverID;

    @Column(name = "time")
    private Time time;

    @Column(name = "size")
    private Long size;

    @Column(name = "price")
    private Long price;

    @Enumerated(EnumType.STRING)
    @Column(name="side")
    private Action side = Action.BUY;
}
