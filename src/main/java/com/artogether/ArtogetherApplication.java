package com.artogether;

import com.artogether.common.member.MemberService;
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
    CommandLineRunner runner(MemberService service){
        return args -> {

//            System.out.println(service.findAll().size());
        };
    }

}
