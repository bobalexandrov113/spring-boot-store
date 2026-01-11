package com.cba.store.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Value("${env}")

    private String env;

    @GetMapping("/")
    public String home(Model model)
        {
            model.addAttribute("name", "Visitor");
        if (env.equals("production")) {
            return "index";
        }
        else if (env.equals("development")) {
            return "index_dev";
        }
        return "index";
        }



}
