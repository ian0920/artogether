package com.artogether.venue.tslot;

import com.artogether.venue.venue.Venue;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(exclude="venue")
@ToString(exclude="venue")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tslot")
public class Tslot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vne_id",referencedColumnName = "id")
    private Venue venue;

    @Column(name = "hour_of_mon")
    private String hourOfMon;

    @Column(name = "hour_of_tue")
    private String hourOfTue;

    @Column(name = "hour_of_wed")
    private String hourOfWed;

    @Column(name = "hour_of_thu")
    private String hourOfThu;

    @Column(name = "hour_of_fri")
    private String hourOfFri;

    @Column(name = "hour_of_sat")
    private String hourOfSat;

    @Column(name = "hour_of_sun")
    private String hourOfSun;

    @Column(name = "effective_time")
    private LocalDateTime effectiveTime;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;
}
