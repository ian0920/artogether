package com.artogether.venue.vnedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VneImgBytesDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer vneId;
    private byte[] imageFile;
    private Integer position;


}
