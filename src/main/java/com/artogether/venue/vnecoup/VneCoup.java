package com.artogether.venue.vnecoup;

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
@Table(name = "vne_coup")
public class VneCoup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vne_id",referencedColumnName = "id")
    private Venue venue;

    @Column(name = "vne_coup_name", length = 50)
    private String couponName;

    private Byte status = 0; // Default to 0 (下架)

    private Byte type; // 0 → 比例折扣, 1 → 金額折扣

    private Integer threshold;

    private Integer deduction;

    private Double ratio;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "duration")
    private Integer duration; // 單位：天數

    @Column(name = "hour_of_day", length = 24)
    private String hourOfDay;

    @Column(name = "day_of_week", length = 7)
    private String dayOfWeek;
}
