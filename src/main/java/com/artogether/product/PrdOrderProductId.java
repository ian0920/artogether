package com.artogether.product;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;



public class PrdOrderProductId implements Serializable {
	private Integer prdOrder;
	private Integer product;
	
	public PrdOrderProductId() {
	}

	public PrdOrderProductId(Integer prdOrderId, Integer productId) {
		super();
		this.prdOrder = prdOrderId;
		this.product = productId;
	}
	
	public Integer getPrdOrder() {
		return prdOrder;
	}
	public void setPrdOrder(Integer prdOrder) {
		this.prdOrder = prdOrder;
	}
	public Integer getProduct() {
		return product;
	}
	public void setProduct(Integer product) {
		this.product = product;
	}

	@Override
	public int hashCode() {
		return Objects.hash(prdOrder, product);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrdOrderProductId other = (PrdOrderProductId) obj;
		return Objects.equals(prdOrder, other.prdOrder) && Objects.equals(product, other.product);
	}
	
}
