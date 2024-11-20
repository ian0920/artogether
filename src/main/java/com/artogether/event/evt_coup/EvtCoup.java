package com.artogether.event.evt_coup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evt_coup")
public class EvtCoup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "evt_id")
    private Integer evtId;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "evt_id", referencedColumnName = "id")
//    private Event event;

    @Column(name = "evt_coup_name", nullable = false)
    private String evtCoupName;

    private Byte status;
    private Byte type;
    private Integer deduction;
    private Double ratio;

    @Column(name = "start_date")
    private java.sql.Timestamp startDate;

    @Column(name = "end_date")
    private java.sql.Timestamp endDate;

    private Integer duration;
    private Integer threshold;
}
