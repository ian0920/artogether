package com.artogether.product.prd_return;

import com.artogether.product.prd_order.model.PrdOrder;
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
public class PrdReturn {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private PrdOrder prdOrder;

	@Column(name="prd_name")
	private String prdName;
	
	@Column(name="reason")
	private String reason;
	
	@Column(name="type")
	private Integer type;
	
	@Column(name = "apply_date")
	private LocalDateTime applyDate;
	
	@Column(name = "return_date")
	private LocalDateTime returnDate;
	
	@Column(name="refund")
	private Integer refund;
	
	@Column(name="status")
	private Integer status;
	
	
}
