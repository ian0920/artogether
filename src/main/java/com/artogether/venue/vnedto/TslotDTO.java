package com.artogether.venue.vnedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TslotDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer vneId;
    private String hourOfMon;
    private String hourOfTue;
    private String hourOfWed;
    private String hourOfThu;
    private String hourOfFri;
    private String hourOfSat;
    private String hourOfSun;


}
