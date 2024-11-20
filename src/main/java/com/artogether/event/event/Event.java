package com.artogether.event.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "business_id")
    private Integer businessId;

    @Column(nullable = false)
    private String name;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "delay_date")
    private Timestamp delayDate;

    @Column(name="catalog_id", nullable = false)
    private Byte catalogId;
    private Integer price;
    private String description;
    private Byte status;
    private Integer maximum;
    private Integer minimum;
    private Integer enrolled;


}
