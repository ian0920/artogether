package com.artogether.venue.venue;


import com.artogether.common.business_member.BusinessMember;
import com.artogether.venue.tslot.Tslot;
import com.artogether.venue.vne_track.Vne_track;
import com.artogether.venue.vnecoup.VneCoup;
import com.artogether.venue.vneimg.VneImg;
import com.artogether.venue.vneorder.VneOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venue", cascade = CascadeType.ALL)
    private List<Tslot> tslots;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venue", cascade = CascadeType.ALL)
    private List<VneCoup> vnecoups;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venue", cascade = CascadeType.ALL)
    private List<VneImg> vneImgs;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venue", cascade = CascadeType.ALL)
    private List<VneOrder> vneOrders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venue", cascade = CascadeType.ALL)
    private List<Vne_track> vneTracks;


    @NotEmpty(message="場地名稱: 請勿空白")
    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "場地名稱: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id",referencedColumnName = "id")
    private BusinessMember businessMember;


    private String type;
    private String description;
    private VenueStatusEnum status;
    private Integer availableDays;
    private Integer allStars;
    private Integer allReviews;


}
