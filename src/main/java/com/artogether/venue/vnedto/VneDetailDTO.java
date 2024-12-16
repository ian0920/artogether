package com.artogether.venue.vnedto;

import com.artogether.venue.venue.VenueStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VneDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer vneId;
    private String vneName;
    private String bizName;
    private String vneAddress;
    //場地類別描述
    private String type;
    //場地描述
    private String description;
    private String status;
    //可容許預約天數
    private Integer availableDays;
    private List<String> imgUrls;
    private TslotDTO tslot;
    private VnePriceDTO price;
    private Integer allStars;
    private Integer allReviews;
}
