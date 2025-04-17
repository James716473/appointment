package health.medikeep.appointment.users;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import jakarta.servlet.http.HttpSession;

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

    //Need: Put an error handling in case its null. 
    @GetMapping("/info")
    UserInfo findById(HttpSession session) {
        String email = (String) session.getAttribute("email");
        Integer id = userRepository.getUid(email);
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
    public ResponseEntity<?> verify_credentials(@RequestBody UserInfo user, HttpSession session) {
        if (userRepository.verify(user.email(), user.password())){
            session.setAttribute("email", user.email());
            return ResponseEntity.ok("Login successful!"); 
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@RequestBody UserInfo user, @PathVariable Integer id) {
        userRepository.update(user, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete_user(@RequestBody UserInfo user){
        if(userRepository.delete(user.id()) == 1){
            return ResponseEntity.ok("User with id " + user.id() + " was deleted");
        } else {
            return ResponseEntity.badRequest().body("Cannot delete user");
        }

    }

    

    
    
}
