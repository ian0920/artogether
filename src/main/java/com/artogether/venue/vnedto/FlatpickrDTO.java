package com.artogether.venue.vnedto;

import com.artogether.venue.tslot.TslotService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlatpickrDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate startDate;
    private LocalDate endDate;
    private List<LocalDate> disableDates;

}
