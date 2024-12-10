package com.artogether.product.prd_coup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.artogether.common.business_member.BusinessMember;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrdCoupDto {

    private Integer id;  // 優惠券 ID
    
    private String businessMemberName; 
    private Integer businessMemberId; 
    private String name;                // 優惠券名稱
    private Integer status;             // 狀態
    private Integer type;               // 折扣類型
    private Integer deduction;          // 金額扣減
    private BigDecimal ratio;           // 折扣比例
    private Timestamp startDate;        // 開始日期
    private Timestamp endDate;          // 結束日期
    private Integer duration;           // 有效期（天數）
    private Integer threshold;          // 使用門檻金額
}
