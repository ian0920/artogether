package com.artogether.product.prd_img;

import java.util.List;

public interface PrdImgDao {
	
	int add(PrdImg prd_img);
	int update(PrdImg prd_img);
	int delete(Integer Id);
	PrdImg findByPK(Integer Id);
	List<PrdImg> getAll();

}
