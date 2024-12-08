package com.artogether.venue.vnedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourStrDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate startDate;
    private LocalDate endDate;
    private List<LocalDate> disableDates;
//    private Map<>



}
