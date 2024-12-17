package com.artogether.controller.GC.model;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.product.prd_order.model.PrdOrder;
import com.artogether.product.prd_order_detail.PrdOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessDAO extends JpaRepository<BusinessMember, Integer> {

    @Query("SELECT new com.artogether.controller.GC.model.BusinessDTO(" +
            "b.id, b.name, c.id) " +
            "FROM BusinessMember b " +
            "JOIN b.products p " +
            "JOIN p.prdOrderDetail pr " +
            "JOIN pr.prdOrder c")
    public List<BusinessDTO> getBusinessDTOList();

    @Query("SELECT new com.artogether.controller.GC.model.BusinessDTO(" +
            "b.id, b.name, c.id) " +
            "FROM BusinessMember b " +
            "JOIN b.products p " +
            "JOIN p.prdOrderDetail pr " +
            "JOIN pr.prdOrder c " +
            "WHERE b.id = ?1"
    )
    public List<BusinessDTO> getBusinessDTOListByBusinessId(Integer businessId);

    @Query("SELECT c " +
            "FROM PrdOrder c " +
            "WHERE c.id = ?1"
    )
    public List<PrdOrder> getOrderByBusinessId(Integer businessId);

    @Query("SELECT prdOrderDetail " +
            "FROM PrdOrderDetail prdOrderDetail " +
            "WHERE prdOrderDetail.prdOrder.id = ?1"
    )
    public List<PrdOrderDetail> getPrdOrderDetailsByPrdOrderId(Integer prdOrderId);
}
