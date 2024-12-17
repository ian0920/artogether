package com.artogether.common.message;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.artogether.common.chatroom.Chatroom;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "message")
public class Message {
	public static Boolean isBmemb = true;
	public static Boolean isMemb = false;
	
	@Id
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", referencedColumnName = "id")
    @JsonBackReference
	private Chatroom chatroom ;
	
	// true->商家發送；false->會員發送
	private Boolean sender;
	
	private String content;
	
	// 因為會先存到redis，所以並沒設置insertable=false
	@Column(name="send_time",updatable = false)
	private Timestamp sendTime;

}
