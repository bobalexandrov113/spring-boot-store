package com.cba.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Value("${spring.application.name}")
    String appName;
    @GetMapping("/")
    public String index()
        {
            System.out.println(appName);

            return "index.html";
        }
    @GetMapping("/bobo")
     public String bobo()
        {
            System.out.println("bobo");
            return "<html><body>hey</body></html>";
        }


}
