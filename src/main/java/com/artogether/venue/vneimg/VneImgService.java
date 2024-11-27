package com.artogether.venue.vneimg;

import com.artogether.venue.vnebo.VneImgPositionBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class VneImgService {

    @Autowired
    private VneImgRepository vneImgRepository;
    @Resource
    RedisTemplate<String, byte[]> redisTemplate;

//    搜尋場地圖片
//    組裝成URL
//    存進Redis

    public VneImgPositionBO getAllImg(Integer vneId){
        VneImgPositionBO vneImgPositionBO = new VneImgPositionBO();
        vneImgPositionBO.setVneId(vneId);
        List<VneImg> images = vneImgRepository.findAllByVenueId(vneId);
        // 檢查返回的圖片列表是否為 null
        if (images == null || images.isEmpty()) {
            return vneImgPositionBO; //已經於DTO內預設佔位圖片路徑
        }
        for(VneImg img : images){
            Integer position = img.getPosition();
            //存入Redis(好像可以稱作緩存)
            saveImageToRedis(vneId, position, img.getImageFile());
            //switch要
            switch (position){
                case 1:vneImgPositionBO.setVneURL1(getImgUrl(vneId, position));break;
                case 2:vneImgPositionBO.setVneURL2(getImgUrl(vneId, position));break;
                case 3:vneImgPositionBO.setVneURL3(getImgUrl(vneId, position));break;
            }
        }
        return vneImgPositionBO;
    }

    public String updateImg(Integer vneId, Integer position,byte[] imageBytes){

        Optional<VneImg> vneImgOptional = vneImgRepository.findByVenueIdAndPosition(vneId, position);

        if(vneImgOptional.isPresent()){
            VneImg vneImg = vneImgOptional.get();

            vneImg.setImageFile(imageBytes);
            vneImgRepository.save(vneImg);
            // 將圖片儲存到 Redis
            saveImageToRedis(vneId, position, imageBytes);
        };
        return getImgUrl(vneId, position);

    }

    public String getImgUrl(Integer vneId, Integer position){
        return "/venue/images/"+vneId+"/"+position;
    }

    private void saveImageToRedis(Integer venueId, Integer position, byte[] imageBytes) {
        String redisKey = "venue:" + venueId + ":image:" + position;
        redisTemplate.opsForValue().set(redisKey, imageBytes, Duration.ofHours(6)); // 設定快取有效期
    }

    public byte[] getImageFromRedis(Integer venueId, Integer position) {
        String redisKey = "venue:" + venueId + ":image:" + position;
        byte[] imageBytes = (byte[])redisTemplate.opsForValue().get(redisKey);
        // 檢查是否存在圖片資料
        if (imageBytes == null) {
        return null;
        }
        return imageBytes;
    }
    // 將 Byte[] 轉換為 byte[]
//    private byte[] toPrimitive(Byte[] byteObjects) {
//        byte[] bytes = new byte[byteObjects.length];
//        for (int i = 0; i < byteObjects.length; i++) {
//            bytes[i] = byteObjects[i];
//        }
//        return bytes;
//    }

//    public VneImg upDateVneImg(Integer vneId){}


}
