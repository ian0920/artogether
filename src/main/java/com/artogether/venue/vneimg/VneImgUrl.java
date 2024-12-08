package com.artogether.venue.vneimg;

import com.artogether.venue.venue.Venue;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode(exclude="venue")
@ToString(exclude="venue")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vne_img")
public class VneImgUrl implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vne_id",referencedColumnName = "id")
    private Venue venue;

    @NotEmpty
    @Column(name = "image_url")
    private String imageUrl;
    //path
    private Integer position;

}
