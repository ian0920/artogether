package com.artogether.product.prd_coup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewPrdCoupService {

    @Autowired
    private PrdCoupRepository prdCoupRepository;

    // 新增優惠券
    @Transactional
    public PrdCoupDto addPrdCoup(PrdCoup prdCoup) {
        PrdCoup savedPrdCoup = prdCoupRepository.save(prdCoup);
        return toDto(savedPrdCoup);
    }

    // 更新優惠券
    @Transactional
    public PrdCoupDto updatePrdCoup(Integer id, PrdCoup prdCoup) {
        // 從資料庫加載完整的實體
        PrdCoup existingPrdCoup = prdCoupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("找不到 ID 為 " + id + " 的優惠券"));

        // 更新必要字段（根據前端傳遞的數據）
        if (prdCoup.getName() != null) {
            existingPrdCoup.setName(prdCoup.getName());
        }
        if (prdCoup.getStatus() != null) {
            existingPrdCoup.setStatus(prdCoup.getStatus());
        }
        if (prdCoup.getType() != null) {
            existingPrdCoup.setType(prdCoup.getType());
        }
        if (prdCoup.getDeduction() != null) {
            existingPrdCoup.setDeduction(prdCoup.getDeduction());
        }
        if (prdCoup.getRatio() != null) {
            existingPrdCoup.setRatio(prdCoup.getRatio());
        }
        if (prdCoup.getStartDate() != null) {
            existingPrdCoup.setStartDate(prdCoup.getStartDate());
        }
        if (prdCoup.getEndDate() != null) {
            existingPrdCoup.setEndDate(prdCoup.getEndDate());
        }
        if (prdCoup.getDuration() != null) {
            existingPrdCoup.setDuration(prdCoup.getDuration());
        }
        if (prdCoup.getThreshold() != null) {
            existingPrdCoup.setThreshold(prdCoup.getThreshold());
        }

        // 保存更新後的實體
        PrdCoup updatedPrdCoup = prdCoupRepository.save(existingPrdCoup);

        // 返回更新後的 DTO
        return toDto(updatedPrdCoup);
    }


    // 刪除優惠券
    @Transactional
    public void deletePrdCoup(Integer id) {
        prdCoupRepository.deleteById(id);
    }

    // 根據 ID 查詢優惠券
    @Transactional(readOnly = true)
    public PrdCoupDto findCoupById(Integer id) {
        PrdCoup prdCoup = prdCoupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("找不到 ID 為 " + id + " 的優惠券"));
        return toDto(prdCoup);
    }

    // 查詢所有優惠券
    @Transactional(readOnly = true)
    public List<PrdCoupDto> getAllCoupons() {
        return prdCoupRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
   
    @Transactional(readOnly = true)
    public List<PrdCoupDto> findCouponsByCriteria(Integer businessMemberId ,String name, Integer type, Integer status, Integer threshold) {
        return prdCoupRepository
                .findByBusinessMemberIdAndNameContainingAndTypeAndStatusAndThresholdGreaterThanEqual(
                businessMemberId, name, type, status, threshold)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    

    // 將 PrdCoup 實體轉換為 DTO
    private PrdCoupDto toDto(PrdCoup prdCoup) {
        return new PrdCoupDto(
                prdCoup.getId(),
                prdCoup.getBusinessMember() != null ? prdCoup.getBusinessMember().getName() : null,
                prdCoup.getBusinessMember() != null ? prdCoup.getBusinessMember().getId() : null,
                prdCoup.getName(),
                prdCoup.getStatus(),
                prdCoup.getType(),
                prdCoup.getDeduction(),
                prdCoup.getRatio(),
                prdCoup.getStartDate(),
                prdCoup.getEndDate(),
                prdCoup.getDuration(),
                prdCoup.getThreshold()
        );
    }
    
    @Transactional(readOnly = true)
    public List<PrdCoupDto> getCouponsByMemberId(Integer businessMemberId) {
        return prdCoupRepository.findByBusinessMemberId(businessMemberId).stream()
                .map(this::toDto) // 將優惠券實體轉換為 DTO
                .collect(Collectors.toList());
    }
    
}
