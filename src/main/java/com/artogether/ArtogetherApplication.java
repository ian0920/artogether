package com.artogether;

import com.artogether.event.my_evt_coup.MyEvtCoupService;
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
    CommandLineRunner runner(MyEvtCoupService service){
        return args -> {

//            service.getMyEvtCoupsByMemberIdAndEventId(1,1)
//                    .forEach(coup -> System.out.println(coup.getEvtCoup().getEvtCoupName() + "" + coup.getStatus())
//            );
        };
    }

}
