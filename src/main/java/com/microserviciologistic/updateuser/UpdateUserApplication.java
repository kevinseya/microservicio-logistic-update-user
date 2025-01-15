package com.microserviciologistic.updateuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class UpdateUserApplication {

    public static void main(String[] args) {

        SpringApplication.run(UpdateUserApplication.class, args);
    }

    @GetMapping("/")
    public String redirectToSwagger() {
        return "redirect:/swagger-ui.html";
    }


}
