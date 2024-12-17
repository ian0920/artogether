package com.artogether.event.event;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.event.evt_coup.EvtCoup;
import com.artogether.event.evt_img.EvtImg;
import com.artogether.event.evt_order.EvtOrder;
import com.artogether.event.evt_track.EvtTrack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@Builder
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id", referencedColumnName = "id")
    private BusinessMember businessMember;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "delay_date")
    private Timestamp delayDate;

    @Column(name="catalog", nullable = false)
    private String catalog;
    private Integer price;
    private String description;
    //0→下架(預設) 1→上架(報名中) 2→延期 3→取消 4→審核被拒 5->結束報名
    private Byte status;
    private Integer maximum;
    private Integer minimum;
    private Integer enrolled;



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = CascadeType.ALL)
    private Set<EvtTrack> tracks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = CascadeType.ALL)
    private Set<EvtImg> images;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = CascadeType.ALL)

    private Set<EvtCoup> evtCoups;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "event", cascade = CascadeType.ALL)
    private Set<EvtOrder> evtOrders;


    public int hashCode() {
        return Objects.hash(this.id);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            Event that = (Event)obj;
            return Objects.equals(this.id, that.id);
        } else {
            return false;
        }
    }

}
