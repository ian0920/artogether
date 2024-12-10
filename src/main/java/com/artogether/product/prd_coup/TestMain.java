//package com.artogether.product.prd_coup;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.context.ApplicationContext;
//
//import com.artogether.ArtogetherApplication;
//
//import java.util.List;
//
//public class TestMain {
//    public static void main(String[] args) {
//        // 啟動 Spring Boot 應用程序上下文
//        ApplicationContext context = SpringApplication.run(ArtogetherApplication.class, args);
//
//        // 獲取 Repository Bean
//        PrdCoupRepository prdCoupRepository = context.getBean(PrdCoupRepository.class);
//
//        // 測試 Repository 方法
//        try {
//            List<PrdCoup> results = prdCoupRepository.findByBusinessMemberId(1); // 替換為實際的 businessMemberId
//            System.out.println("結果數量：" + results.size());
//            for (PrdCoup coupon : results) {
//                System.out.println(coupon);
//            }
//        } catch (Exception e) {
//            System.err.println("執行測試時發生錯誤：" + e.getMessage());
//            e.printStackTrace();
//        }
//
//        // 結束應用程序
//        SpringApplication.exit(context);
//    }
//}

