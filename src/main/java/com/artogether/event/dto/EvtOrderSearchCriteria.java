package com.artogether.event.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvtOrderSearchCriteria {

    private Integer eventId = -1;
    private Integer status = -1;
    private String startDate;
    private String endDate;

}
