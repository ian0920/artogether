package com.artogether.common.perm_desc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "perm_desc")
@Data //Getter and Setter Hashcode toString equals
@AllArgsConstructor // 全參數建構子
@NoArgsConstructor // 無參數建構子
public class PermDesc {
	//平台功能說明
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", updatable = false)
	private Integer id;
	
	@Column(name = "description")
	private String description;
	
}
