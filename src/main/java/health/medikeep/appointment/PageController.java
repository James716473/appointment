package health.medikeep.appointment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
<<<<<<< HEAD:src/main/java/health/medikeep/appointment/PageController.java
=======

    @GetMapping("/")
    public String landingPage() {
        return "landing";
    }

>>>>>>> test-branch:src/main/java/health/medikeep/appointment/users/PageController.java
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/login")  
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String landing() {
        return "landing";
    }
}
