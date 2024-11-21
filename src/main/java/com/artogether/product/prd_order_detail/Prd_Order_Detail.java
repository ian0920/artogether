package com.artogether.product.prd_order_detail;

import com.artogether.product.Prd_Order_Product_Id;
import com.artogether.product.prd_order.Prd_Order;
import com.artogether.product.prd_report.Prd_Report;
import com.artogether.product.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;



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
	private Prd_Order prd_order;
	
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "prd_id", referencedColumnName = "id")
	private Product product;
	
	@Column(name = "qty")
	private Integer qty;
	
	@Column(name = "price")
	private Integer price;
	
	@OneToMany(mappedBy = "prd_order_detail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("id asc")
	private Set<Prd_Report> prd_reports;
	
	@OneToMany(mappedBy = "prd_order_detail2", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("id asc")
	private Set<Prd_Report> prd_reports2;



}
