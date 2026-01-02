package com.cba.store.admin;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/com/cba/store/admin")
public class AdminController {

    @GetMapping("/hello")
    public String hello(){
        return "hello admin";
    }

}
