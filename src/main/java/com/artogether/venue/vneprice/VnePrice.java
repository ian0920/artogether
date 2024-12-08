package com.artogether.venue.vneprice;

import com.artogether.venue.venue.Venue;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(exclude="venue")
@ToString(exclude="venue")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vne_price")
public class VnePrice implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vne_id",referencedColumnName = "id")
    private Venue venue;

    // 預設價格
    @NotNull
    @Min(0)
    private Integer defaultPrice;

    // 開始時間與結束時間
    @Column(name = "price_tslot")
    private String priceTslot;
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;

    // 星期設定 (例如：'0111110' 表示哪些日子適用此價格)
    private String dayOfWeek;

    // 特定時段的價格
    private Integer price;

    @Column(name = "effective_time")
    private LocalDateTime effectiveTime;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;
}
