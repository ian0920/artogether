package com.artogether.venue.vnedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<List<Integer>> availableSegments;
    private Map<Integer, Integer> hourlyPrice;
}
