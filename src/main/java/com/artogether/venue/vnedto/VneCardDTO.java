package com.artogether.venue.vnedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VneCardDTO {
    private Integer vneId;
    private String vneName;
    //場地類別描述
    private String type;
    private String vneAddress;
    //場地描述
    private String description;
    private Integer availableDays;
    private List<String> imgUrls;
    private Integer startHour;
    private Integer endHour;
}
