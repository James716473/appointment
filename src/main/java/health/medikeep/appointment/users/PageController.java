package health.medikeep.appointment.users;

import java.lang.foreign.Linker.Option;
import java.util.ArrayList;
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
        return "/landing-signup-login/landing";
    }

    
    @GetMapping("/signup")
    public String signup() {
        return "/landing-signup-login/signup";
    }

    @GetMapping("/login")  
    public String login() {
        return "/landing-signup-login/login";
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
        if (user_id == null || (String) session.getAttribute("role") != "user") {
            return "no-rights";
        }
        List<AppointmentInfo> appointments = appointmentRepository.findByUserId(user_id);
        List<AppointmentInfo> pastAppointments = appointmentRepository.findPastByUserId(user_id);
        List<BillingInfo> billings = new ArrayList<>();
        List<BillingInfo> pastBillings = new ArrayList<>();
        List<DoctorInfo> pastAppointmentDoctors = new ArrayList<>();

        for(AppointmentInfo appointment: pastAppointments) {
            Optional<DoctorInfo> pastDoctor = doctorRepository.findById(appointment.doctor_id());
            if (pastDoctor.isEmpty()) {
                
                continue; // Skip this iteration if doctor is not found
            }
            pastBillings.add(billingRepository.findByAppointmentId(appointment.appointment_id()));
            pastAppointmentDoctors.add(pastDoctor.get());

        }
        for(AppointmentInfo appointment: appointments) {
            billings.add(billingRepository.findByAppointmentId(appointment.appointment_id()));

        }
        

        model.addAttribute("past_appointments", pastAppointments);
        model.addAttribute("past_billings", pastBillings);
        model.addAttribute("past_appointment_doctors", pastAppointmentDoctors);



        model.addAttribute("billings", billings);
        model.addAttribute("doctors", appointmentRepository.appointmentDoctorInfo(user_id));
        model.addAttribute("user", userRepository.findById(user_id).orElse(null));
        model.addAttribute("appointments", appointments);
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
        return "/admin/admin-user-info";
    }

    @GetMapping("/user/messages")
    public String userMessages(Model model, HttpSession session) {
        Integer user_id = (Integer) session.getAttribute("user_id");
        if (user_id == null || (String) session.getAttribute("role") != "user") {
            return "no-rights";
        }
        List<MessageInfo> messages = messageRepository.showUserMessages(user_id); // kasama dito ung mga deleted doctor which magcacause ng error
        List<MessageInfo> validMessages = new ArrayList<>(); //dito mafifilter ung mga message na merong nag eexist na doctor 
        

        for(MessageInfo message: messages) {
            Optional<DoctorInfo> doctor = doctorRepository.findById(message.receiver_id());
            if (doctor.isPresent()) {
                validMessages.add(message);
            }
        }

        List<DoctorInfo> doctors = appointmentRepository.showDoctor(user_id);
     
        
        for(DoctorInfo doctor: doctors) {
            System.out.println(doctor.first_name() + " " + doctor.middle_name() + " " + doctor.last_name());
        }
        model.addAttribute("id", user_id);
        model.addAttribute("role", (String) session.getAttribute("role"));
        model.addAttribute("doctors", doctors);
        model.addAttribute("messages", messages);
        System.out.println(doctors);
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

    @GetMapping("/doctor")
    public String doctorPage(Model model, HttpSession session) {
        Integer doctor_id = (Integer) session.getAttribute("doctor_id");
        if (doctor_id == null || (String) session.getAttribute("role") != "doctor") {
            return "no-rights";
        }

        
        Optional<DoctorInfo> doctor = doctorRepository.findById(doctor_id);
        model.addAttribute("appointment_user_infos", appointmentRepository.appointmentUserInfo(doctor_id));
        model.addAttribute("appointments", appointmentRepository.findByDoctorId(doctor_id));
        model.addAttribute("doctor", doctor.get());
        return "doctor-home";
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

        return "admin/admin-doctor-info";
    }

    @GetMapping("doctor/appointment")
    public String doctorAppointment(Model model, HttpSession session) {
        Integer doctor_id = (Integer) session.getAttribute("doctor_id");
        if (doctor_id == null || (String) session.getAttribute("role") != "doctor") {
            return "no-rights";
        }
        Optional<DoctorInfo> doctor = doctorRepository.findById(doctor_id);
        List<AppointmentInfo> appointments = appointmentRepository.findByDoctorId(doctor_id);
        List<AppointmentInfo> pastAppointments = appointmentRepository.findPastByDoctorId(doctor_id);
        List<BillingInfo> billings = new ArrayList<>();
        List<BillingInfo> pastBillings = new ArrayList<>();
        List<UserInfo> pastAppointmentUsers = new ArrayList<>();
        for(AppointmentInfo appointment: appointments) {
            billings.add(billingRepository.findByAppointmentId(appointment.appointment_id()));

        
        }
        for(AppointmentInfo appointment: pastAppointments) {
            Optional<UserInfo> pastUser = userRepository.findById(appointment.user_id());
            if (pastUser.isEmpty()) {
                
                continue; // Skip this iteration if user is not found
            }
            pastBillings.add(billingRepository.findByAppointmentId(appointment.appointment_id()));
            pastAppointmentUsers.add(pastUser.get());

        }
        System.out.println(pastAppointmentUsers);
        

        model.addAttribute("past_appointments", pastAppointments);
        model.addAttribute("past_billings", pastBillings);
        model.addAttribute("past_appointment_users", pastAppointmentUsers);

        model.addAttribute("billings", billings);
        model.addAttribute("users", appointmentRepository.appointmentUserInfo(doctor_id));
        model.addAttribute("doctor", doctor.get());
        model.addAttribute("appointments", appointments);
        return "doctor-appointment";
    }

    @GetMapping("/doctor/messages")
    public String doctorMessages(Model model, HttpSession session) {
        Integer doctor_id = (Integer) session.getAttribute("doctor_id");
        if (doctor_id == null || (String) session.getAttribute("role") != "doctor") {
            return "no-rights";
        }
        List<MessageInfo> messages = messageRepository.showUserMessages(doctor_id); // kasama dito ung mga deleted doctor which magcacause ng error
        List<MessageInfo> validMessages = new ArrayList<>(); //dito mafifilter ung mga message na merong nag eexist na doctor 
        

        for(MessageInfo message: messages) {
            Optional<UserInfo> doctor = userRepository.findById(message.receiver_id());
            if (doctor.isPresent()) {
                validMessages.add(message);
            }
        }
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
