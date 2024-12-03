package com.artogether.venue.vnedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VneImgPositionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer vneId;
    //"static/web_design/asset/images/placeholder.jpg"
    private String vneURL1 = "public/venue/images/0_0.jpg";
    private String vneURL2 = "public/venue/images/0_0.jpg";
    private String vneURL3 = "public/venue/images/0_0.jpg";
}
