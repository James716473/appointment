package health.medikeep.appointment.users;

import java.util.List;
import java.util.Optional;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping("/")
    List<UserInfo> findAll() {
        return userRepository.findAll();
    }
    
    @GetMapping("/{id}")
    UserInfo findById(@PathVariable Integer id) {
        
        Optional<UserInfo> user =  userRepository.findById(id);
        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public String create(@RequestBody UserInfo user) {
        userRepository.create(user);

        return "user successfully created";
    }

    @PostMapping("/login")
    public String verify_credentials(@RequestBody UserInfo user) {
        boolean isValid = userRepository.verify(user.email(), user.password());
        if (isValid) {
            return "Login successful, welcome " + ((userRepository.roleByEmail(user.email()).equals("Doctor")) ? "Dr. " : "") + userRepository.findNameByEmail(user.email()) + "!";
        } else {
            return "Invalid email or password";
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@RequestBody UserInfo user, @PathVariable Integer id) {
        userRepository.update(user, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        userRepository.delete(id);
    }

    
    
}
