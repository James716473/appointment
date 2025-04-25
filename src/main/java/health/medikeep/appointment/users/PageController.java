package health.medikeep.appointment.users;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class PageController {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final AffiliateRepository affiliateRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillingRepository billingRepository;

    public PageController(UserRepository userRepository, DoctorRepository doctorRepository, AffiliateRepository affiliateRepository, AppointmentRepository appointmentRepository, BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.affiliateRepository = affiliateRepository;
        this.appointmentRepository = appointmentRepository;
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

    @GetMapping("user/book-appointment")
    public String bookAppointment(Model model, HttpSession session) {
        model.addAttribute("doctors", doctorRepository.showDoctors());
        model.addAttribute("affiliates", affiliateRepository.showAffiliates());
        model.addAttribute("specialties", doctorRepository.showSpecialty());

        String email = (String) session.getAttribute("email");
        Integer user_id = userRepository.getUid(email).orElse(null);
        if(user_id == null) {
            return "no-rights";
        }
        model.addAttribute("user", userRepository.findById(user_id).orElse(null));

        
        return "book-appointment";
    }

    @GetMapping("user/appointment")
    public String userAppointment(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        Integer user_id = userRepository.getUid(email).orElse(null);
        model.addAttribute("user", userRepository.findById(user_id).orElse(null));
        model.addAttribute("appointments", appointmentRepository.findByUserId(user_id));
        return "user-appointment";
    }

    @GetMapping("/user-info")
    public String userInfo(Model model, HttpSession session) {

        Integer user_id = (Integer) session.getAttribute("user_id");
        if (user_id == null || (String) session.getAttribute("role") != "user") {
            return "no-rights";
        }
        Optional<UserInfo> user = userRepository.findById(user_id);
        model.addAttribute("user", user.get());
        return "admin-user-info";
    }

    @GetMapping("/doctor-info")
    public String doctorInfo(Model model, HttpSession session) {
        Integer doctor_id = (Integer) session.getAttribute("doctor_id");
        if (doctor_id == null || (String) session.getAttribute("role") != "doctor") {
            return "no-rights";
        }
        Optional<DoctorInfo> doctor = doctorRepository.findById(doctor_id);
        List<AffiliateInfo> affiliates = affiliateRepository.showAffiliates();
        model.addAttribute("affiliates", affiliates);
        model.addAttribute("doctor", doctor.get());

        return "admin-doctor-info";
    }
    
}
