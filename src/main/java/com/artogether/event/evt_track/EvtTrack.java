package com.artogether.event.evt_track;

import com.artogether.common.member.Member;
import com.artogether.event.event.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="evt_track")
public class EvtTrack {

	@EmbeddedId
	private EvtTrackPK evtTrackPK;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "evt_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Event event;

	@Column(name="track_date")
	private Timestamp trackDate;
}
