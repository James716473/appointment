package health.medikeep.appointment.users;

import java.time.LocalDate;

public record AppointmentInfo(Integer appointment_id, Integer user_id, Integer doctor_id, LocalDate appointment_date, String appointment_type, Integer billing_id, String description) {

}
