package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {
    
    //Redirects the user to the frontpage
    @GetMapping("/")
    public String getDefault() {
        return "redirect:/news";
    }
    
}
