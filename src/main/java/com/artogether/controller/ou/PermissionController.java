package com.artogether.controller.ou;

import com.artogether.common.perm_desc.PermDesc;
import com.artogether.common.perm_desc.PermDescRepository;
import com.artogether.common.permission.Permission;
import com.artogether.common.permission.PermissionDTO;
import com.artogether.common.permission.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private PermDescRepository permDescRepository;

    // 查詢所有權限
    @GetMapping("/pm")
    public String getAllPermissions(Model model) {
        List<PermissionDTO> permissionDTOS = permissionService.findAllDTO();
        model.addAttribute("permissions", permissionDTOS);
        return "platform/permission";  // JSP/HTML file to display the list of permissions
    }

//        List<Permission> permissions = permissionService.findAll();
//        model.addAttribute("permissions", permissions);
//        return "platform/permission";  // JSP/HTML file to display the list of permissions
//    }

    /*=======================
    顯示新增權限頁面與處理新增請求
     ========================*/
    @GetMapping("/pmadd")
    public String showAddPermissionForm(Model model) {
//        Permission permission = new Permission();
//        model.addAttribute("permission", new Permission());
//        List<PermDesc> list = permDescRepository.findAll();
        List<PermissionDTO> permissionDTOS = permissionService.findAllDTO();
        model.addAttribute("permDescs", permissionDTOS);
        return "platform/pmadd"; // 返回 Thymeleaf 模板名稱
    }

    @PostMapping("/pmadd")
    public String handleAddPermission(@ModelAttribute Permission permission, Model model) {
        permissionService.add(permission);
        List<Permission> permissions = permissionService.findAll();
        model.addAttribute("permissions", permissions);
        return "/platform/permission"; // 重新導向至權限列表
    }

    //===============================

    // 處理更新權限的請求
    @PostMapping("/update")
    public String updatePermission(@ModelAttribute Permission permission, Model model) {
        Permission updatedPermission = permissionService.update(permission);
        if (updatedPermission == null) {
            model.addAttribute("errorMessage", "權限更新失敗！");
            return "redirect:/permission/all";
        }
        return "redirect:/permission/all";  // Redirect to the list of all permissions
    }

    // 刪除權限
    @GetMapping("/delete/{id}")
    public String deletePermission(@PathVariable Integer id, Model model) {
        Permission permission = permissionService.findAll().stream()
                .filter(p -> p.getPermissionId().getManagerId().equals(id))  // 假設您可以用 id 創建一個 PermissionId 實例
                .findFirst()
                .orElse(null);
        if (permission != null) {
            permissionService.delete(permission);
        } else {
            model.addAttribute("errorMessage", "權限未找到！");
        }
        return "redirect:/permission/all";  // Redirect to the list of all permissions
    }
}
