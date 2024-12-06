package com.artogether.controller.ou;

import com.artogether.common.permission.Permission;
import com.artogether.common.permission.PermissionService;
import com.artogether.common.perm_desc.PermDesc;
import com.artogether.common.perm_desc.PermDescService;
import com.artogether.common.system_mamager.SystemManager;
import com.artogether.common.system_mamager.SystemManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/platform")
public class CommonController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermDescService permDescService;

    @Autowired
    private SystemManagerService systemManagerService;

    // 平台功能說明頁面
    @GetMapping("/management")
    public String getPermDescPage(Model model) {
        List<PermDesc> permDescAll = permDescService.findAll();
        permDescAll.forEach(permDesc -> System.out.println(permDesc.getDescription()));
        model.addAttribute("permDescs", permDescAll);
        return "platform/management";
    }

    // 平台管理員頁面
//    @GetMapping("/management")
//    public String getSystemManagersPage(Model model) {
//        List<SystemManager> systemManagers = systemManagerService.findAll();
//        model.addAttribute("systemManagers", systemManagers);
//        return "platform/management";
//    }

    // 新增平台功能說明
    @PostMapping("/permdesc/add")
    public String addPermDesc(@RequestParam String description) {
        PermDesc permDesc = new PermDesc();
        permDesc.setDescription(description);
        permDescService.add(permDesc);
        return "redirect:/platform/permdesc";
    }

    // 刪除平台功能說明
    @RequestMapping(value = "/permdesc/{id}", method = RequestMethod.DELETE)
    public String deletePermDesc(@PathVariable int id) {
        PermDesc permDesc = permDescService.findById(id);
        if (permDesc != null) {
            permDescService.delete(permDesc);
        }
        return "redirect:/platform/permdesc";
    }

    // 權限頁面
    @GetMapping("/permissions")
    public String getPermissionsPage(Model model) {
        List<Permission> permissions = permissionService.findAll();
        model.addAttribute("permissions", permissions);
        return "platform/permissions";
    }

    // 新增權限
    @PostMapping("/permissions/add")
    public String addPermission(@RequestParam Integer managerId, @RequestParam Integer descId) {
        // 創建 PermissionId，並設置 managerId 和 descId
        Permission.PermissionId permissionId = new Permission.PermissionId(managerId, descId);

        // 創建 Permission 物件，並設置 PermissionId
        Permission permission = new Permission();
        permission.setPermissionId(permissionId);  // 使用複合主鍵設置

        // 調用 service 方法保存權限
        permissionService.add(permission);

        return "redirect:/platform/permissions";  // 重定向到權限列表頁面
    }

    // 刪除權限
    @RequestMapping(value = "/permissions/{id}", method = RequestMethod.DELETE)
    public String deletePermission(@PathVariable int id) {
        Permission permission = permissionService.findById(id);
        if (permission != null) {
            permissionService.delete(permission);
        }
        return "redirect:/platform/permissions";
    }

//    // 平台管理員頁面
//    @GetMapping("/management")
//    public String getSystemManagersPage(Model model) {
//        List<SystemManager> systemManagers = systemManagerService.findAll();
//        model.addAttribute("systemManagers", systemManagers);
//        return "platform/management";
//    }

    // 新增平台管理員
    @PostMapping("/system-managers/add")
    public String addSystemManager(@RequestParam String username) {
        SystemManager systemManager = new SystemManager();
//        systemManager.setId(systemManager.getId());
        systemManagerService.add(systemManager);
        return "redirect:/platform/management";
    }

    // 刪除平台管理員
    @RequestMapping(value = "/system-managers/{id}", method = RequestMethod.DELETE)
    public String deleteSystemManager(@PathVariable int id) {
        SystemManager systemManager = systemManagerService.findById(id);
        if (systemManager != null) {
            systemManagerService.delete(id);
        }
        return "redirect:/platform/management";
    }
}
