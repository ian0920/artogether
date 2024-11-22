package com.artogether.product;

import java.io.Serializable;
import java.util.Objects;




public class Prd_Order_Product_Id implements Serializable {
	private Integer prd_Order;
	private Integer product;
	
	public Prd_Order_Product_Id() {
		
	}
	
	public Integer getPrd_Order_Id() {
		return prd_Order;
	}
	public void setPrd_Order_Id(Integer prd_Order_Id) {
		this.prd_Order = prd_Order_Id;
	}
	public Integer getProduct_Id() {
		return product;
	}
	public void setProduct_Id(Integer product_Id) {
		this.product = product_Id;
	}
	public Prd_Order_Product_Id(Integer prd_Order_Id, Integer product_Id) {
		super();
		this.prd_Order = prd_Order_Id;
		this.product = product_Id;
	}
	@Override
	public int hashCode() {
		return Objects.hash(prd_Order, product);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prd_Order_Product_Id other = (Prd_Order_Product_Id) obj;
		return Objects.equals(prd_Order, other.prd_Order) && Objects.equals(product, other.product);
	}
	
}
