package com.artogether.aop;

import groovy.util.logging.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionHandle {


    //處理輸入資料是否符合格式需求
    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException ex, HttpServletRequest request, Model model) {


        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );


        model.addAttribute("member", ex.getTarget());
        model.addAttribute("errors", errors);


        //寫入資料庫時違反unique條件
        if (ex.getObjectName().equals("emailDuplicate")) {
            errors.put("email", "email已經被註冊過，請使用其他email信箱");
        }

        String viewName = request.getServletPath();
        return viewName != null ? "frontend" + viewName : "error";


    }


    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleException(RuntimeException ex) {

        ModelAndView mv = new ModelAndView();

        Map<String, String> error = new HashMap<>();
        error.put("no", ex.getMessage());


        mv.addObject("message", error);
        mv.setViewName("transient_page/status_page");


        return mv;
    }



}
