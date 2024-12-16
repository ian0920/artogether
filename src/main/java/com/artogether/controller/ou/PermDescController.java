package com.artogether.controller.ou;

import com.artogether.common.perm_desc.PermDesc;
import com.artogether.common.perm_desc.PermDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/permDesc")
public class PermDescController {

    @Autowired
    private PermDescService permDescService;

    // 點擊後進入畫面並查詢全部功能說明並顯示在頁面
    @GetMapping("/page") // 網址
    public String getPermDescPage(Model model) {
        List<PermDesc> allPermDesc = permDescService.findAll();
//        allPermDesc.forEach(permDesc -> System.out.println(permDesc.getDescription()));
        model.addAttribute("permDescs", allPermDesc);
        return "/platform/permDesc";  // 位置
    }


    /*===================
        新增功能說明頁面
     ====================*/
    @GetMapping("/pdfadd")
    public String addViewPermDesc(Model model) {
        model.addAttribute("permDesc", new PermDesc());
//        System.out.println(((PermDesc) model.getAttribute("permDesc")).getDescription());
        return "platform/pdfadd";
    }

    @PostMapping("/pdfadd")
    public String addPermDesc(@ModelAttribute PermDesc permDesc, Model model) {

        List<String> errors = permDescService.permDescError(permDesc);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "platform/pdfadd";
        }

        // 調用 Service 或 Repository 進行儲存邏輯
        permDescService.save(permDesc); // 示例：調用 Service 方法
        List<PermDesc> allPermDesc = permDescService.findAll();
//        model.addAttribute("message", isSuccess ? "新增成功！" : "新增失敗！");
        model.addAttribute("permDescs", allPermDesc);
        return "/platform/permDesc"; // 成功後重定向至列表頁面
    }

    /* =================================================================================== */

    // 處理更新功能說明的請求
    @PostMapping("/update")
    public String updatePD(@ModelAttribute PermDesc permDesc) {
        permDescService.update(permDesc);
        return "redirect:/permDesc/page";
    }

    // 刪除功能說明
    @GetMapping("/delete/{id}")
    public String removePD(@PathVariable Integer id) {
        PermDesc permDesc = new PermDesc();
        permDesc.setId(id);
        permDescService.delete(permDesc);
        return "redirect:/permDesc/page";
    }
}

