package com.artogether.product.prd_img;

import java.util.List;

public interface PrdImgDao {
	
	int add(PrdImg prdImg);
	int update(PrdImg prdImg);
	int delete(Integer Id);
	PrdImg findByPK(Integer Id);
	List<PrdImg> getAll();
	void saveAll(List<PrdImg> images);

}
