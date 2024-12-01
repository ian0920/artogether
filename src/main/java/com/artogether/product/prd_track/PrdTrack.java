package com.artogether.product.prd_track;


import com.artogether.common.member.Member;
import com.artogether.product.product.Product;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name="prd_track")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PrdTrack.PrdTrackId.class) // 指定複合主鍵類
public class PrdTrack {

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "prd_id", referencedColumnName = "id", nullable = false) // 商品外來鍵
	private Product product;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false) // 會員外來鍵
	private Member member;


	// 嵌套類：複合主鍵類
	public static class PrdTrackId implements Serializable {
		private Integer product; // 對應 Product 的主鍵
		private Integer member;  // 對應 Member 的主鍵

		public PrdTrackId() {}

		public PrdTrackId(Integer product, Integer member) {
			this.product = product;
			this.member = member;
		}

		// Getters 和 Setters
		public Integer getProduct() {
			return product;
		}

		public void setProduct(Integer product) {
			this.product = product;
		}

		public Integer getMember() {
			return member;
		}

		public void setMember(Integer member) {
			this.member = member;
		}

		// 必須覆蓋 equals 和 hashCode
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			PrdTrackId that = (PrdTrackId) o;
			return Objects.equals(product, that.product) && Objects.equals(member, that.member);
		}

		@Override
		public int hashCode() {
			return Objects.hash(product, member);
		}
	}
	
	
	
}
