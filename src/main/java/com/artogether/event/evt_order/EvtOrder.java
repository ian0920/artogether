package com.artogether.event.evt_order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evt_order")
public class EvtOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "evt_id", nullable = false)
    private Integer evtId;

    @Column(name = "member_id", nullable = false)
    private Integer memberId;


    private Byte status;

    @Column(name = "order_Date", updatable = false, insertable = false)
    @CreationTimestamp
    private Timestamp orderDate;

    @Column(nullable = false)
    private Integer qty;

    @Column(name = "evt_coup_id")
    private Integer evtCoupId;


    private Integer paid;

    @Column(name = "total_price")
    private Integer totalPrice;

    private Integer discount;
    private Integer refund;

    @Column(name = "refund_apply_date")
    private Timestamp refundApplyDate;

    @Column(name = "refund_date")
    private Timestamp refundDate;

    @Column(name = "payment_method")
    private Byte paymentMethod;


}
