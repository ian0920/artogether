package com.artogether.product.prd_return;

import com.artogether.product.prd_order.model.PrdOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private PrdOrder prdOrder;

	@Column(name="prd_name")
	private String prdName;
	
	@Column(name="reason")
	private String reason;
	
	@Column(name="type")
	private Integer type;
	
	@Column(name = "apply_date")
	private Timestamp applyDate;
	
	@Column(name = "return_date")
	private Timestamp returnDate;
	
	@Column(name="refund")
	private Integer refund;
	
	@Column(name="status")
	private Integer status;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	
}
