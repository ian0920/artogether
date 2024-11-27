package com.artogether.controller;

import com.artogether.venue.vnebo.VneImgPositionBO;
import com.artogether.venue.vneimg.VneImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("venue/images")
public class VneImgController {
    @Resource
    private RedisTemplate<String, Byte[]> redisTemplate;
    @Autowired
    private VneImgService vneImgService;

    @GetMapping("/{vneId}")
    public ResponseEntity<VneImgPositionBO> setImagesUrl(@PathVariable("vneId")Integer vneId) {
        VneImgPositionBO vipDTO = vneImgService.getAllImg(vneId);
        return ResponseEntity.ok(vipDTO); // 返回 200 狀態碼以及 把 DTO 轉成json
    }

    @GetMapping("/{vneId}/{position}")
    public ResponseEntity<byte[]> getImage(@PathVariable("vneId") Integer vneId,
                                           @PathVariable("position") Integer position) {
        // 從 Redis 取得圖片資料
        byte[] imageData = vneImgService.getImageFromRedis(vneId, position);
        // 檢查是否存在圖片資料
        if (imageData == null) {
            return ResponseEntity.notFound().build(); // 回傳 404 狀態碼
//            <img src="/venue/{vneId}/images/{position}"
//            onerror="this.onerror=null; this.src='/static/web_design/asset/images/placeholder.jpg';" />
        }
        // 設定 HTTP 標頭，例如 Content-Type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // 根據實際圖片格式設置，如 IMAGE_PNG
        // 建立並回傳 ResponseEntity，將 byte[] 作為回應主體
        return ResponseEntity.ok()
                .headers(headers)
                .body(imageData);
    }
    @PostMapping("upload/{vneId}/{position}")
    public ResponseEntity<String> uploadImage(@PathVariable("vneId") Integer vneId,
                                              @PathVariable("position") Integer position,
                                              @RequestParam("image") MultipartFile imageFile) {
        try {
            // 檢查是否有檔案被上傳
            if (imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body("請選擇要上傳的圖片。");
            }
            // 獲取圖片的 byte[] 數據
            byte[] imageBytes = imageFile.getBytes();
            String imgUrl = vneImgService.updateImg(vneId, position, imageBytes);

//        實際希望回傳的
            return ResponseEntity.ok()
                    .body(imgUrl);

        } catch (Exception e) {
            // 錯誤處理
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("圖片上傳失敗：" + e.getMessage());
        }
    }

}