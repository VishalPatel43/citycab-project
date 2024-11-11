package com.springboot.project.citycab;

import com.springboot.project.citycab.services.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CitycabApplicationTests {

    @Autowired
    private EmailSenderService emailSenderService;

    @Test
    void contextLoads() {
        emailSenderService.sendEmail(
                "xeroc17176@inikale.com",
                "This is testing mail",
                "This is testing mail body"
        );
    }

    @Test
    void sendEmailMultiple() {
        String[] emails = {
                "xeroc17176@inikale.com",
//                "vishal.hpatel0403@gmail.com",
                "vishal.hpatel786@gmail.com"
        };
        emailSenderService.sendEmail(
                emails,
                "Hello from Citycab",
                "This is testing mail body"
        );
    }

}
