package com.artogether.product.my_prd_coup;

import java.sql.Timestamp;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPrdCoupDto {
	  	private Integer memberId;
	    private Integer couponId;
	    private String couponName;
	    private Integer status;
	    private Timestamp receiveDate;
	    private Timestamp useDate;
	    private Timestamp endDate;

}
