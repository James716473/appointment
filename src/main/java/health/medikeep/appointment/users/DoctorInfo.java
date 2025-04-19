package health.medikeep.appointment.users;

import java.sql.Time;
import java.time.LocalDate;

public record DoctorInfo(Integer doctor_id, String first_name, String middle_name, String last_name, 
                        String email, String pass, Sex sex, LocalDate birth_date, String contact_number,
                        String specialty, String affiliation, Time schedule_from, Time schedule_to) {

}
