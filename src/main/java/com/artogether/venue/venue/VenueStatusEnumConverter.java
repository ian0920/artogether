package com.artogether.venue.venue;

import com.artogether.util.EnumConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class VenueStatusEnumConverter extends EnumConverter<VenueStatusEnum,Integer> {
    public VenueStatusEnumConverter() {
        super(VenueStatusEnum.class);
    }
}
