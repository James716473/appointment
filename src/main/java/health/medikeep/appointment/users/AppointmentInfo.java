package health.medikeep.appointment.users;


import java.util.Date;

import java.sql.Time;

public record AppointmentInfo(Integer appointment_id, Date appointment_date, Time appointment_time, Integer doctor_id, Integer user_id) {
    
}
