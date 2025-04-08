package health.medikeep.appointment.users;

import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class AppointmentRepository {
    private final JdbcClient jdbcClient;
    public AppointmentRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public void create(AppointmentInfo appointmentInfo){
        var updated = jdbcClient.sql("insert into appointments (appointment_date, appointment_time, doctor_id, user_id) values (?, ?, ?, ?)")
        .params(List.of(appointmentInfo.appointment_date(), appointmentInfo.appointment_time(), appointmentInfo.doctor_id(), appointmentInfo.user_id()))
        .update();

        Assert.state(updated == 1, "Appointment creation failed");
        
    }

}
