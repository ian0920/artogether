package com.artogether.product.my_prd_coup;

import com.artogether.common.member.Member;
import com.artogether.product.prd_coup.Prd_Coup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;



@Entity
@Table(name = "my_prd_coup")
@IdClass(My_Prd_Coup.CompositeCoup.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class My_Prd_Coup {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;
	
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prd_coup_id", referencedColumnName = "id")
	private Prd_Coup prd_coup;
	
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "receive_date")
	private  LocalDateTime receive_date;
	
	@Column(name = "use_date")
	private  LocalDateTime use_date;

	/********************************
	 以下複合主鍵的用法建議再老師確認是否正確
	 缺少複合主鍵的getter setter
	 *********************************/



	public CompositeCoup getCompositeCoup() {
		return new CompositeCoup(member.getId(), prd_coup.getId());
	}

	public void setCompositeCoup(CompositeCoup key) {
		this.member = new Member();
		this.member.setId(key.getMemberId());
		this.prd_coup = new Prd_Coup();
		this.prd_coup.setId(key.getPrdCouponId());
	}



	public static class CompositeCoup implements Serializable {
		private static final long serialVersionUID = 1L;

		private Integer member;
		private Integer prd_coup;

		public CompositeCoup() {
			super();
		}

		public CompositeCoup(Integer memberId, Integer prdCouponId) {
			super();
			this.member = memberId;
			this.prd_coup = prdCouponId;
		}

		// Getters 和 Setters
		public Integer getMemberId() {
			return member;
		}

		public void setMemberId(Integer memberId) {
			this.member = memberId;
		}

		public Integer getPrdCouponId() {
			return prd_coup;
		}

		public void setPrdCouponId(Integer prdCouponId) {
			this.prd_coup = prdCouponId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((member == null) ? 0 : member.hashCode());
			result = prime * result + ((prd_coup == null) ? 0 : prd_coup.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			CompositeCoup other = (CompositeCoup) obj;
			return member.equals(other.member) && prd_coup.equals(other.prd_coup);
		}

	}



}
