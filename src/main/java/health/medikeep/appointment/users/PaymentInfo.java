package health.medikeep.appointment.users;

import java.sql.Date;

public record PaymentInfo(Integer payment_id, double amount, String type, Date payment_date, Integer user_id, Integer doctor_id, Integer appointment_id) {

}
