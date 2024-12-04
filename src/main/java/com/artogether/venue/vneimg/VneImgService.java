package com.artogether.venue.vneimg;

import com.artogether.venue.venue.Venue;
import com.artogether.venue.vnedto.VneImgBytesDTO;
import com.artogether.venue.vnedto.VneImgPositionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class VneImgService {

    @Resource
    RedisTemplate<String, byte[]> redisTemplate;
    @Autowired
    private VneImgUrlRepository vneImgUrlRepository;

    //使用url讓<img src>需要再請求
    public List<String> getAllImgs(Integer vneId) {
        List<VneImgUrl> imageData = vneImgUrlRepository.findImageUrlsByVneId(vneId);
        List<String> imageUrls = new ArrayList<>();
        // 檢查返回的圖片列表是否為 null 或空集合
        if (imageData != null && !imageData.isEmpty()) {
            for (VneImgUrl vneImg : imageData) {
                if(vneImg.getPosition() == imageUrls.size()+1) {
                    imageUrls.add(vneImg.getImageUrl());
                }else {
                    imageUrls.add("public/venue/images/0_0.jpg");
                    }
            }
            return imageUrls;
        }
        //Collections.nCopies(int i,T o)快速生成一個集合(但不可變動，可以用ArrayList包裝來調整)
        List<String> defaultUrls = new ArrayList<>(Collections.nCopies(3, "public/venue/images/0_0.jpg"));
        return defaultUrls;
    }

    public void updateVneImg(VneImgBytesDTO vneImgBytesDTO){
        int vneId = vneImgBytesDTO.getVneId();
        int position = vneImgBytesDTO.getPosition();
        byte[] imageBytes = vneImgBytesDTO.getImageFile();

        // 檔案儲存路徑
        String uploadDir = "src/main/resources/public/uploads/venue/";//Dir=Directory（目錄）
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
                                  .imageUrl(filePath)
                                  .position(position)
                                  .build();
            vneImgUrlRepository.save(vneImgUrl);
        }
    }

    public void updateVneImg(int vneId, int position, byte[] imageBytes) {

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
                    .imageUrl(filePath)
                    .position(position)
                    .build();
            vneImgUrlRepository.save(vneImgUrl);
        }
    }

    public String getFileName(Integer vneId, Integer position){
        return  vneId + "_" + position + ".jpg"; // 自定義檔案名稱，我想確保不需要多存
    }
    public String getAssetPath(Integer vneId, Integer position){
        return "public/uploads/venue/"+getFileName(vneId, position);
    }
}
