package com.cba.store.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Value("${env}")
    private String env;

    @Value("${baseUrl}")
    private String baseUrl;

    @GetMapping("/")
    public String home(Model model)
    {
       String cssUrl = baseUrl + "/css/";
       String jsUrl = baseUrl + "/js/";

       String theme = "light";
       String company = "cba";

       String bundle = "bundle";
       String endpoints = "endpoints";

       //CSS files
        model.addAttribute("cssFileName1", cssUrl + theme + ".css");
        model.addAttribute("cssFileName2", cssUrl + company + ".css");

        //Js files
        model.addAttribute("jsFileName1", jsUrl + bundle + ".js");
        model.addAttribute("jsFileName2", jsUrl +endpoints + ".js");

        //Js variables
        model.addAttribute("baseUrl", baseUrl);

        String pageTitle = "Store Application";
        model.addAttribute("pageTitle", pageTitle);
        return "index";
    }



}
