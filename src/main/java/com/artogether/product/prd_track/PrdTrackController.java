package com.artogether.product.prd_track;

import com.artogether.common.member.Member;
import com.artogether.product.prd_img.PrdImg;
import com.artogether.product.prd_img.PrdImgRepository;
import com.artogether.product.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Base64;
import java.util.List;


@Controller
@RequestMapping("/prdTrack")
public class PrdTrackController {

    private final PrdTrackService prdTrackService;

    @Autowired
    private PrdImgRepository prdImgRepository;

    @Autowired
    public PrdTrackController (PrdTrackService prdTrackService){
        this.prdTrackService = prdTrackService;
    }

    // 新增收藏
    @PostMapping("/addTrack")
    public String addPrdToTrack(@RequestParam(value = "productId", required = true) Integer productId, HttpSession session){
        Integer memberId = (Integer) session.getAttribute("member");
        if(memberId == null){
            return "redirect:/login";
        }
        Member member = new Member();
        member.setId(memberId);
        Product product = new Product();
        product.setId(productId);
        prdTrackService.addToTrack(product, member);
        return "redirect:/prdTrack/member/" + memberId;
    }

    @GetMapping("/member/{memberId}")
    public String getPrdTrackByMemberId(HttpSession session, Model model){
        Integer memberId = (Integer) session.getAttribute("member");
        System.out.println("Session memberId: " + memberId);
        if(memberId == null){
            return "redirect:/login";
        }
        List<PrdTrack> prdTrackList = prdTrackService.getPrdTrackByMemberId(memberId);
        if (prdTrackList.size() == 0){
            model.addAttribute("empty", true);
        } else {
            model.addAttribute("empty", false);
            model.addAttribute("prdTrackList", prdTrackList);
        }

        prdTrackList.forEach((item -> {
            setProductsImg(item.getProduct());
        }));
        return "/product/prdTrack";
    }

    public void setProductsImg(Product products) {
        // 获取图片列表并处理可能为空的情况
        List<PrdImg> prdImgs = prdImgRepository.getPrdImgByProductId(products.getId());
        if (prdImgs != null && !prdImgs.isEmpty()) {
            byte[] prdImgData = prdImgs.get(0).getImageFile();
            if (prdImgData != null) {
                String base64Img = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(prdImgData);
                products.setImg(base64Img); // 假设 `img` 字段已更改为 `String`
            }
        }
    }

    @PostMapping("/remove")
    public String removeTrackItem(
            @RequestParam Integer productId,
            HttpSession session) {
        Integer memberId = (Integer) session.getAttribute("member");
        if (memberId == null) {
            return "redirect:/login";
        }

        Member member = new Member();
        member.setId(memberId);

        Product product = new Product();
        product.setId(productId);

        prdTrackService.removeProductFromTrack(member, product);

        return "redirect:/prdTrack/member/" + memberId;
    }

    @PostMapping("/clear")
    public String clearTrack(HttpSession session) {
        Integer memberId = (Integer) session.getAttribute("member");
        if (memberId == null) {
            return "redirect:/login";
        }

        Member member = new Member();
        member.setId(memberId);

        prdTrackService.removeAllTrack(memberId);

        return "redirect:/prdTrack/member/" + memberId;
    }



}
