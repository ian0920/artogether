package com.artogether.product.prd_catalog;

import com.artogether.product.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name="prd_catalog" )
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PrdCatalog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id",updatable = false)
	private Integer id;

	@Column(name = "name")
	private String name;



}
