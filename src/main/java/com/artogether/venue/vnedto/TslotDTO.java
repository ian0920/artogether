package com.artogether.venue.vnedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TslotDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer vneId;
    private String vneName;
    private List<Integer> hourOfMon;
    private List<Integer> hourOfTue;
    private List<Integer> hourOfWed;
    private List<Integer> hourOfThu;
    private List<Integer> hourOfFri;
    private List<Integer> hourOfSat;
    private List<Integer> hourOfSun;


}
