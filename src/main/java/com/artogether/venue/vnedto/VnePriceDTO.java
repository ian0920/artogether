package com.artogether.venue.vnedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VnePriceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer vneId;
    private String vneName;
    private Integer defaultPrice;
    // 特定時段的價格
    private Integer price;
    // 開始時間與結束時間
    private List<Integer> priceTslot;
    // 星期設定 (例如：'0111110' 表示哪些日子適用此價格)
    private List<Integer> dayOfWeek;

    private LocalDateTime effectiveTime;
    private LocalDateTime expirationTime;
}
