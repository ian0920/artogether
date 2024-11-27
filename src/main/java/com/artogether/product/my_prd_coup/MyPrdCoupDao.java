package com.artogether.product.my_prd_coup;

import java.util.List;



public interface MyPrdCoupDao {
	
	MyPrdCoup.MyPrdCoupId add(MyPrdCoup myPrdCoup);
	MyPrdCoup.MyPrdCoupId update(MyPrdCoup myPrdCoup);
	boolean delete(MyPrdCoup.MyPrdCoupId myPrdCoupId);
	MyPrdCoup findByPK(MyPrdCoup.MyPrdCoupId myPrdCoupId);
	List<MyPrdCoup> getAll();

}
