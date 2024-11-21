package com.artogether.product.prd_img;

import java.util.List;

public interface Prd_ImgDao {
	
	int add(Prd_Img prd_img);
	int update(Prd_Img prd_img);
	int delete(Integer Id);
	Prd_Img findByPK(Integer Id);
	List<Prd_Img> getAll();

}
