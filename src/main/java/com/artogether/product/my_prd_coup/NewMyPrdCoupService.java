package com.artogether.product.my_prd_coup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.artogether.common.member.Member;
import com.artogether.product.my_prd_coup.MyPrdCoup.MyPrdCoupId;
import com.artogether.product.prd_coup.PrdCoup;
import com.artogether.product.prd_coup.PrdCoupDto;
import com.artogether.product.prd_coup.PrdCoupRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewMyPrdCoupService {
	
	
	@Autowired
    private MyPrdCoupRepository myPrdCoupRepository;

	@Autowired
    private PrdCoupRepository prdCoupRepository;


    // 查詢所有的 MyPrdCoup
    public List<MyPrdCoup> findAll() {
        return myPrdCoupRepository.findAll();
    }

    // 根據複合主鍵查詢 MyPrdCoup
    public Optional<MyPrdCoup> findById(MyPrdCoup.MyPrdCoupId id) {
        return myPrdCoupRepository.findById(id);
    }

    // 新增或更新 MyPrdCoup
    public MyPrdCoup save(MyPrdCoup myPrdCoup) {
        return myPrdCoupRepository.save(myPrdCoup);
    }

    // 根據複合主鍵刪除 MyPrdCoup
    public void deleteById(MyPrdCoup.MyPrdCoupId id) {
        myPrdCoupRepository.deleteById(id);
    }

    // 根據實體刪除 MyPrdCoup
    public void delete(MyPrdCoup myPrdCoup) {
        myPrdCoupRepository.delete(myPrdCoup);
    }

    // 自定義邏輯範例：查詢特定會員的所有記錄
    public List<MyPrdCoupDto> findCouponsByMemberId(Integer memberId) {
        return myPrdCoupRepository.findAll()
                .stream()
                .filter(myPrdCoup -> myPrdCoup.getMember().getId().equals(memberId))
                .map(myPrdCoup -> new MyPrdCoupDto(
                        myPrdCoup.getMember().getId(),
                        myPrdCoup.getPrdCoup().getId(),
                        myPrdCoup.getPrdCoup().getName(),
                        myPrdCoup.getStatus(),
                        myPrdCoup.getReceiveDate(),
                        myPrdCoup.getUseDate(),
                        myPrdCoup.getPrdCoup().getEndDate()
                        ))
                        .toList();
        
    }
    	//查看會員尚未領取優惠券
        @Transactional(readOnly = true)
        public List<PrdCoupDto> getAvailableCoupons(Integer memberId) {
            // 1. 查詢所有優惠券
            List<PrdCoupDto> allCoupons = prdCoupRepository.findAll().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

            // 2. 查詢會員已領取的優惠券 ID
            List<MyPrdCoupDto> claimedCoupons = findCouponsByMemberId(memberId);
            
            // 3. 提取會員已領取優惠券的 ID 列表
            List<Integer> claimedCouponIds = claimedCoupons.stream()
                    .map(MyPrdCoupDto::getCouponId)
                    .collect(Collectors.toList());

            // 4. 過濾尚未領取的優惠券
            return allCoupons.stream()
                    .filter(coupon -> !claimedCouponIds.contains(coupon.getId()))
                    .collect(Collectors.toList());
        }
        
        //提供map(this::toDto)使用
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
        
        
     // 使用DTO轉換成實體後的save方法(因controller使用dto取得memberId/prdcoupId)
        public void saveFromDto(MyPrdCoupDto dto) {
            // 將 DTO 轉換為實體對象
            MyPrdCoup myPrdCoup = toEntity(dto);

            // 保存到資料庫
            myPrdCoupRepository.save(myPrdCoup);
        }

        // DTO -> 實體轉換
        private MyPrdCoup toEntity(MyPrdCoupDto dto) {
            MyPrdCoup myPrdCoup = new MyPrdCoup();
            
            Member member = new Member();
            member.setId(dto.getMemberId());           
            myPrdCoup.setMember(member);
            
            PrdCoup prdCoup = new PrdCoup();           
            prdCoup.setId(dto.getCouponId());
            myPrdCoup.setPrdCoup(prdCoup);
            
            myPrdCoup.setStatus(dto.getStatus());
            myPrdCoup.setReceiveDate(dto.getReceiveDate());
            myPrdCoup.setUseDate(dto.getUseDate());
            return myPrdCoup;
        }
               
    
}
