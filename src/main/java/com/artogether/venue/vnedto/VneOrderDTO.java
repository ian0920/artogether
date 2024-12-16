package com.artogether.venue.vnedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VneOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer vneId;
    private Integer memId;
    private String memName;
    private String memPhone;
    private Integer orderId;
    // 總價格
    private Integer totalPrice;

    // 優惠券ID
    private Integer vneCoupId;

    // 應該支付的金額
    private Integer shouldPaid;

    // 已支付金額
    private Integer paid;

    // 開始日期
    private LocalDate startDate;

    //開始時間
    private Integer startTime;

    // 結束日期
    private LocalDate endDate;

    //結束時間
    private Integer endTime;
}
