//package com.artogether.controller.eason;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.artogether.product.prd_coup.PrdCoup;
//import com.artogether.product.prd_coup.PrdCoupService;
//
//import java.util.Date;
//import java.util.List;
//
//@Controller
//@RequestMapping("/coupons")
//public class PrdCoupController {
//
//    @Autowired
//    private PrdCoupService prdCoupService;
//
//    // ==============================
//    // 優惠券列表頁面
//    // ==============================
//    @GetMapping("/list")
//    public String getAllCoupons(Model model) {
//        List<PrdCoup> coupons = prdCoupService.getAllCoupons();
//        model.addAttribute("coupons", coupons); // 傳遞所有優惠券至模板
//        return "coupons/list"; // 返回優惠券列表的 Thymeleaf 模板
//    }
//
//    // ==============================
//    // 新增優惠券頁面
//    // ==============================
//    @GetMapping("/add")
//    public String showAddForm(Model model) {
//        model.addAttribute("prdCoup", new PrdCoup()); // 傳遞空的 PrdCoup 物件至表單
//        return "coupons/add"; // 返回新增優惠券頁面的 Thymeleaf 模板
//    }
//
//    @PostMapping("/add")
//    public String addPrdCoup(@ModelAttribute PrdCoup prdCoup, Model model) {
//        int result = prdCoupService.addPrdCoup(prdCoup); // 調用 Service 新增方法
//        model.addAttribute("message", result > 0 ? "新增成功！" : "新增失敗！");
//        return "redirect:/coupons/list"; // 新增成功後重定向至列表頁面
//    }
//
//    // ==============================
//    // 編輯優惠券頁面
//    // ==============================
//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable Integer id, Model model) {
//        PrdCoup prdCoup = prdCoupService.findCoupById(id); // 根據 ID 查詢優惠券
//        model.addAttribute("prdCoup", prdCoup);
//        return "coupons/edit"; // 返回編輯頁面的 Thymeleaf 模板
//    }
//
//    @PostMapping("/update/{id}")
//    public String updatePrdCoup(@PathVariable Integer id, @ModelAttribute PrdCoup prdCoup, RedirectAttributes redirectAttributes) {
//        prdCoup.setId(id); // 設置更新的優惠券 ID
//        int result = prdCoupService.updatePrdCoup(prdCoup); // 調用 Service 更新方法
//        redirectAttributes.addFlashAttribute("message", result > 0 ? "更新成功！" : "更新失敗！");
//        return "redirect:/coupons/list"; // 更新成功後重定向至列表頁面
//    }
//
//
//    // ==============================
//    // 刪除優惠券
//    // ==============================
//    @PostMapping("/delete/{id}")
//    public String deletePrdCoup(@PathVariable Integer id, Model model) {
//        PrdCoup prdCoup = new PrdCoup();
//        prdCoup.setId(id); // 設置刪除的優惠券 ID
//        int result = prdCoupService.deletePrdCoup(prdCoup); // 調用 Service 刪除方法
//        model.addAttribute("message", result > 0 ? "刪除成功！" : "刪除失敗！");
//        return "redirect:/coupons/list"; // 刪除成功後重定向至列表頁面
//    }
//
//    // ==============================
//    // 根據條件查詢優惠券
//    // ==============================
//    @GetMapping("/search")
//    public String findCouponsByCriteria(
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) Integer type,
//            @RequestParam(required = false) Integer status,
//            @RequestParam(required = false) Integer threshold,
//            Model model) {
//        List<PrdCoup> coupons = prdCoupService.findCouponsByCriteria(name, type, status, threshold); // 調用 Service 查詢方法
//        model.addAttribute("coupons", coupons);
//        return "coupons/search"; // 返回查詢結果的 Thymeleaf 模板
//    }
//
//    // ==============================
//    // 查詢有效期間內的優惠券
//    // ==============================
//    @GetMapping("/active")
//    public String findActiveCoupons(@RequestParam Date currentDate, Model model) {
//        List<PrdCoup> coupons = prdCoupService.findActiveCoupons(currentDate); // 調用 Service 查詢有效優惠券
//        model.addAttribute("coupons", coupons);
//        return "coupons/active"; // 返回有效優惠券的 Thymeleaf 模板
//    }
//
//    // ==============================
//    // 查詢即將過期的優惠券
//    // ==============================
//    @GetMapping("/expiring")
//    public String findCouponsExpiringSoon(@RequestParam Date now, @RequestParam Integer days, Model model) {
//        List<PrdCoup> coupons = prdCoupService.findCouponsExpiringSoon(now, days); // 調用 Service 查詢即將過期的優惠券
//        model.addAttribute("coupons", coupons);
//        return "coupons/expiring"; // 返回即將過期的優惠券頁面
//    }
//}


