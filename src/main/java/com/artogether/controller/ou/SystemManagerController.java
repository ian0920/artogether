package com.artogether.controller.ou;

import org.springframework.stereotype.Controller;

import com.artogether.common.system_mamager.SystemManager;
import com.artogether.common.system_mamager.SystemManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/system-managers")
public class SystemManagerController {

    @Autowired
    private SystemManagerService systemManagerService;

    // 新增平台管理員
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody SystemManager systemManager) {
        systemManagerService.add(systemManager);
        return ResponseEntity.ok().build();
    }

    // 修改
    @PutMapping("/{id}")
    public ResponseEntity<SystemManager> update(@PathVariable int id, @RequestBody SystemManager systemManager) {
        systemManagerService.update(systemManager);
        return ResponseEntity.ok(systemManagerService.findById(id));
    }

    // 狀態修改
    @PutMapping("/status/{id}")
    public ResponseEntity<SystemManager> updateStatus(@PathVariable int id) {
        SystemManager updatedManager = systemManagerService.update(id);
        return ResponseEntity.ok(updatedManager);
    }

    // 單一查詢
    @GetMapping("/{id}")
    public ResponseEntity<SystemManager> findById(@PathVariable int id) {
        SystemManager systemManager = systemManagerService.findById(id);
        if (systemManager != null) {
            return ResponseEntity.ok(systemManager);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 查詢全部
    @GetMapping
    public ResponseEntity<List<SystemManager>> findAll() {
        List<SystemManager> systemManagers = systemManagerService.findAll();
        return ResponseEntity.ok(systemManagers);
    }
}

