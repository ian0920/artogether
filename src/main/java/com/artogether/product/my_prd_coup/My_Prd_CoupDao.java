package com.artogether.product.my_prd_coup;

import java.util.List;



public interface My_Prd_CoupDao {
	
	My_Prd_Coup.CompositeCoup add(My_Prd_Coup my_prd_coup);
	My_Prd_Coup.CompositeCoup update(My_Prd_Coup my_prd_coup);
	boolean delete(My_Prd_Coup.CompositeCoup member_prd_coup_id);
	My_Prd_Coup findByPK(My_Prd_Coup.CompositeCoup member_prd_coup_id);
	List<My_Prd_Coup> getAll();

}
