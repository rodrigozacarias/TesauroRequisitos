package com.requirementsthesauri;

import com.requirementsthesauri.service.Authentication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class  Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        Authentication authentication = new Authentication();
        authentication.getConnectionDataBase();
    }

}