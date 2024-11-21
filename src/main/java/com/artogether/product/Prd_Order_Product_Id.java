package com.artogether.product;

import java.io.Serializable;
import java.util.Objects;




public class Prd_Order_Product_Id implements Serializable {
	private Integer prd_Order_Id;
	private Integer product_Id;
	
	public Prd_Order_Product_Id() {
		
	}
	
	public Integer getPrd_Order_Id() {
		return prd_Order_Id;
	}
	public void setPrd_Order_Id(Integer prd_Order_Id) {
		this.prd_Order_Id = prd_Order_Id;
	}
	public Integer getProduct_Id() {
		return product_Id;
	}
	public void setProduct_Id(Integer product_Id) {
		this.product_Id = product_Id;
	}
	public Prd_Order_Product_Id(Integer prd_Order_Id, Integer product_Id) {
		super();
		this.prd_Order_Id = prd_Order_Id;
		this.product_Id = product_Id;
	}
	@Override
	public int hashCode() {
		return Objects.hash(prd_Order_Id, product_Id);
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
		return Objects.equals(prd_Order_Id, other.prd_Order_Id) && Objects.equals(product_Id, other.product_Id);
	}
	
}
