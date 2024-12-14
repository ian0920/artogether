package com.artogether.product.my_prd_coup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MyPrdCoupRepository extends JpaRepository<MyPrdCoup, MyPrdCoup.MyPrdCoupId> {
    List<MyPrdCoup> findAllByMember_Id(Integer memberId);

}
