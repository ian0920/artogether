package com.artogether.product.prd_coup;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.product.my_prd_coup.MyPrdCoup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;



@Entity
@Table(name= "prd_coup")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrdCoup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", updatable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "business_id", referencedColumnName = "id")
	private BusinessMember businessMember;

	@Column(name = "prd_coup_name")
	private String name;

	@Column(name = "status")
	private Integer status;

	@Column(name = "type")
	private Integer type;

	@Column(name = "deduction")
	private Integer deduction;

	@Column(name = "ratio")
	private  BigDecimal ratio;

	@Column(name = "start_date")
	private  LocalDateTime startDate;

	@Column(name = "end_date")
	private  LocalDateTime endDate;

	@Column(name = "duration")
	private Integer duration;

	@Column(name = "threshold")
	private Integer threshold;

	@OneToMany(mappedBy = "prdCoup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<MyPrdCoup> myPrdCoups;

//	@ManyToMany(mappedBy = "coupons")
//	private Set<PrdOrder> orders = new HashSet<>();

}
