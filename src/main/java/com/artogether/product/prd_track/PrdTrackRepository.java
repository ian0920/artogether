package com.artogether.product.prd_track;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PrdTrackRepository extends JpaRepository<PrdTrack, PrdTrack.PrdTrackId> {

    List<PrdTrack> findByMemberId(Integer memberId);

    // 根據會員ID查詢其追蹤的所有上架商品
    @Query("SELECT pt FROM PrdTrack pt WHERE pt.member.id = :memberId AND pt.product.status = 1")
    List<PrdTrack> findAvailablePrdByMemberId(Integer memberId);

    // 根據會員ID查詢其追蹤的所有未上架商品
    @Query("SELECT pt FROM PrdTrack pt WHERE pt.member.id = :memberId AND pt.product.status = 0")
    List<PrdTrack> findUnavailablePrdByMemberId(Integer memberId);
}
