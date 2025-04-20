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
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }
    
    @GetMapping("/")
    List<DoctorInfo> showdoctors() {
        return doctorRepository.showDoctors();
    }
    
    @GetMapping("/{id}")
    DoctorInfo findById(@PathVariable Integer id) {
        
        Optional<DoctorInfo> doctor =  doctorRepository.findById(id);
        if(doctor.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "doctor not found");
        }
        
        return doctor.get();
    }

    //Need: Put an error handling in case its null. 
    @GetMapping("/info")
    DoctorInfo findById(HttpSession session) {
        String email = (String) session.getAttribute("email");

        if (email == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No doctor logged in");
        }

        Integer doctor_id = doctorRepository.getUid(email).orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "doctor ID not found"));

        return doctorRepository.findById(doctor_id).orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "doctor not found"));
        
        
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public String create(@RequestBody DoctorInfo doctor) {
        doctorRepository.create(doctor);

        return "doctor successfully created";
    }

    @PutMapping("/")
    public ResponseEntity<?> editUser(@RequestBody DoctorInfo doctor){
        if(doctorRepository.update(doctor) == true){
            return ResponseEntity.ok("User Updated Sucessfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Updated Unsucessfully");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> verify_credentials(@RequestBody DoctorInfo doctor, HttpSession session) {
        if (doctorRepository.verify(doctor.email(), doctor.pass())){
            session.setAttribute("email", doctor.email());
            return ResponseEntity.ok("Login successful!"); 
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete_doctor(@RequestBody DoctorInfo doctor){
        if(doctorRepository.delete(doctor.doctor_id()) == 1){
            return ResponseEntity.ok("doctor with id " + doctor.doctor_id() + " was deleted");
        } else {
            return ResponseEntity.badRequest().body("Cannot delete doctor");
        }

    }

    

    
    
}
