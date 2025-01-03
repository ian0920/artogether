package com.artogether.product.prd_order_detail;

import com.artogether.product.prd_order.model.PrdOrder;
import com.artogether.product.prd_report.PrdReport;
import com.artogether.product.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;




@Entity
@Table(name="prd_order_detail")
@IdClass(PrdOrderProductId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PrdOrderDetail {

//	@JsonIgnore
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private PrdOrder prdOrder;

//	@JsonIgnore
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "prd_id", referencedColumnName = "id")
	private Product product;
	
	@Column(name = "qty")
	private Integer qty;
	
	@Column(name = "price")
	private Integer price;

	@JsonIgnore
	/* 以下寫法會報錯 請再確認正確使用方式 */
	@OneToMany(mappedBy = "prdOrderDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("id asc")
	private Set<PrdReport> prdReport;



	public void setPrdOrderProductId(PrdOrderProductId prdOrderProductId) {
		this.prdOrder.setId(prdOrderProductId.getProduct());
		this.product.setId(prdOrderProductId.getProduct());
	}

	public PrdOrderProductId getPrdOrderProductId() {
		return new PrdOrderProductId(this.prdOrder.getId(), this.product.getId());
	}

	@Override
	public String toString() {
		return "PrdOrderDetail{" +
				"prdOrder=" + prdOrder +
				", product=" + product +
				", qty=" + qty +
				", price=" + price +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(prdOrder.getId(), product.getId());
	}


}
