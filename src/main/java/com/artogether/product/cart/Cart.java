package com.artogether.product.cart;

import com.artogether.common.member.Member;
import com.artogether.product.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "cart")
@IdClass(Cart.CartId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {


    @Id
    @ManyToOne
    @JoinColumn(name = "prd_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private Member member;

    @Column(name = "qty", nullable = false)
    private Integer qty;

	/**************************
	  缺少複合主鍵的Getter Setter
	***************************/


	public static class CartId implements Serializable {

        private Integer product_id; 
        private Integer member_id;
		public CartId() {
			
		}
		public CartId(Integer product_id, Integer member_id) {
			super();
			this.product_id = product_id;
			this.member_id = member_id;
		}
		public Integer getProduct_id() {
			return product_id;
		}
		public void setProduct_id(Integer product_id) {
			this.product_id = product_id;
		}
		public Integer getMember_id() {
			return member_id;
		}
		public void setMember_id(Integer member_id) {
			this.member_id = member_id;
		}
		@Override
		public int hashCode() {
			return Objects.hash(member_id, product_id);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CartId other = (CartId) obj;
			return Objects.equals(member_id, other.member_id) && Objects.equals(product_id, other.product_id);
		} 

            }

}
	


