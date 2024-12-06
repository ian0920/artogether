package com.artogether.venue.vneorder;

import com.artogether.common.member.Member;
import com.artogether.venue.venue.Venue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
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

    // 訂單日期
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    // 總價格
    private Integer totalPrice;

    // 優惠券ID
    private Integer vneCoupId;

    // 應該支付的金額
    private Integer shouldPaid;

    // 已支付金額
    private Integer paid;

    // 開始日期
    @Column(name = "start_date")
    private LocalDate startDate;

    // 開始時間
    @Column(name = "start_time")
    private Integer startTime;

    // 結束日期
    @Column(name = "end_date")
    private LocalDate endDate;

    // 結束時間
    @Column(name = "end_time")
    private Integer endTime;

    // 評論
    private String review;

    // 評分星數
    private Integer stars;

    // 支付方式
    private PaymentMethodEnum paymentMethod;

    // 訂單狀態
    private OrderStatusEnum status;

    // 申請日期
    @Column(name = "apply_date")
    private LocalDateTime applyDate;

    // 退款日期
    @Column(name = "refund_date")
    private LocalDateTime refundDate;
}
