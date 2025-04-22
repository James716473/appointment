package health.medikeep.appointment.users;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class PageController {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final AffiliateRepository affiliateRepository;

    public PageController(UserRepository userRepository, DoctorRepository doctorRepository, AffiliateRepository affiliateRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.affiliateRepository = affiliateRepository;
    }

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

    @GetMapping("/book-appointment")
    public String bookAppointment(Model model, HttpSession session) {
        model.addAttribute("doctors", doctorRepository.showDoctors());
        model.addAttribute("affiliates", affiliateRepository.showAffiliates());
        model.addAttribute("specialties", doctorRepository.showSpecialty());

        String email = (String) session.getAttribute("email");
        Integer user_id = userRepository.getUid(email).orElse(null);
        model.addAttribute("user", userRepository.findById(user_id).orElse(null));

        
        return "book-appointment";
    }
    
}
