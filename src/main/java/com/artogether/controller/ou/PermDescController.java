package com.artogether.controller.ou;

import org.springframework.stereotype.Controller;
import com.artogether.common.perm_desc.PermDesc;
import com.artogether.common.perm_desc.PermDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/perm-desc")
public class PermDescController {

    @Autowired
    private PermDescService permDescService;

    // 新增平台功能說明
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody PermDesc permDesc) {
        List<PermDesc> existingPermDescs = permDescService.findAll();
        boolean exists = existingPermDescs.stream().anyMatch(p -> p.getDescription().equals(permDesc.getDescription()));

        if (!exists) {
            permDescService.add(permDesc);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(409).build(); // 返回 409 衝突狀態碼
        }
    }

    // 刪除
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        PermDesc permDesc = permDescService.findById(id);
        if (permDesc != null) {
            permDescService.delete(permDesc);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 更新
    @PutMapping("/{id}")
    public ResponseEntity<PermDesc> update(@PathVariable int id, @RequestBody PermDesc permDesc) {
        permDesc.setId(id); // 確保更新的是正確的 PermDesc
        permDescService.update(permDesc);
        return ResponseEntity.ok(permDesc);
    }

    // 查詢全部
    @GetMapping
    public ResponseEntity<List<PermDesc>> findAll() {
        List<PermDesc> permDescs = permDescService.findAll();
        return ResponseEntity.ok(permDescs);
    }

    // 單一查詢
    @GetMapping("/{id}")
    public ResponseEntity<PermDesc> findById(@PathVariable int id) {
        PermDesc permDesc = permDescService.findById(id);
        if (permDesc != null) {
            return ResponseEntity.ok(permDesc);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

