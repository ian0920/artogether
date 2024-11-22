package com.artogether.venue.vne_track;

import com.artogether.common.member.Member;
import com.artogether.venue.venue.Venue;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "Vne_track")
@IdClass(Vne_track.Vne_trackCom.class)
public class Vne_track {

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vne_id", referencedColumnName = "id")
	private Venue venue;

	private Date track_date;

	public Vne_track() {
		super();
	}

	public Vne_track(Date track_date, Venue venue, Member member) {
		this.track_date = track_date;
		this.venue = venue;
		this.member = member;
	}


	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public Date getTrack_date() {
		return track_date;
	}

	public void setTrack_date(Date track_date) {
		this.track_date = track_date;
	}


	/***************************************
	  以下複合主鍵setter getter請在跟老師確認
	***************************************/


	public void setVne_trackCom(Vne_trackCom vne_trackCom){
		this.venue.setId(vne_trackCom.getVne_id());
		this.member.setId(vne_trackCom.getMember_id());
	}

	public Vne_trackCom getVne_trackCom(){
		return new Vne_trackCom(this.venue.getId(), this.member.getId());
	}

	static class Vne_trackCom implements Serializable {

		private int venue;
		private int member;

		public Vne_trackCom() {
			super();
		}

		public Vne_trackCom(int vne_id, int member_id) {
			super();
			this.venue = vne_id;
			this.member = member_id;
		}

		public int getVne_id() {
			return venue;
		}

		public void setVne_id(int vne_id) {
			this.venue = vne_id;
		}

		public int getMember_id() {
			return member;
		}

		public void setMember_id(int member_id) {
			this.member = member_id;
		}

		@Override
		public int hashCode() {
			return Objects.hash(member, venue);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Vne_trackCom other = (Vne_trackCom) obj;
			return member == other.member && venue == other.venue;
		}

	}

}