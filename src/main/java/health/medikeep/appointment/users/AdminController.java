package health.medikeep.appointment.users;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    
    public AdminController(UserRepository userRepository, DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/all")
    public String showAll(Model model){
        List<UserInfo> users = userRepository.showUsers();
        List<DoctorInfo> doctors = doctorRepository.showDoctors();
        model.addAttribute("users", users);
        model.addAttribute("doctors", doctors);
        
        return "admin-all";
    }

    @GetMapping("/user/{user_id}")
    public String userInfo(Model model, @PathVariable Integer user_id){
        Optional<UserInfo> user = userRepository.findById(user_id);
        model.addAttribute("user", user.get());
        return "admin-user-info";
    }

    @GetMapping("/doctor/{doctor_id}")
    public String doctorInfo(Model model, @PathVariable Integer doctor_id){
        Optional<DoctorInfo> doctor = doctorRepository.findById(doctor_id);
        model.addAttribute("doctor", doctor.get());
        return "admin-doctor-info";
    }

}
