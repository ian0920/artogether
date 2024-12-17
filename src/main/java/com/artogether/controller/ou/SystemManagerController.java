package com.artogether.controller.ou;

import com.artogether.common.system_manager.SystemManager;
import com.artogether.common.system_manager.SystemManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/systemManager")
public class SystemManagerController {

    @Autowired
    private SystemManagerService systemManagerService;

    // 查詢全部系統管理員
    @GetMapping("/manager")
    public String getAllSystemManagers(Model model) {
        List<SystemManager> systemManagers = systemManagerService.findAll();
//        systemManagers.forEach(systemManager -> {
//            System.out.println(systemManager);
//        });
        model.addAttribute("systemManagers", systemManagers);
        return "platform/systemManager";
    }

    /*=====================
    顯示新增系統管理員的表單頁面
     ======================*/
    @GetMapping("/smadd")
    public String addSystemManagerPage(Model model) {
        model.addAttribute("systemManager", new SystemManager());
        return "/platform/smadd";  // JSP/HTML file for adding a new system manager
    }

    // 處理新增系統管理員的請求
    @PostMapping("/smadd")
    public String addSystemManager(@ModelAttribute SystemManager systemManager, Model model) {


        List<String> errors = systemManagerService.validateInput(systemManager);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "platform/smadd";
        }

        systemManagerService.add(systemManager);
        List<SystemManager> systemManagers = systemManagerService.findAll();
        model.addAttribute("systemManagers", systemManagers);
        return "/platform/systemManager";
    }

    /* =================================================================================== */

    // 顯示更新系統管理員的表單頁面
    @GetMapping("smUpdate")
    public String updateSystemManagerPage(@RequestParam Integer id, Model model) {
        SystemManager systemManager = systemManagerService.findById(id);
        if(systemManager.getStatus() == 0){
            systemManager.setStatus((byte) 1);
        }else {
            systemManager.setStatus((byte) 0);
        }
        systemManagerService.update(systemManager);
        List<SystemManager> systemManagers = systemManagerService.findAll();
        model.addAttribute("systemManagers", systemManagers);
        return "redirect:/systemManager/manager";
    }

    // 刪除系統管理員
    @GetMapping("/delete/{id}")
    public String deleteSystemManager(@PathVariable int id, Model model) {
        SystemManager systemManager = systemManagerService.findById(id);
        if (systemManager != null) {
            systemManagerService.delete(id);  // Call delete service method
        } else {
            model.addAttribute("errorMessage", "系統管理員未找到！");
        }
        return "redirect:/systemManager/all";  // Redirect to the list of all system managers
    }

}