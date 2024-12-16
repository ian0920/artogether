package com.artogether.controller.ian;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessService;
import com.artogether.common.business_perm.BusinessPerm;
import com.artogether.common.business_perm.BusinessPermService;
import com.artogether.common.member.Member;
import com.artogether.common.member.MemberService;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventService;
import com.artogether.product.product.Product;
import com.artogether.product.product.ProductService;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Controller
public class GeneralController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private BusinessPermService businessPermService;

    @Autowired
    private EventService eventService;

    @Autowired
	private BusinessService businessService;
    
    @Autowired
    private ProductService productService;

    //拜訪首頁
    @GetMapping("/")
    public String hello(Model model) {

        //精選活動呈現
        List<Event> topThreeEvent = eventService.findAllEvents("enrolledR").subList(0,3);
        model.addAttribute("topThreeEvents", topThreeEvent);
        
        //精選商城呈現
        List<BusinessMember> randomVendors = businessService.getRandomBusinessMembers(); 
	    model.addAttribute("vendors", randomVendors);
	    
	    //精選商品呈現
	    
	    List<Product> topProducts = productService.getTopRatedProducts(3);

	    model.addAttribute("topProducts", topProducts);

	    return "homepage";
    }

    //帳號註冊頁面拜訪
    @GetMapping("register")
    public String landing(Model model) {
        model.addAttribute("member", new Member());
        //給第一次拜訪頁面時賦予欄位空值使用，後續若有格式驗證錯誤，則會保留原先輸入的資料
        return "frontend/register";
    }

    //帳號註冊資料驗證
    @PostMapping("register")
    public String register (@Valid Member member, Model model) throws BindException {

        memberService.register(member);
        return "Test_success";
    }

    //帳號啟用
    @GetMapping("memberVerifier")
    public String memberVerifier(Integer memberId, Model model) {

        //確認此會員id的狀態
        Member check = memberService.findById(memberId);

        //http://localhost:8080/memberVerifier?memberId=22

        //確定帳號狀態
        if(check == null) {
            //該帳號id查無資料(ex:使用者自行修改郵件內連結的id)
            model.addAttribute("message", "查無此帳號");
        }else if (check.getStatus() == (byte)0) {
            //剛註冊的帳號完成啟用
            Member member = memberService.memberVerify(memberId);
            model.addAttribute("member", member);
            model.addAttribute("message", "帳號驗證成功已順利啟用！");
        }else if(check.getStatus() == (byte)1){
            //帳號已啟用
            model.addAttribute("member", check);
            model.addAttribute("message", "帳號先前已完成驗證");
        } else if(check.getStatus() == (byte)2) {
            //被停權的帳號試圖使用連結重新啟用
            model.addAttribute("message", "該帳號因違反使用規定已被停權");
        }

        return "frontend/memberVerify";
    }


    //一般會員登入頁面拜訪
    @GetMapping("login")
    public String login(Model model) {

        Map<String, String> errors = new HashMap<>();
        model.addAttribute("errors", errors);
        return "frontend/login";
    }



    //一般會員登入驗證
    @PostMapping("login")
    public String login(
            @NotBlank(message = "Email請勿空白")
            @Email(message = "Email格式有誤")
            String email,
            @NotBlank(message = "密碼請勿空白")
            @Length(min=8, message = "密碼長度不足，至少8位數密碼以上")
            String password, Model model, HttpSession session)
    {

        Member find = memberService.findByEmail(email);
        Map<String, String> errors = new HashMap<>();

        //確認是否有此email帳號註冊會員
        if(find == null) {
            errors.put("email", "查無此email註冊的帳號");
            model.addAttribute("errors", errors);

        } else if(!password.equals(find.getPassword())) {
            //確認密碼是否正確
            errors.put("password", "密碼不正確");
            model.addAttribute("errors",errors);
        } else if (find.getStatus() != 1) {
            errors.put("email", "帳號尚未啟用或已被停權");
            model.addAttribute("errors", errors);
        } else {
            session.setAttribute("islogin", true);
            session.setAttribute("member", find.getId());
            session.setAttribute("memberName", find.getName());
        }


        return errors.isEmpty() ? "redirect:/" : "frontend/login";
    }



    //一般會員登出
    @GetMapping({"logout"})
    public String logout(HttpSession session) {
        session.removeAttribute("islogin");
        session.removeAttribute("member");
        session.removeAttribute("memberObject");
        return "redirect:/";
    }


    //商家會員登入
    @GetMapping({"business_login"})
    public String businessLogin(HttpSession session, Model model) {
        Integer memberId = (Integer)session.getAttribute("member");
        List<BusinessPerm> businessPerms = businessPermService.getPermsByMember(memberId);
        if (!businessPerms.isEmpty()) {
            List<BusinessMember> businessMembers = new ArrayList();
            businessPerms.forEach((businessPerm) -> {
                businessMembers.add(businessPerm.getBusinessMember());
            });

            List<BusinessMember> sortedBusinessMember = businessMembers.stream().sorted(Comparator.comparing(BusinessMember::getId)).toList();

            BusinessMember presentBusinessMember = sortedBusinessMember.get(0);
            List<BusinessMember> listedBusinessMember = new ArrayList();
            for(BusinessMember m : sortedBusinessMember) {
                if(m.getId() != presentBusinessMember.getId()) {
                    listedBusinessMember.add(m);
                }
            }



            //將第一個商家作為預設登入的商家並寫入session
            session.setAttribute("presentBusinessMember", presentBusinessMember);
            session.setAttribute("businessMembers", listedBusinessMember);
        }

        return businessPerms.isEmpty() ? "redirect:/" : "homepage_business";
    }



    //商家會員切換
    @GetMapping({"businessMemberSwitch"})
    public String businessMemberSwitch(Integer index, HttpSession session) {


        BusinessMember oldPresentBusinessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        List<BusinessMember> businessMembers = (List<BusinessMember>) session.getAttribute("businessMembers");

        //從清單抓出要切換的商家，加入session
        BusinessMember PresentBusinessMember = businessMembers.get(index);


        //排除選到的商家，創建新list(舊的list immutable，所以創一個新的來裝)
        List<BusinessMember> newList = new ArrayList();
        for(BusinessMember m : businessMembers) {
            if(m.getId() != PresentBusinessMember.getId()) {
                newList.add(m);
            }
        }

        //將切換前的商家加回來
        newList.add(oldPresentBusinessMember);


        //將要呈現回去的商家排序
        businessMembers.sort(Comparator.comparing(BusinessMember::getId));


        //將切換至的商家寫入session

        session.setAttribute("presentBusinessMember", PresentBusinessMember);
        session.setAttribute("businessMembers", newList);


        return "homepage_business";
    }


    //商家會員登出
    @GetMapping({"business_logout"})
    public String businessLogout(HttpSession session) {
        session.removeAttribute("presentBusinessMember");
        session.removeAttribute("businessMembers");
        return "redirect:/";
    }

    //會員個人頁面
    @GetMapping("member")
    public String memeberPage(){

        return"member_page";
    }


    //過場頁面測試
    @GetMapping("status")
    public String statusPage(Model model) {

        Map<String, String> message = new HashMap<>();
        //key -> ok or no  value -> 訊息
        message.put("no", "失敗");
        model.addAttribute("message", message);

        return"transient_page/status_page";
    }

}
