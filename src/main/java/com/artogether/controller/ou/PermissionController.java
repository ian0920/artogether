package com.artogether.controller.ou;

import com.artogether.common.perm_desc.PermDesc;
import com.artogether.common.perm_desc.PermDescService;
import com.artogether.common.permission.Permission;
import com.artogether.common.permission.PermissionDTO;
import com.artogether.common.permission.PermissionService;
import com.artogether.common.system_manager.SystemManager;
import com.artogether.common.system_manager.SystemManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private PermDescService permDescService;
    @Autowired
    private SystemManagerService systemManagerService;

    // 查詢所有權限
    @GetMapping("/pm")
    public String getAllPermissions(Model model) {
        List<PermissionDTO> permissionDTOS = permissionService.findAllDTO();
        model.addAttribute("permissions", permissionDTOS);
        return "platform/permission";  // JSP/HTML file to display the list of permissions
    }

    // 沒有DTO版本
//        List<Permission> permissions = permissionService.findAll();
//        model.addAttribute("permissions", permissions);
//        return "platform/permission";  // JSP/HTML file to display the list of permissions
//    }

    /*=======================
    顯示新增權限頁面與處理新增請求
     ========================*/

//    @GetMapping("/pmadd")
//    public String showAddPermissionForm(Model model) {
//        Permission permission = new Permission();
//        model.addAttribute("permission", new Permission());
//        List<PermDesc> list = permDescRepository.findAll();
//        List<PermissionDTO> permissionDTOS = permissionService.findAllDTO();
//        model.addAttribute("permission", permissionDTOS);
//        return "platform/pmadd"; // 返回 Thymeleaf 模板名稱
//    }

    @GetMapping("/pmadd")
    public String showAddPermissionForm(Model model) {

        List<SystemManager> managerList = systemManagerService.findAll();
        List<PermDesc> permDescList = permDescService.findAll();

        model.addAttribute("managerList", managerList);
        model.addAttribute("permDescList", permDescList);


        return "platform/pmadd"; // 返回 Thymeleaf 模板名稱
    }

    // DTO回傳
    @PostMapping("/pmadd")
    public String handleAddPermission(Integer managerId, Integer permDescId, Model model) {

//        System.out.println(managerId);
//        System.out.println(permDescId);

        List<PermissionDTO> permissionDTOS = permissionService.findAllDTO();
        model.addAttribute("permissions", permissionDTOS);

        List<SystemManager> managerList = systemManagerService.findAll();
        List<PermDesc> permDescList = permDescService.findAll();

        model.addAttribute("managerList", managerList);
        model.addAttribute("permDescList", permDescList);

        // 找出管理員
        List<Permission> list = permissionService.findByManagerId(managerId);

        // 判斷管理員權限是否重複
        boolean hasPermission = list.stream().anyMatch(p -> Objects.equals(p.getPermDesc().getId(), permDescId));
//        boolean hasPermission = list.stream().anyMatch(p -> p.getPermDesc().getId().equals(permDescId));

        if (!hasPermission) {
            Permission p = new Permission();

            permissionService.addNewPerm(managerId, permDescId);
            model.addAttribute("message", "Permission added successfully.");
        } else {
            model.addAttribute("message", "You already have this permission.");
            return "platform/pmadd";
        }


//        List<Permission> permissions = permissionService.findAll();
//        model.addAttribute("permissions", permissions);
        return "/platform/permission"; // 重新導向至權限列表
    }


    /* 邏輯
     *
     * String message = null;
     *   form -> managerId permId
     *  findbyid(manager) -> List<perm>
     *  list.foreach( list id == permId )
     *  message = "此會員已有此權限"
     *
     * (list id != permId)
     *  repo.save()
     *  message = "新增成功"
     *
     *  model.addAttribute("message", message)
     *  retrun:""
     *
     * <h2 th:if="${message != null}" th:text="${message}"></2>
     *
     * */


    /* =================================================================================== */

    /*==============
     處理更新權限的請求
     ===============*/

//    @Transactional
//    @GetMapping("/publish")

    /// /    修改活動狀態ㄉ按鈕
//    public String publish(@RequestParam Integer id, @RequestParam Integer status) {
//        evtService.updateEvtStatus(id, status);
//        return "redirect:/event/listall";
//    }
    @PostMapping("/update")
    public String updatePermission(@ModelAttribute Permission permission, Model model) {
        Permission updatedPermission = permissionService.update(permission);
        if (updatedPermission == null) {
            model.addAttribute("errorMessage", "權限更新失敗！");
            return "/permission/all";
        }
        return "permission/all";  // Redirect to the list of all permissions
    }

    /*======
     刪除權限
     =======*/
    @GetMapping("/deletePm")
    public String deletePermission(@RequestParam Integer managerId,Integer permDescId, Model model) {
        // 查找該管理員擁有的所有權限
        Optional<Permission> permission = permissionService.findById(managerId, permDescId);

        // 刪除權限
        permission.ifPresent(p -> permissionService.delete(p));

        List<PermissionDTO> permissionDTOS = permissionService.findAllDTO();
        model.addAttribute("permissions", permissionDTOS);

        return "redirect:/permission/pm"; // 或者重定向到其他頁面，根據需求
    }
}
