package com.artogether.product.prd_report;

import com.artogether.common.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "prd_report")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prd_Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;

	/* 以下寫法會報錯 請再確認正確使用方式 */

//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "order_id", referencedColumnName = "order_id")
//	private Prd_Order_Detail prd_order_detail;
//
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "prd_id", referencedColumnName = "prd_id")
//	private Prd_Order_Detail prd_order_detail2;

	@Column(name = "report_time")
	private LocalDateTime report_time;

	@Column(name = "reason")
	private String reason;

	@Column(name = "status")
	private Integer status;

}
