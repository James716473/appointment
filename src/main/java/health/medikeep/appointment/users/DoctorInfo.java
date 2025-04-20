package health.medikeep.appointment.users;

import java.time.LocalTime;
import java.time.LocalDate;

public record DoctorInfo(Integer doctor_id, String first_name, String middle_name, String last_name, 
                        String email, String pass, Sex sex, LocalDate birth_date, String contact_number,
                        String specialty, String affiliation, LocalTime schedule_from, LocalTime schedule_to) {

}
