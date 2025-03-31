package health.medikeep.appointment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String landingPage() {
        return "landing";
    }

    
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/login")  
    public String login() {
        return "login";
    }

    
}
