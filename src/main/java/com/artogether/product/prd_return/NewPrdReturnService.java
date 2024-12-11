package com.artogether.product.prd_return;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewPrdReturnService {

    @Autowired
    private PrdReturnRepository prdReturnRepository;

    // 新增退換貨
    @Transactional
    public PrdReturnDto addPrdReturn(PrdReturn prdReturn) {
        PrdReturn savedPrdReturn = prdReturnRepository.save(prdReturn);
        return toDto(savedPrdReturn);
    }

    // 更新退換貨
    @Transactional
    public PrdReturnDto updatePrdReturn(Integer id, PrdReturn prdReturn) {
        PrdReturn existingPrdReturn = prdReturnRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("找不到 ID 為 " + id + " 的退換貨記錄"));

        // 更新必要字段
        
        
        if (prdReturn.getStatus() != null) {
            existingPrdReturn.setStatus(prdReturn.getStatus());
        }    
      
        if (prdReturn.getReturnDate() != null) {
            existingPrdReturn.setReturnDate(prdReturn.getReturnDate());
        }
        if (prdReturn.getRefund() != null) {
            existingPrdReturn.setRefund(prdReturn.getRefund());
        }

        // 保存更新後的實體
        PrdReturn updatedPrdReturn = prdReturnRepository.save(existingPrdReturn);

        return toDto(updatedPrdReturn);
    }

    // 刪除退換貨
    @Transactional
    public void deletePrdReturn(Integer id) {
        prdReturnRepository.deleteById(id);
    }

    // 根據 ID 查詢退換貨
    @Transactional(readOnly = true)
    public PrdReturnDto findPrdReturnById(Integer id) {
        PrdReturn prdReturn = prdReturnRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("找不到 ID 為 " + id + " 的退換貨記錄"));
        
        return toDto(prdReturn);
        
    }

    // 查詢所有退換貨
    @Transactional(readOnly = true)
    public List<PrdReturnDto> getAllReturns() {
        return prdReturnRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 根據條件查詢退換貨
    @Transactional(readOnly = true)
    public List< PrdReturnDto> findCouponsByCriteria(Integer id,Integer orderId, String reason, Integer type, Integer status) {
        return prdReturnRepository
                .findByIdAndPrdOrder_IdAndReasonAndTypeAndStatusGreaterThanEqual(
                		id, orderId, reason, type, status)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 將 PrdReturnDto 實體轉換為 DTO
    private PrdReturnDto toDto(PrdReturn prdReturn) {
        return new PrdReturnDto(
        		prdReturn.getId(),
        		prdReturn.getPrdOrder() != null ? prdReturn.getPrdOrder().getId() : null,
        		prdReturn.getPrdName(),
        		prdReturn.getReason(),
        		prdReturn.getStatus(),
        		prdReturn.getType(),
        		prdReturn.getApplyDate(),
        		prdReturn.getReturnDate(),
        		prdReturn.getRefund()
        		
        );
    }
    
    @Transactional(readOnly = true)
    public List<PrdReturnDto> getReturnByPrdOrder_Id(Integer orderId) {
        return prdReturnRepository.findByPrdOrder_Id(orderId).stream()
                .map(this::toDto) 
                .collect(Collectors.toList());
    }

    
}
