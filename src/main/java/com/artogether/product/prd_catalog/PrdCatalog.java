package com.artogether.product.prd_catalog;

import com.artogether.product.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name="prd_catalog" )
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrdCatalog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id",updatable = false)
	private Integer id;
	
	@OneToMany(mappedBy = "prdCatalogs", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("id asc")
	private Set<Product> products;
	
	@Column(name = "name")
	private String name;


}
