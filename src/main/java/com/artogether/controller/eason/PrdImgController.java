//package com.artogether.controller.eason;
//
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.artogether.product.prd_img.PrdImg;
//
//
//@Controller
//@RequestMapping("product")
//public class PrdImgController {
//
//	@Autowired
//    private  PrdImgService prdImgService;
//
//
//
//    @GetMapping("/upload_image_form")
//    public String showUploadForm() {
//        return "product/upload_image_form"; // Thymeleaf 頁面名稱
//    }
//
//    @PostMapping("/upload")
//    public String uploadImageForProduct(
//            @RequestParam("productId") Integer productId,
//            @RequestParam("image") MultipartFile image,
//            Model model) {
//        try {
//            // 調用 Service 儲存圖片
//            prdImgService.saveImageForProduct(productId, image);
//            model.addAttribute("message", "圖片上傳成功！");
//        } catch (Exception e) {
//            model.addAttribute("error", "圖片上傳失敗：" + e.getMessage());
//        }
//
//        return "product/upload_image_form"; // 頁面名稱保持一致
//    }
//
//
//}
