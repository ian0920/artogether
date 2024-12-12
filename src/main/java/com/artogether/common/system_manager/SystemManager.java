package com.artogether.common.system_manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "system_manager")
@Data //Getter and Setter Hashcode toString equals
@AllArgsConstructor // 全參數建構子
@NoArgsConstructor // 無參數建構子
//@NamedQuery(name = "getALLma", query = "from Emp where id > :id order by id desc")
public class SystemManager {
	//平台管理員
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", updatable = false)
	private Integer id;
	
	@Column(name = "account")
	private String account;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "register_date")
	@CreationTimestamp // Bean
	private Timestamp  register_date;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "assign_date")
	@CreationTimestamp
	private Timestamp  assign_date;

	@Column(name = "status")
	private byte status;

}
