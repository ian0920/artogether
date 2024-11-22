package com.artogether.product.prd_order;


import com.artogether.common.member.Member;
import com.artogether.product.prd_coup.Prd_Coup;
import com.artogether.product.prd_order_detail.Prd_Order_Detail;
import com.artogether.product.prd_return.Prd_Return;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="prd_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prd_Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;
	
	@Column(name="ship_date")
	private LocalDateTime ship_date;
	
	@Column(name="order_date")
	private LocalDateTime order_date;
	
	@Column(name="status")
	private String status;
	
	@ManyToMany
    @JoinTable
    (name = "order_coupon",joinColumns = @JoinColumn(name = "prd_order_id"),
     inverseJoinColumns = @JoinColumn(name = "prd_coup_id") )
    
    private Set<Prd_Coup> coupons = new HashSet<>();
    
    @Column(name="total_price")
	private Integer total_price;
    
    @Column(name="discount")
	private Integer discount;
    
    @Column(name="paid")
	private Integer paid;
    
    @Column(name="payment_method")
    private String payment_method;
    
    @OneToMany(mappedBy = "prd_order", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Prd_Return> prd_returns;
    
    @OneToMany(mappedBy = "prd_Order", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Prd_Order_Detail> prd_order_details;


}
