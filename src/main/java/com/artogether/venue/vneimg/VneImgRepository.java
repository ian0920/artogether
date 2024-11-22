package com.artogether.venue.vneimg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VneImgRepository extends JpaRepository<VneImg, Integer> {
    /* 以下方法缺少Query查詢語句，請補上後再解開註解 */
//    List<VneImg> findAllByVneId(Venue vneId);
//    VneImg findByVneId(Venue vneId);

}
