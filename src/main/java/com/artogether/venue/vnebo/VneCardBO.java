package com.artogether.venue.vnebo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VneCardBO {
    private Integer vneId;
    private String name;
    private String vneAddress;
    //場地描述
    private String description;
    private String vneImgUrl;
}
