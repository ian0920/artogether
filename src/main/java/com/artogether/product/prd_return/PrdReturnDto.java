package com.artogether.product.prd_return;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrdReturnDto {
	
	 	private Integer id;  	    
	    private Integer orderId;
	    private String prdName;  
	    private String reason;              
	    private Integer status;            
	    private Integer type;                            
		private Timestamp applyDate;	
		private Timestamp returnDate;
		private Integer refund;
		
	

	
	

	     
	


}
