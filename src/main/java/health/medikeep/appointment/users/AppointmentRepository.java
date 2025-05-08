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

    public boolean update(AppointmentInfo appointment) {
        return jdbcClient.sql("UPDATE APPOINTMENTS SET appointment_date = ?, appointment_time = ? WHERE appointment_id = ?")
            .params(List.of(appointment.appointment_date(), appointment.appointment_time(), appointment.appointment_id()))
            .update() == 1;    
    }
    

    public Optional<AppointmentInfo> findById(Integer appointment_id) {
        return jdbcClient.sql("SELECT * FROM APPOINTMENTS WHERE appointment_id = :appointment_id")
            .param("appointment_id", appointment_id)
            .query(AppointmentInfo.class)
            .optional();
    }

    public List<AppointmentInfo> findByUserId(Integer user_id) {
        return jdbcClient.sql("SELECT * FROM APPOINTMENTS WHERE user_id = :user_id AND (appointment_date > CURDATE() OR (appointment_date=CURDATE() AND appointment_time > CURTIME())) ORDER BY appointment_date ASC, appointment_time ASC")
            .param("user_id", user_id)
            .query(AppointmentInfo.class)
            .list();
    }

    public List<AppointmentInfo> findPastByUserId(Integer user_id) {
        return jdbcClient.sql("SELECT * FROM APPOINTMENTS WHERE user_id = :user_id AND (appointment_date < CURDATE() OR (appointment_date=CURDATE() AND appointment_time < CURTIME())) ORDER BY appointment_date DESC, appointment_time DESC")
            .param("user_id", user_id)
            .query(AppointmentInfo.class)
            .list();
    }

    public List<AppointmentInfo> findByDoctorId(Integer doctor_id) {
        return jdbcClient.sql("SELECT * FROM APPOINTMENTS WHERE doctor_id = :doctor_id AND (appointment_date > CURDATE() OR (appointment_date=CURDATE() AND appointment_time > CURTIME())) ORDER BY appointment_date ASC, appointment_time ASC")
            .param("doctor_id", doctor_id)
            .query(AppointmentInfo.class)
            .list();
    }

    public List<AppointmentInfo> findPastByDoctorId(Integer doctor_id) {
        return jdbcClient.sql("SELECT * FROM APPOINTMENTS WHERE doctor_id = :doctor_id AND (appointment_date < CURDATE() OR (appointment_date=CURDATE() AND appointment_time < CURTIME())) ORDER BY appointment_date DESC, appointment_time DESC")
            .param("doctor_id", doctor_id)
            .query(AppointmentInfo.class)
            .list();
    }

    public List<DoctorInfo> showDoctor(Integer user_id){
        return jdbcClient.sql("SELECT distinct doctors.* FROM doctors INNER JOIN appointments on appointments.doctor_id=doctors.doctor_id WHERE appointments.user_id=:user_id")
            .param("user_id", user_id)
            .query(DoctorInfo.class)
            .list();
    }

    public List<UserInfo> showUser(Integer doctor_id){
        return jdbcClient.sql("SELECT distinct users.* FROM users INNER JOIN appointments on appointments.user_id=users.user_id WHERE appointments.doctor_id=:doctor_id")
            .param("doctor_id", doctor_id)
            .query(UserInfo.class)
            .list();
    }

    public List<DoctorInfo> appointmentDoctorInfo(Integer user_id){
        return jdbcClient.sql("SELECT doctors.* FROM doctors INNER JOIN appointments on appointments.doctor_id=doctors.doctor_id WHERE appointments.user_id=:user_id AND (appointment_date > CURDATE() OR (appointment_date=CURDATE() AND appointment_time > CURTIME())) ORDER BY appointments.appointment_date ASC, appointments.appointment_time ASC")
            .param("user_id", user_id)
            .query(DoctorInfo.class)
            .list();
    }

    public List<UserInfo> appointmentUserInfo(Integer doctor_id){
        return jdbcClient.sql("SELECT users.* FROM users INNER JOIN appointments on appointments.user_id=users.user_id WHERE appointments.doctor_id=:doctor_id AND (appointment_date > CURDATE() OR (appointment_date=CURDATE() AND appointment_time > CURTIME())) ORDER BY appointments.appointment_date ASC, appointments.appointment_time ASC")
            .param("doctor_id", doctor_id)
            .query(UserInfo.class)
            .list();
    }
    
    
}
