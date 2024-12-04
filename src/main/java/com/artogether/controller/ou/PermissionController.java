package com.artogether.controller.ou;

import com.artogether.common.permission.PermissionAnn;
import com.artogether.common.permission.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List; // 導入List
import com.artogether.common.permission.Permission; // 導入Permission

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    // 新增權限
    @PostMapping("/add") public void addPermission(@RequestBody Permission permission) {
        permissionService.add(permission);
    }

    // 單一查詢
    @GetMapping("/{id}") public Permission getPermission(@PathVariable int id) {
        return permissionService.findById(id);
    }

    // 查詢全部
    @GetMapping("/all")
    public List<Permission> getAllPermissions() {
        return permissionService.findAll();
    }

    // 更新狀態
    @PutMapping("/update/{id}&{point}")
    public Permission updatePermission(@PathVariable int id, @PathVariable String point) {
        return null;

    }

    @GetMapping("/getTest")
    @ResponseBody // 文字文本回傳
    @PermissionAnn("1")
    public String getTestPermission() {
        System.out.println("getTestPermission...");
        return "test";
    }


}
