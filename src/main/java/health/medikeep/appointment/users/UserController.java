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
    List<UserInfo> showUsers() {
        return userRepository.showUsers();
    }
    
    @GetMapping("/{id}")
    UserInfo findById(@PathVariable Integer user_id) {
        
        Optional<UserInfo> user =  userRepository.findById(user_id);
        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        
        return user.get();
    }

    @PutMapping("/")
    public ResponseEntity<?> editUser(@RequestBody UserInfo user){
        if(userRepository.update(user) == true){
            return ResponseEntity.ok("User Updated Sucessfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Updated Unsucessfully");
        }
    }

    //Need: Put an error handling in case its null. 
    @GetMapping("/info")
    UserInfo findById(HttpSession session) {
        String email = (String) session.getAttribute("email");
        System.out.println("Session email: " + email);  // Debugging log

        if (email == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user logged in");
        }

        Integer user_id = userRepository.getUid(email).orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found"));

        return userRepository.findById(user_id).orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        

        
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public String create(@RequestBody UserInfo user) {
        userRepository.create(user);

        return "user successfully created";
    }

    @PostMapping("/login")
    public ResponseEntity<?> verify_credentials(@RequestBody UserInfo user, HttpSession session) {
        System.out.println("Verifying email: " + user.email() + " with password: " + user.pass()); // Debugging line
        Optional<Integer> user_id = userRepository.verify(user.email(), user.pass());
        if (user_id.isPresent()){
            session.setAttribute("user_id", user_id.get());
            session.setAttribute("email", user.email());
            session.setAttribute("role", "user");
            System.out.println("Session email set to: " + user.email()); // Debugging line
            return ResponseEntity.ok(user_id.get()); 
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete_user(@RequestBody UserInfo user){
        if(userRepository.delete(user.user_id()) == 1){
            return ResponseEntity.ok("User with id " + user.user_id() + " was deleted");
        } else {
            return ResponseEntity.badRequest().body("Cannot delete user");
        }

    }

    

    
    
}
