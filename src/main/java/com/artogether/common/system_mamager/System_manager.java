package com.artogether.common.system_mamager;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "system_manager")
//@NamedQuery(name = "getALLma", query = "from Emp where id > :id order by id desc")
public class System_manager {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", updatable = false)
	private Integer id;
	
	@Column(name = "account")
	private String account;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "register_date")
	private Timestamp  register_date;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "assign_date")
	private Timestamp  assign_date;

	@Column(name = "status")
	private byte status;
	
	public System_manager() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getRegister_date() {
		return register_date;
	}

	public void setRegister_date(Timestamp register_date) {
		this.register_date = register_date;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Timestamp getAssign_date() {
		return assign_date;
	}

	public void setAssign_date(Timestamp assign_date) {
		this.assign_date = assign_date;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
	
//	@Override
//	public String toString() {
//	    return "Emp [id=" + id + ", ename=" + ename + ", job=" + job + ", hiredate=" + hiredate + ", sal=" + sal
//	            + ", comm=" + comm + ", empdeptno=" + empdeptno + "]";
//	}

}
