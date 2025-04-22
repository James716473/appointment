package health.medikeep.appointment.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentRepository appointmentRepository;

    public AppointmentController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/")
    public List<AppointmentInfo> showAppointments() {
        return appointmentRepository.showAppointments();
    }

    
    @PostMapping("/")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentInfo appointment) {
        if (appointmentRepository.create(appointment)) {
            return ResponseEntity.ok("Appointment Created Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment Creation Unsuccessful");
        }
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteAppointment(@RequestBody AppointmentInfo appointment) {
        if (appointmentRepository.delete(appointment.appointment_id())) {
            return ResponseEntity.ok("Appointment Deleted Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment Deletion Unsuccessful");
        }
    }

}
