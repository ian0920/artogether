package com.artogether.controller.eason;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.artogether.product.prd_coup.PrdCoup;
import com.artogether.product.prd_coup.PrdCoupService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/coupons")
public class PrdCoupController {

    @Autowired
    private PrdCoupService prdCoupService;

    // 1. 新增優惠券
    @PostMapping
    public String addCoupon(@RequestBody PrdCoup prdCoup) {
        int result = prdCoupService.addPrdCoup(prdCoup);
        return result > 0 ? "新增成功" : "新增失敗";
    }

    // 2. 更新優惠券
    @PutMapping("/{id}")
    public String updateCoupon(@PathVariable Integer id, @RequestBody PrdCoup prdCoup) {
        prdCoup.setId(id); // 確保更新目標的 ID
        int result = prdCoupService.updatePrdCoup(prdCoup);
        return result > 0 ? "更新成功" : "更新失敗";
    }

    // 3. 刪除優惠券
    @DeleteMapping("/{id}")
    public String deleteCoupon(@PathVariable Integer id) {
        PrdCoup prdCoup = new PrdCoup();
        prdCoup.setId(id);
        int result = prdCoupService.deletePrdCoup(prdCoup);
        return result > 0 ? "刪除成功" : "刪除失敗";
    }

    // 4. 查詢單一優惠券
    @GetMapping("/{id}")
    public PrdCoup getCouponById(@PathVariable Integer id) {
        return prdCoupService.findCoupById(id);
    }

    // 5. 查詢所有優惠券
    @GetMapping
    public List<PrdCoup> getAllCoupons() {
        return prdCoupService.getAllCoupons();
    }

    // 6. 查詢特定商家的優惠券
    @GetMapping("/business/{businessId}")
    public List<PrdCoup> getCouponsByBusinessId(@PathVariable Integer businessId) {
        return prdCoupService.findCouponsByBusinessId(businessId);
    }

    // 7. 根據條件查詢優惠券（例如名稱、類型、狀態、門檻）
    @PostMapping("/search")
    public List<PrdCoup> searchCoupons(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer threshold
    ) {
        return prdCoupService.findCouponsByCriteria(name, type, status, threshold);
    }


    // 8. 查詢有效期間內的優惠券
    @GetMapping("/valid")
    public List<PrdCoup> getValidCoupons() {
        return prdCoupService.findValidCoupons(LocalDateTime.now());
    }

    // 9. 查詢即將到期的優惠券
    @GetMapping("/expiring-soon/{days}")
    public List<PrdCoup> getCouponsExpiringSoon(@PathVariable Integer days) {
        return prdCoupService.findCouponsExpiringSoon(LocalDateTime.now(), days);
    }

    // 10. 根據狀態查詢優惠券
    @GetMapping("/status/{status}")
    public List<PrdCoup> getCouponsByStatus(@PathVariable Integer status) {
        return prdCoupService.findCouponsByStatus(status);
    }

    // 11. 根據類型查詢優惠券
    @GetMapping("/type/{type}")
    public List<PrdCoup> getCouponsByType(@PathVariable Integer type) {
        return prdCoupService.findCouponsByType(type);
    }

    // 12. 根據多條件組合查詢優惠券

    @GetMapping("/filter")
    public List<PrdCoup> filterCoupons(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer threshold) {
        return prdCoupService.findCouponsByMultipleCriteria(status, type, threshold);
    }

    // 13. 查詢目前「活動中」的優惠券
    @GetMapping("/active")
    public List<PrdCoup> getActiveCoupons() {
        return prdCoupService.findActiveCoupons(LocalDateTime.now());
    }
}
