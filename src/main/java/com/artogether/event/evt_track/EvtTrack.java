package com.artogether.event.evt_track;

import com.artogether.common.member.Member;
import com.artogether.event.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="evt_track")
public class EvtTrack {

	@Id
	@Column(name="id")
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "evt_id", referencedColumnName = "id")
	private Event event;

	@Column(name="track_date")
	private Date trackDate;
}
