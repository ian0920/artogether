//package com.artogether.product.cart.model;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/test")
//public class TestController {
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @GetMapping("/redis")
//    public String testRedis() {
//        redisTemplate.opsForValue().set("testKey", "Hello, Redis!");
//        return (String) redisTemplate.opsForValue().get("testKey");
//    }
//}