package health.medikeep.appointment.users;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/all") // Changed the HTML for user and doctor
    public String showAll(Model model){
        List<UserInfo> users = userRepository.showUsers();
        List<DoctorInfo> doctors = doctorRepository.showDoctors();
        
        model.addAttribute("users", users);
        model.addAttribute("doctors", doctors);
        
        return "/admin/admin-all";
    }

    @GetMapping("/doctors")
    public String showDoctoctors(Model model){
        List<DoctorInfo> doctors = doctorRepository.showDoctors();
        model.addAttribute("doctors", doctors);
        return "/admin/admin-doctor";
    }

    @GetMapping("/users")
    public String showUsers(Model model){
        List<UserInfo> users = userRepository.showUsers();
        model.addAttribute("users", users);
        return "/admin/admin-user";
    }

    

    @GetMapping("/affiliates")
    public String showAffiliates(Model model){
        List<AffiliateInfo> affiliates = affiliateRepository.showAffiliates();
        model.addAttribute("affiliates", affiliates);
        return "/admin/admin-affiliates";
    }

    @GetMapping("/affiliates/create")
    public String createAffiliates(){
        return "/admin/admin-affiliate-create";
    }

    @GetMapping("/appointments")
    public String showAppointments(Model model){
        List<AppointmentInfo> appointments = appointmentRepository.showAppointments();
        model.addAttribute("appointments", appointments);
        return "/admin/admin-all-appointments";
    }

    @GetMapping("/billings")
    public String showBillings(Model model){
        List<BillingInfo> billings = billingRepository.showBillings();
        model.addAttribute("billings", billings);
        return "/admin/admin-billings";
    }
}
