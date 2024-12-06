package com.artogether;


import com.artogether.common.member.MemberService;
import com.artogether.product.cart.model.CartService;
import com.artogether.venue.venue.VenueService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com")
public class ArtogetherApplication {


    public static void main(String[] args) {
        SpringApplication.run(ArtogetherApplication.class, args);


    }

    //以下為測試用

    @Bean
    CommandLineRunner runner(CartService service){
        return args -> {
//            service.getCartByMember(1).forEach(System.out::println);
//            System.out.println();
//            System.out.println(service.getDetailVenue(1).getImgUrls());
//            System.out.println(service.getDetailVenue(1).getAvailableDays());

        };
    }

}
