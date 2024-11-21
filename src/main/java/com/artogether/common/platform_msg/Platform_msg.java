package com.artogether.common.platform_msg;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.member.Member;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "platform_msg")
public class Platform_msg {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", updatable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "business_id", referencedColumnName = "id")
	private BusinessMember businessMember;
	
	@Column(name = "message")
	private String message;
	
	@Column(name = "msg_time")
	private Timestamp msg_time;
	
	@Column(name = "status")
	private byte status;

}
