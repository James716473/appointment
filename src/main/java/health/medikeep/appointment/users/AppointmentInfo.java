package health.medikeep.appointment.users;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentInfo(Integer appointment_id, Integer user_id, Integer doctor_id, LocalDate appointment_date, LocalTime appointment_time, String appointment_type, Integer billing_id, String description) {

}
