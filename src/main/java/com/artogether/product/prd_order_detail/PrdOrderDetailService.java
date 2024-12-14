package com.artogether.product.prd_order_detail;

import com.artogether.product.prd_order.model.PrdOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrdOrderDetailService {

    private final PrdOrderDetailRepository prdOrderDetailRepository;

    public PrdOrderDetailService(PrdOrderDetailRepository prdOrderDetailRepository) {
        this.prdOrderDetailRepository = prdOrderDetailRepository;
    }


    public List<PrdOrderDetail> findByPrdOrder(PrdOrder prdOrder) {
        return prdOrderDetailRepository.findByPrdOrder(prdOrder);
    }
}
