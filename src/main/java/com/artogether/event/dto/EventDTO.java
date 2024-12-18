package com.artogether.event.dto;

import com.artogether.event.event.Event;
import com.artogether.event.evt_img.EvtImg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDTO {


    private Integer id;
    private Integer businessId;
    private String name;
    private String location;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp delayDate;
    private String catalog;
    private Integer price;
    private String description;
    //0→下架(預設) 1→上架(報名中) 2→延期 3→取消 4→審核被拒 5->結束報名
    private Byte status;
    private Integer maximum;
    private Integer minimum;
    private Integer enrolled;
    private Integer imgId;
    private byte[] imageFile;



    public static EventDTO eventToDTO(Event e, EvtImg img){

        EventDTO eventDTO =
                EventDTO.builder()
                .imgId(img.getId())
                .imageFile(img.getImageFile())
                .id(e.getId())
                .businessId(e.getBusinessMember().getId())
                .name(e.getName())
                .location(e.getLocation())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .delayDate(e.getDelayDate())
                .catalog(e.getCatalog())
                .price(e.getPrice())
                .description(e.getDescription())
                .status(e.getStatus())
                .maximum(e.getMaximum())
                .minimum(e.getMinimum())
                .enrolled(e.getEnrolled())
                .build();



        return eventDTO;

    }
}
