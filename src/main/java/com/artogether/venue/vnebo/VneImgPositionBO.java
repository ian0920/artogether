package com.artogether.venue.vnebo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VneImgPositionBO {

    private Integer vneId;
    //Default可能會有Annotation
    //不建議這樣寫，會有資安問題
    private String vneURL1 = "static/web_design/asset/images/placeholder.jpg";
    private String vneURL2 = "static/web_design/asset/images/placeholder.jpg";
    private String vneURL3 = "static/web_design/asset/images/placeholder.jpg";
}
