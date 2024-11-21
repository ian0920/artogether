package com.artogether.venue.vneorder;

import com.artogether.common.member.Member;
import com.artogether.venue.venue.Venue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vne_order")
public class VneOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id",referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vne_id",referencedColumnName = "id")
    private Venue venue;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    private Integer totalPrice;

    private Integer vneCoupId;

    private Integer paid;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    private String review;

    private Integer stars;

    private PaymentMethodEnum paymentMethod;

    private OrderStatusEnum status;

    @Column(name = "apply_date")
    private LocalDateTime applyDate;

    @Column(name = "refund_date")
    private LocalDateTime refundDate;
}
