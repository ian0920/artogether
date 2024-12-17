package com.artogether.product.prd_track;


import com.artogether.common.member.Member;
import com.artogether.product.cart.model.Cart;
import com.artogether.product.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PrdTrackService {

    private final PrdTrackRepository prdTrackRepository;

    @Autowired
    public PrdTrackService(PrdTrackRepository prdTrackRepository){
        this.prdTrackRepository = prdTrackRepository;
    }

//    public PrdTrack addPrdTrack(PrdTrack prdTrack){
//        if (prdTrackRepository.existsById(prdTrack.getPrdTrackId())) {
//            throw new IllegalArgumentException("該追蹤記錄已存在！");
//        }
//        return prdTrackRepository.save(prdTrack);
//    }

    // 添加追蹤記錄並檢查是否重複
    @Transactional
    public PrdTrack addToTrack(Product product, Member member){
        if (member == null || product == null ) {
            throw new IllegalArgumentException("Invalid input for adding product to cart");
        }
        PrdTrack existingTrack = prdTrackRepository.findByProductAndMember(product, member);
        if(existingTrack != null){
            System.out.println("已追蹤");
        }
        PrdTrack newTrack = new PrdTrack();
        newTrack.setProduct(product);
        newTrack.setMember(member);
        return prdTrackRepository.save(newTrack);
    }

    // 從追蹤移除
    @Transactional
    public void removeProductFromTrack(Member member, Product product){
        PrdTrack existingTrack = prdTrackRepository.findByProductAndMember(product, member);
        if(existingTrack != null){
            prdTrackRepository.delete(existingTrack);
        }
    }



        // 獲取會員的所有追蹤商品
    public List<PrdTrack> getPrdTrackByMemberId(Integer memberId){
        return prdTrackRepository.findByMemberId(memberId);
    }


    // 獲取會員追蹤的所有上架商品
    public List<PrdTrack> getAvailablePrdByMemberId(Integer memberID){
        return prdTrackRepository.findAvailablePrdByMemberId(memberID);
    }

    // 獲取會員追蹤的所有未上架商品
    public List<PrdTrack> getUnavailablePrdByMemberId(Integer memberID){
        return prdTrackRepository.findUnavailablePrdByMemberId(memberID);
    }

    // 刪除單個追蹤記錄
    public void removeTrackPrd(PrdTrack.PrdTrackId prdTrackId){
        prdTrackRepository.deleteById(prdTrackId);
    }

    // 刪除所有追蹤記錄
    public void removeAllTrack(Integer memberId){
        List<PrdTrack> prdTrackList = prdTrackRepository.findByMemberId(memberId);
        if (prdTrackList == null || prdTrackList.isEmpty()) {
            throw new IllegalArgumentException("該會員無任何追蹤記錄！");
        }
        prdTrackRepository.deleteAll(prdTrackList);
    }


}
