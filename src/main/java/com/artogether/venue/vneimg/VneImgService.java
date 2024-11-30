package com.artogether.venue.vneimg;

import com.artogether.venue.venue.Venue;
import com.artogether.venue.vnedto.VneImgDTO;
import com.artogether.venue.vnedto.VneImgPositionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class VneImgService {

    @Autowired
    private VneImgRepository vneImgRepository;
    @Resource
    RedisTemplate<String, byte[]> redisTemplate;
    @Autowired
    private VneImgUrlRepository vneImgUrlRepository;

    //使用url讓<img src>需要再請求
    public VneImgPositionDTO getAllImg(Integer vneId) {
        List<String> imageUrls = vneImgUrlRepository.findImageUrlsByVneId(vneId);

        // 檢查返回的圖片列表是否為 null 或空集合
        if (imageUrls != null && !imageUrls.isEmpty()) {
            // 建立 VneImgPositionDTO 物件，並根據可用的圖片URL數量進行設置
            VneImgPositionDTO.VneImgPositionDTOBuilder builder
                    = VneImgPositionDTO.builder().vneId(vneId)
                                                 .vneURL1(imageUrls.get(0));
            if (imageUrls.size() > 1) {builder.vneURL2(imageUrls.get(1));}
            if (imageUrls.size() > 2) {builder.vneURL3(imageUrls.get(2));}
            return builder.build();
        }

        // 如果 imageUrls 為 null 或空集合，返回一個包含預設值的 DTO
        return VneImgPositionDTO.builder().vneId(vneId).build();
    }

    public void updateVneImg(VneImgDTO vneImgDTO){
        int vneId = vneImgDTO.getVneId();
        int position = vneImgDTO.getPosition();
        byte[] imageBytes = vneImgDTO.getImageFile();

        // 檔案儲存路徑
        String uploadDir = "public/uploads/venue/";//Dir=Directory（目錄）
        String fileName = getFileName(vneId, position);
        String filePath = uploadDir + fileName;

        // 儲存圖片到指定路徑
        try {
            //教學中用的是File file = new File("static/uploads/venue/");
            //Path"s"是處理Path的工具類，可以比較有效的去處理分隔路徑:"\"or"/"
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path); // 若資料夾不存在則建立
            }
            //Files.write
            //如果目標路徑的檔案已經存在，新的內容會直接覆蓋舊的檔案。
            //如果檔案不存在，則會自動建立新的檔案。
            Files.write(Paths.get(filePath), imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
        }
        if (!vneImgUrlRepository.existsByVenueIdAndPosition(vneId, position)) {
            // 新增新的圖片 URL
            VneImgUrl vneImgUrl = VneImgUrl.builder()
                                  .venue(Venue.id(vneId))
                                  .imageFileUrl(filePath)
                                  .position(position)
                                  .build();
            vneImgUrlRepository.save(vneImgUrl);
        }
    }

    public String getFileName(Integer vneId, Integer position){
        return "venue_" + vneId + "_position_" + position + ".jpg"; // 自定義檔案名稱，我想確保不需要多存
    }
    public String getAssetPath(Integer vneId, Integer position){
        return "/uploads/venue/"+getFileName(vneId, position);
    }
}
