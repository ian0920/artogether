package com.artogether.event.evt_img;

import com.artogether.event.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "evt_img")
public class EvtImgVO {

	@Id
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "evt_id", referencedColumnName = "id")
	private Event event;
	
	@Column(name = "image_file")
	private byte[] imageFile;
	
}
