package com.artogether.event.dto;

import com.artogether.event.evt_order.EvtOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvtOrderDTO {


    private Integer id;
    private Integer eventId;
    private String eventName;
    private String memberName;
    private Integer memberId;
    private Byte status;
    private String orderDate;
    private Integer qty;
    private Integer evtCoupId;
    private String evtCoupName;
    private Integer paid;
    private Integer totalPrice;
    private Integer discount;
    private Integer refund;
    private Timestamp refundApplyDate;
    private Timestamp refundDate;
    private Byte paymentMethod;


    public static EvtOrderDTO EvtOrderDTOTransformer(EvtOrder evtOrder) {

        EvtOrderDTO dto = EvtOrderDTO.builder()
                .id(evtOrder.getId())
                .eventId(evtOrder.getEvent().getId())
                .eventName(evtOrder.getEvent().getName())
                .memberId(evtOrder.getMember().getId())
                .memberName(evtOrder.getMember().getName())
                .status(evtOrder.getStatus())
                .orderDate(timestampToString(evtOrder.getOrderDate()))
                .qty(evtOrder.getQty())
                .evtCoupId(evtOrder.getEvtCoupId())
                .paid(evtOrder.getPaid())
                .totalPrice(evtOrder.getTotalPrice())
                .discount(evtOrder.getDiscount())
                .refund(evtOrder.getRefund())
                .refundApplyDate(evtOrder.getRefundApplyDate())
                .refundDate(evtOrder.getRefundDate())
                .paymentMethod(evtOrder.getPaymentMethod())
                .build();


        return dto;
    }

    private static String timestampToString (Timestamp timestamp) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(timestamp);

    }


}


