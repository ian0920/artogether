package com.artogether.product.my_prd_coup;

import com.artogether.common.member.Member;
import com.artogether.product.prd_coup.PrdCoup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;



@Entity
@Table(name = "my_prd_coup")
@IdClass(MyPrdCoup.MyPrdCoupId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPrdCoup {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;
	
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prd_coup_id", referencedColumnName = "id")
	private PrdCoup prdCoup;
	
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "receive_date")
	private  Timestamp receiveDate;
	
	@Column(name = "use_date")
	private  Timestamp useDate;

	/********************************
	 以下複合主鍵的用法建議再老師確認是否正確
	 缺少複合主鍵的getter setter
	 *********************************/
	public void setMyPrdCoupId(MyPrdCoupId myPrdCoupId) {
		this.member.setId(myPrdCoupId.getMember());
		this.prdCoup.setId(myPrdCoupId.getPrdCoup());
	}
	public MyPrdCoupId getMyPrdCoupId() {
		return new MyPrdCoupId(this.member.getId(),this.prdCoup.getId());
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MyPrdCoupId implements Serializable {
		private static final long serialVersionUID = 1L;

		private Integer member;
		private Integer prdCoup;


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((member == null) ? 0 : member.hashCode());
			result = prime * result + ((prdCoup == null) ? 0 : prdCoup.hashCode());
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
			MyPrdCoupId other = (MyPrdCoupId) obj;
			return member.equals(other.member) && prdCoup.equals(other.prdCoup);
		}

	}



}
