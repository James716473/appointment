package health.medikeep.appointment.users;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentRepository {
    private final JdbcClient jdbcClient;

    public AppointmentRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public boolean create(AppointmentInfo appointment) {
        return jdbcClient.sql("INSERT INTO APPOINTMENTS(user_id, doctor_id, appointment_date, appointment_time, appointment_type, billing_id, description) values(?, ?, ?, ?, ?, ?, ?)")
            .params(List.of(appointment.user_id(), appointment.doctor_id(), appointment.appointment_date(), appointment.appointment_time(), appointment.appointment_type(), appointment.billing_id(), appointment.description()))
            .update() == 1;    
    }


    public List<AppointmentInfo> showAppointments() {
        return jdbcClient.sql("select * from appointments")
            .query(AppointmentInfo.class)
            .list();
    }

    public boolean delete(Integer appointment_id) {
        return jdbcClient.sql("DELETE FROM APPOINTMENTS WHERE appointment_id = :appointment_id")
            .param("appointment_id", appointment_id)
            .update() == 1;    
    }
    
    public Optional<AppointmentInfo> findById(Integer appointment_id) {
        return jdbcClient.sql("SELECT * FROM APPOINTMENTS WHERE appointment_id = :appointment_id")
            .param("appointment_id", appointment_id)
            .query(AppointmentInfo.class)
            .optional();
    }

    public List<AppointmentInfo> findByUserId(Integer user_id) {
        return jdbcClient.sql("SELECT * FROM APPOINTMENTS WHERE user_id = :user_id")
            .param("user_id", user_id)
            .query(AppointmentInfo.class)
            .list();
    }
}
