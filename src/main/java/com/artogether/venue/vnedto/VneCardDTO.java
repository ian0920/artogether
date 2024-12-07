package com.artogether.venue.vnedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VneCardDTO {
    private Integer vneId;
    private String vneName;
    private String vneAddress;
    //場地描述
    private String description;
    private String vneImgUrl;
}
