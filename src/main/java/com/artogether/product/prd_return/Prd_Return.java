package com.artogether.product.prd_return;

import com.artogether.product.prd_order.Prd_Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="prd_return")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prd_Return {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Prd_Order prd_order;

	@Column(name="prd_name")
	private String prd_name;
	
	@Column(name="reason")
	private String reason;
	
	@Column(name="type")
	private Integer type;
	
	@Column(name = "apply_date")
	private LocalDateTime apply_date;
	
	@Column(name = "return_date")
	private LocalDateTime return_date;
	
	@Column(name="refund")
	private Integer refund;
	
	@Column(name="status")
	private Integer status;
	
	
}
