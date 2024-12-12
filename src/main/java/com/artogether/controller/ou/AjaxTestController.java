package com.artogether.controller.ou;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@ResponseBody // 物件自動轉json
@RestController
public class AjaxTestController {

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        System.out.println("test...");
        Map<String, String> a = new HashMap<String, String>();
        a.put("code", "200");
        a.put("msg", "這是測試");
        return ResponseEntity.ok(a);

    }
}
