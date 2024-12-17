package com.artogether.product.prd_order.model;


import com.artogether.common.member.Member;
import com.artogether.product.prd_order_detail.PrdOrderDetail;
import com.artogether.product.prd_return.PrdReturn;
import com.artogether.product.prd_coup.PrdCoup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;


@Entity
@Table(name="prd_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrdOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Integer id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;

	@Column(name="order_name", nullable = false, length = 50)
	private String orderName;

	@Column(name="order_phone", nullable = false, length = 15)
	private String orderPhone;

	@Column(name="order_address", nullable = false, length = 255)
	private String orderAddress;
	
	@Column(name="ship_date")
	private Timestamp shipDate;
	
	@Column(name="order_date")
	private Timestamp orderDate;
	
	@Column(name="status")
	private String status;
	
//	@ManyToMany
//    @JoinTable
//    (name = "order_coupon",joinColumns = @JoinColumn(name = "prd_order_id"),
//     inverseJoinColumns = @JoinColumn(name = "prd_coup_id") )
//    private Set<PrdCoup> coupons = new HashSet<>();


    @Column(name="total_price")
	private Integer totalPrice;

    @Column(name="discount")
	private Integer discount;

    @Column(name="paid")
	private Integer paid;

    @Column(name="payment_method")
    private String paymentMethod;

	@JsonIgnore
    @OneToMany(mappedBy = "prdOrder", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<PrdReturn> prdReturns;

	@JsonIgnore
    @OneToMany(mappedBy = "prdOrder", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<PrdOrderDetail> prdOrderDetails;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "PrdOrder{" +
				"id=" + id +
				", member=" + member +
				", orderName='" + orderName + '\'' +
				", orderPhone='" + orderPhone + '\'' +
				", orderAddress='" + orderAddress + '\'' +
				", shipDate=" + shipDate +
				", orderDate=" + orderDate +
				", status='" + status + '\'' +
				", totalPrice=" + totalPrice +
				", discount=" + discount +
				", paid=" + paid +
				'}';
	}

}
