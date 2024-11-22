package com.artogether.product.product;


import com.artogether.common.business_member.BusinessMember;
import com.artogether.product.cart.Cart;
import com.artogether.product.prd_catalog.Prd_Catalog;
import com.artogether.product.prd_img.Prd_Img;
import com.artogether.product.prd_order_detail.Prd_Order_Detail;
import com.artogether.product.prd_review.Prd_Review;
import com.artogether.product.prd_track.Prd_Track;
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
	private BusinessMember business_member;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "catalog_id", referencedColumnName = "id")
	private Prd_Catalog prd_catalog;

	@Column(name = "price")
	private Integer price;

	@Column(name = "qty")
	private Integer qty;

	@Column(name = "description")
	private String description;

	@Column(name = "status")
	private Integer status;

	@Column(name = "all_stars")
	private Integer all_stars;

	@Column(name = "all_reviews")
	private Integer all_reviews;

	@OneToMany(mappedBy = "product",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Prd_Img> prd_imgs;
	
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Prd_Review> prd_reviews;
	
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Prd_Order_Detail> prd_order_details;

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Prd_Track> prd_track;
	
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Cart> carts;



	

}
