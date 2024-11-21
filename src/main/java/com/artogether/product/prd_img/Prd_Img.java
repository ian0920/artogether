package com.artogether.product.prd_img;


import com.artogether.product.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "prd_img")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prd_Img {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Integer id;


	@Column(name = "image_file")
	private byte[] image_file;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "prd_id", referencedColumnName = "id")
	private Product product;

	
}

