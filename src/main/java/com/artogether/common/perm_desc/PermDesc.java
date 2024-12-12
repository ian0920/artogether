package com.artogether.common.perm_desc;

import com.artogether.common.permission.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "perm_desc")
@Data //Getter and Setter Hashcode toString equals
@AllArgsConstructor // 全參數建構子
@NoArgsConstructor // 無參數建構子
public class PermDesc {
	//平台功能說明
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "description")
	private String description;

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "permDesc", cascade = CascadeType.ALL)
//	private Set<Permission> permission;
	
}
