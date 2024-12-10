package com.artogether.event.dto;

import com.artogether.event.evt_coup.EvtCoup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvtCoupDTO {

    private Integer id;
    private Integer eventId;
    private String eventName;
    private String coupName;
    private Date startDate;
    private Date endDate;
    private String type;
    private Double discount;
    private Integer duration;
    private Integer threshold;
    private Byte status;


    public static EvtCoupDTO transformFromEvtCoup(EvtCoup evtCoup) {
        EvtCoupDTO dto = EvtCoupDTO.builder()
                .id(evtCoup.getId())
                .eventId(evtCoup.getEvent().getId())
                .eventName(evtCoup.getEvent().getName())
                .coupName(evtCoup.getEvtCoupName())
                .type(evtCoup.getType() == 0 ? "比例" : "金額")
                .duration(evtCoup.getDuration())
                .discount(evtCoup.getType() == 0 ? evtCoup.getRatio() : evtCoup.getDeduction())
                .startDate(new Date(evtCoup.getStartDate().getTime()))
                .endDate(new Date(evtCoup.getEndDate().getTime()))
                .status(evtCoup.getStatus())
                .threshold(evtCoup.getThreshold()).build();

        return dto;
    }

}
