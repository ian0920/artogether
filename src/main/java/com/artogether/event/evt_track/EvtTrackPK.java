package com.artogether.event.evt_track;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.artogether.common.member.Member;
import com.artogether.event.event.Event;

import lombok.*;

@Builder 
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Embeddable 
@EqualsAndHashCode
public class EvtTrackPK implements  Serializable  {
	
	@Column(name = "member_id")
    private Integer memberId;

    @Column(name = "evt_id")
    private Integer evtId;
}
