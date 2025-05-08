package health.medikeep.appointment.users;

import java.time.LocalTime;
import java.util.ArrayList;
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
        Optional<Integer> doctor_id = doctorRepository.verify(doctor.email(), doctor.pass());
        if (doctor_id.isPresent()){
            session.setAttribute("doctor_id", doctor_id.get());
            session.setAttribute("email", doctor.email());
            session.setAttribute("role", "doctor");
            return ResponseEntity.ok(doctor_id.get()); 
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/get-doctor-sched/{doctor_id}")
    public List<LocalTime> getDoctorSched(@PathVariable Integer doctor_id) {
        DoctorInfo doctor = doctorRepository.findById(doctor_id).orElse(null);
        LocalTime index = doctor.schedule_from();
        List<LocalTime> schedule = new ArrayList<>();
        
        while(index.isBefore(doctor.schedule_to())){
            schedule.add(index);
            index = index.plusMinutes(60);
            
        }
        return schedule;
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

    @PostMapping("/get-available-times")
    public List<LocalTime> getAvailableTimes(@RequestBody AppointmentInfo appointment) {
        List<LocalTime> scheduledTimes = doctorRepository.getScheduledTimes(appointment.doctor_id(), appointment.appointment_date());
        DoctorInfo doctor = doctorRepository.findById(appointment.doctor_id()).orElse(null);
        LocalTime index = doctor.schedule_from();
        List<LocalTime> availableTimes = new ArrayList<>();
        while(index.isBefore(doctor.schedule_to())){
            if(!scheduledTimes.contains(index)){
                availableTimes.add(index);
            }
            index = index.plusMinutes(60);
        }
        return availableTimes;
    }
    

    
    
}
