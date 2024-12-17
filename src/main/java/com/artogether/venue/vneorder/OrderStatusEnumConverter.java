package com.artogether.venue.vneorder;

import com.artogether.util.EnumConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusEnumConverter extends EnumConverter<OrderStatusEnum, Integer> {
    public OrderStatusEnumConverter() {
        super(OrderStatusEnum.class);
    }
}
