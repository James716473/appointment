package health.medikeep.appointment.users;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class PageController {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final AffiliateRepository affiliateRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillingRepository billingRepository;
    private final MessageRepository messageRepository;

    public PageController(UserRepository userRepository, DoctorRepository doctorRepository, AffiliateRepository affiliateRepository, AppointmentRepository appointmentRepository, BillingRepository billingRepository, MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
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

    @GetMapping("/user")
    public String userPage(Model model, HttpSession session) {
        Integer user_id = (Integer) session.getAttribute("user_id");
        if (user_id == null || (String) session.getAttribute("role") != "user") {
            return "no-rights";
        }
        Optional<UserInfo> user = userRepository.findById(user_id);
        model.addAttribute("appointment_doctor_infos", appointmentRepository.appointmentDoctorInfo(user_id));
        model.addAttribute("appointments", appointmentRepository.findByUserId(user_id));
        model.addAttribute("user", user.get());
        return "user-home";
    }

    @GetMapping("user/book-appointment")
    public String bookAppointment(Model model, HttpSession session) {
        Integer user_id = (Integer) session.getAttribute("user_id");
        if (user_id == null || (String) session.getAttribute("role") != "user") {
            return "no-rights";
        }
        model.addAttribute("doctors", doctorRepository.showDoctors());
        model.addAttribute("affiliates", affiliateRepository.showAffiliates());
        model.addAttribute("specialties", doctorRepository.showSpecialty());

        
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

    @GetMapping("/user/info")
    public String userInfo(Model model, HttpSession session) {

        Integer user_id = (Integer) session.getAttribute("user_id");
        if (user_id == null || (String) session.getAttribute("role") != "user") {
            return "no-rights";
        }
        Optional<UserInfo> user = userRepository.findById(user_id);
        model.addAttribute("user", user.get());
        return "admin-user-info";
    }

    @GetMapping("/user/messages")
    public String userMessages(Model model, HttpSession session) {
        Integer user_id = (Integer) session.getAttribute("user_id");
        if (user_id == null || (String) session.getAttribute("role") != "user") {
            return "no-rights";
        }
        List<MessageInfo> messages = messageRepository.showUserMessages(user_id);
        List<DoctorInfo> doctors = appointmentRepository.showDoctor(user_id);

        for(DoctorInfo doctor: doctors) {
            System.out.println(doctor.first_name() + " " + doctor.middle_name() + " " + doctor.last_name());
        }
        model.addAttribute("id", user_id);
        model.addAttribute("role", (String) session.getAttribute("role"));
        model.addAttribute("doctors", doctors);
        model.addAttribute("messages", messages);
        return "messages";
    }

    @GetMapping("/user/messages/create")
    public String createMessage(Model model, HttpSession session) {
        Integer user_id = (Integer) session.getAttribute("user_id");
        if (user_id == null || (String) session.getAttribute("role") != "user") {
            return "no-rights";
        }
        List<DoctorInfo> doctors = doctorRepository.showDoctors();
        model.addAttribute("recievers", doctors);
        return "create-message";
    }

    @GetMapping("/doctor/info")
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

    @GetMapping("/doctor/messages")
    public String doctorMessages(Model model, HttpSession session) {
        Integer doctor_id = (Integer) session.getAttribute("doctor_id");
        if (doctor_id == null || (String) session.getAttribute("role") != "doctor") {
            return "no-rights";
        }
        List<MessageInfo> messages = messageRepository.showDoctorMessages(doctor_id);
        List<UserInfo> users = appointmentRepository.showUser(doctor_id);

        model.addAttribute("id", doctor_id);
        model.addAttribute("role", (String) session.getAttribute("role"));
        model.addAttribute("users", users);
        model.addAttribute("messages", messages);
        return "messages";

       
        
    }

    @GetMapping("/doctor/messages/create")
    public String createDoctorMessage(Model model, HttpSession session) {
        Integer doctor_id = (Integer) session.getAttribute("doctor_id");
        if (doctor_id == null || (String) session.getAttribute("role") != "doctor") {
            return "no-rights";
        }
        List<UserInfo> users = userRepository.showUsers();
        model.addAttribute("recievers", users);
        return "create-message";
    }

    
}
