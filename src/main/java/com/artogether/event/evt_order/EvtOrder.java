package com.artogether.event.evt_order;

import com.artogether.common.member.Member;
import com.artogether.event.event.Event;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "evt_id", referencedColumnName = "id")
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

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
