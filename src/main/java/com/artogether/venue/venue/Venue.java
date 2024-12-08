package com.artogether.venue.venue;


import com.artogether.common.business_member.BusinessMember;
import com.artogether.venue.tslot.Tslot;
import com.artogether.venue.vne_track.VneTrack;
import com.artogether.venue.vnecoup.VneCoup;
import com.artogether.venue.vneimg.VneImgUrl;
import com.artogether.venue.vneorder.VneOrder;
import com.artogether.venue.vneprice.VnePrice;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = {"tslots", "vnecoups", "vneImgs", "vneOrders", "vneTracks", "vnePrice", "businessMember"}) // 排除雙向引用的字段
@ToString(exclude = {"tslots", "vnecoups", "vneImgs", "vneOrders", "vneTracks", "vnePrice", "businessMember"}) // 同樣排除以避免遞迴
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
    private List<VneImgUrl> vneImgs;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venue", cascade = CascadeType.ALL)
    private List<VneOrder> vneOrders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venue", cascade = CascadeType.ALL)
    private List<VneTrack> vneTracks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venue", cascade = CascadeType.ALL)
    private List<VnePrice> vnePrice;

    @NotEmpty(message="場地名稱: 請勿空白")
    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "場地名稱: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id",referencedColumnName = "id")
    private BusinessMember businessMember;

    //場地類別描述
    private String type;
    //場地描述
    private String description;
    //場地狀態(上架/下架/停權)
    private VenueStatusEnum status;

    //可容許預約天數
    @Column(name = "available_days", nullable = false)
    private Integer availableDays;

    @Column(name = "all_stars")
    private Integer allStars;

    @Column(name = "all_reviews")
    private Integer allReviews;

    //設為靜態方法，只處理id的附值(單一職責)
    //不要有過多創建邏輯
    public static Venue id(Integer id) {
        Venue venue = new Venue();
        venue.setId(id);
        return venue;
    }
}
