package com.artogether.event.evt_coup;

import com.artogether.event.event.Event;
import com.artogether.event.my_evt_coup.MyEvtCoup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evt_coup")
public class EvtCoup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "evt_id", referencedColumnName = "id")
    private Event event;

    @Column(name = "evt_coup_name", nullable = false)
    private String evtCoupName;

    //0→下架 1→上架
    private Byte status;
    //0→比例折扣 1→金額折扣
    private Byte type;
    private Integer deduction;
    private Double ratio;

    @Column(name = "start_date")
    private java.sql.Timestamp startDate;

    @Column(name = "end_date")
    private java.sql.Timestamp endDate;

    private Integer duration;
    private Integer threshold;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "evtCoup", cascade = CascadeType.ALL)
    private Set<MyEvtCoup> myEvtCoups;

    public int hashCode() {
        return Objects.hash(this.id);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            EvtCoup that = (EvtCoup)obj;
            return Objects.equals(this.id, that.id);
        } else {
            return false;
        }
    }

}
