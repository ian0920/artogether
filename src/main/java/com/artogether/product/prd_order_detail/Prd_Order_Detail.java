package com.artogether.product.prd_order_detail;

import com.artogether.product.Prd_Order_Product_Id;
import com.artogether.product.prd_order.Prd_Order;
import com.artogether.product.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;



@Entity
@Table(name="prd_order_detail")
@IdClass(Prd_Order_Product_Id.class)
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Prd_Order_Detail {
	
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Prd_Order prd_Order;
	
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "prd_id", referencedColumnName = "id")
	private Product product;
	
	@Column(name = "qty")
	private Integer qty;
	
	@Column(name = "price")
	private Integer price;

	/* 以下寫法會報錯 請再確認正確使用方式 */
//	@OneToMany(mappedBy = "prd_order_detail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@OrderBy("id asc")
//	private Set<Prd_Report> prd_reports;
//
//	@OneToMany(mappedBy = "prd_order_detail2", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@OrderBy("id asc")
//	private Set<Prd_Report> prd_reports2;


	public void setPrd_Order_Product_Id(Prd_Order_Product_Id prd_order_product_Id) {
		this.prd_Order.setId(prd_order_product_Id.getProduct_Id());
		this.product.setId(prd_order_product_Id.getProduct_Id());
	}

	public Prd_Order_Product_Id getPrd_order_product_Id() {
		return new Prd_Order_Product_Id(this.prd_Order.getId(), this.product.getId());
	}



}
