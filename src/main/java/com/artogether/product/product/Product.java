package com.artogether.product.product;


import com.artogether.common.business_member.BusinessMember;
import com.artogether.product.cart.model.Cart;
import com.artogether.product.prd_catalog.PrdCatalog;
import com.artogether.product.prd_img.PrdImg;
import com.artogether.product.prd_order_detail.PrdOrderDetail;
import com.artogether.product.prd_review.PrdReview;
import com.artogether.product.prd_track.PrdTrack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "business_id", referencedColumnName = "id")
	private BusinessMember businessMember;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "catalog_id", referencedColumnName = "id")
	private PrdCatalog prdCatalog;

	@Column(name = "price")
	private Integer price;

	@Column(name = "qty")
	private Integer qty;

	@Column(name = "description")
	private String description;

	@Column(name = "status")
	private Integer status;

	@Column(name = "all_stars")
	private Integer allStars;

	@Column(name = "all_reviews")
	private Integer allReviews;

	@OneToMany(mappedBy = "product",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<PrdImg> prdImg;
	
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<PrdReview> prdReviews;
	
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<PrdOrderDetail> prdOrderDetail;

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<PrdTrack> prdTrack;
	
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Cart> cart;



	

}
