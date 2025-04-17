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
    
    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public String showAll(Model model){
        List<UserInfo> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin-all";
    }

    @GetMapping("/{id}")
    public String userInfo(Model model, @PathVariable Integer id){
        Optional<UserInfo> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        return "admin-user-info";
    }
}
