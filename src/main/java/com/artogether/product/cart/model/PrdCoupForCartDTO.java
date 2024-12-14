package com.artogether.product.cart.model;

import com.artogether.product.my_prd_coup.MyPrdCoup;
import com.artogether.product.prd_coup.PrdCoup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrdCoupForCartDTO {

    private Integer prdCoupId;
    private Integer myPrdCoupStatus;
    private String prdCoupName;
    private Integer status;
    private Integer type;
    private Integer deduction;
    private Double ration;
    private Timestamp startDate;
    private Timestamp endDate;
    private Integer duration;
    private Integer threshold;


    public static PrdCoupForCartDTO prdCoupForCartDTOTransformer (PrdCoup prdCoup, MyPrdCoup myPrdCoup) {



        PrdCoupForCartDTO.PrdCoupForCartDTOBuilder builder = PrdCoupForCartDTO.builder();

        builder
                .prdCoupId(prdCoup.getId())
                .myPrdCoupStatus(myPrdCoup.getStatus())
                .prdCoupName(prdCoup.getName())
                .status(prdCoup.getStatus())
                .type(prdCoup.getType())
                .deduction(prdCoup.getDeduction());

                if (prdCoup.getRatio() != null)
                    builder.ration(prdCoup.getRatio().doubleValue());

                builder
                .startDate(prdCoup.getStartDate())
                .endDate(prdCoup.getEndDate())
                .duration(prdCoup.getDuration())
                .threshold(prdCoup.getThreshold());


        return builder.build();
    }


}

