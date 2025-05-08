package health.medikeep.appointment.users;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final AffiliateRepository affiliateRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillingRepository billingRepository;

    public AdminController(UserRepository userRepository, DoctorRepository doctorRepository, AffiliateRepository affiliateRepository, AppointmentRepository appointmentRepository, BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.affiliateRepository = affiliateRepository;
    }

    @PostMapping("/login")
    public String login(@RequestParam String password, HttpSession session){
        System.out.println(password);
        if(password.equals("root")){
            session.setAttribute("role", "admin");
            System.out.println("Session role: " + session.getAttribute("role"));

            return "/admin/admin-home";
            
        } else {
            System.out.println("Session role: " + session.getAttribute("role"));

            return "/admin/";
        }
        
    }
    
    @GetMapping("/")
    public String login(HttpSession session){
        System.out.println("Session role: " + session.getAttribute("role"));

        if(session.getAttribute("role") != null && session.getAttribute("role").equals("admin")){
            return "/admin/admin-home";
        } else {
            return "/admin/admin-login"; 
        }
        

    }

    @GetMapping("/all") // Changed the HTML for user and doctor
    public String showAll(Model model, HttpSession session){
        if(session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")){
            return "/admin/admin-login";
        } 
        List<UserInfo> users = userRepository.showUsers();
        List<DoctorInfo> doctors = doctorRepository.showDoctors();
        
        model.addAttribute("users", users);
        model.addAttribute("doctors", doctors);
        
        return "/admin/admin-all";
    }

    @GetMapping("/doctors")
    public String showDoctoctors(Model model, HttpSession session){
        if(session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")){
            return "/admin/admin-login";
        }
        List<DoctorInfo> doctors = doctorRepository.showDoctors();
        model.addAttribute("doctors", doctors);
        return "/admin/admin-doctor";
    }

    @GetMapping("/doctor/{doctor_id}")
    public String showDoctor(@PathVariable Integer doctor_id, Model model, HttpSession session){
        if(session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")){
            return "/admin/admin-login";
        }
        DoctorInfo doctor = doctorRepository.findById(doctor_id).orElseThrow(() -> new RuntimeException("Doctor not found"));
        model.addAttribute("doctor", doctor);
        return "/admin/admin-doctor-info";
    }

    @GetMapping("/user/{user_id}")
    public String showUser(@PathVariable Integer user_id, Model model, HttpSession session){
        if(session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")){
            return "/admin/admin-login";
        }
        UserInfo user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "/admin/admin-user-info";
    }

    @GetMapping("/users")
    public String showUsers(Model model, HttpSession session){
        if(session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")){
            return "/admin/admin-login";
        }
        List<UserInfo> users = userRepository.showUsers();
        model.addAttribute("users", users);
        return "/admin/admin-user";
    }

    

    @GetMapping("/affiliates")
    public String showAffiliates(Model model, HttpSession session){
        if(session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")){
            return "/admin/admin-login";
        }
        List<AffiliateInfo> affiliates = affiliateRepository.showAffiliates();
        model.addAttribute("affiliates", affiliates);
        return "/admin/admin-affiliates";
    }

    @GetMapping("/affiliates/create")
    public String createAffiliates(HttpSession session){
        if(session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")){
            return "/admin/admin-login";
        }
        return "/admin/admin-affiliate-create";
    }

    @GetMapping("/appointments")
    public String showAppointments(Model model, HttpSession session){
        if(session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")){
            return "/admin/admin-login";
        }
        List<AppointmentInfo> appointments = appointmentRepository.showAppointments();
        model.addAttribute("appointments", appointments);
        return "/admin/admin-all-appointments";
    }

    @GetMapping("/billings")
    public String showBillings(Model model, HttpSession session){
        if(session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")){
            return "/admin/admin-login";
        }
        List<BillingInfo> billings = billingRepository.showBillings();
        model.addAttribute("billings", billings);
        return "/admin/admin-billings";
    }
}
