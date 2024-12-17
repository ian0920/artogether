package com.artogether.controller.GC;


import com.artogether.controller.GC.model.BusinessDAO;
import com.artogether.controller.GC.model.BusinessDTO;
import com.artogether.product.prd_order.model.PrdOrder;
import com.artogether.product.prd_order_detail.PrdOrderDetail;
import com.artogether.product.product.Product;
import com.artogether.product.product.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BusinessWEIService {

    private final BusinessDAO businessDAO;

    public BusinessWEIService(BusinessDAO businessDAO) {
        this.businessDAO = businessDAO;
    }

    public List<BusinessDTO> getBusinessDTOList() {
        List<BusinessDTO> a = businessDAO.getBusinessDTOList();
        System.out.println(a);
        return a;
    }

    public List<BusinessDTO> getBusinessDTOList(Integer businessId) {
        return businessDAO.getBusinessDTOListByBusinessId(businessId);
    }

    public List<BusinessDTO> getOrderByBusinessId(Integer businessId) {
        // 獲得商家的訂單
        List<BusinessDTO> businessDTOList = getBusinessDTOList(businessId);


        for (BusinessDTO businessDTO : businessDTOList) {
            List<PrdOrder> prdOrderListNew = new ArrayList<>();
            int prdOrderId = businessDTO.getPrdOrderId();
            List<PrdOrder> prdOrderList = businessDAO.getOrderByBusinessId(prdOrderId);
           for (PrdOrder prdOrder : prdOrderList) {

               List<PrdOrderDetail> prdOrderDetailsNew = new ArrayList<>();
               List<PrdOrderDetail> prdOrderDetails = businessDAO.getPrdOrderDetailsByPrdOrderId(prdOrder.getId());
               for (PrdOrderDetail prdOrderDetail : prdOrderDetails) {
                   Product product = prdOrderDetail.getProduct();
                   if (product.getBusinessMember().getId().equals(businessId)) {
                       prdOrderDetailsNew.add(prdOrderDetail);
                   }
               }
               businessDTO.setPrdOrderDetails((prdOrderDetailsNew));
//               businessDTO.setPrdOrder(prdOrder);
//                businessDTO.setProduct(prdOrderDetailsNew.get(0).getProduct());

           }

        }
        return businessDTOList;
    }

}
