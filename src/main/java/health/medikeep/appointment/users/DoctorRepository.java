package health.medikeep.appointment.users;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorRepository {
    private final JdbcClient jdbcClient;

    public DoctorRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<DoctorInfo> showDoctors(){
        return jdbcClient.sql("select * from doctors")
            .query(DoctorInfo.class)
            .list();
    }

    public List<String> showSpecialty(){
        return jdbcClient.sql("select distinct specialty from doctors")
            .query(String.class)
            .list();
    }

    public Optional<DoctorInfo> findById(Integer doctor_id){
        return jdbcClient.sql("select * from doctors where doctor_id = :doctor_id")
            .param("doctor_id", doctor_id)
            .query(DoctorInfo.class)  
            .optional();
    }

    public Optional<Integer> getUid(String email){
        return jdbcClient.sql("select doctor_id from doctors where email = :email")
            .param("email", email)
            .query(Integer.class)
            .optional();
    }

    public String findNameByEmail(String email){
        return jdbcClient.sql("select first_name from doctors where email = :email")
            .param("email", email)
            .query(String.class)
            .single();           
    }

    public int create(DoctorInfo doctor){
        var updated = jdbcClient.sql("INSERT INTO doctorS(last_name, first_name, middle_name, email, pass, sex, birth_date) values(?, ?, ?, ?, ?, ?, ?)")
            .params(List.of(doctor.last_name(), doctor.first_name(), doctor.middle_name(), doctor.email(), doctor.pass(), doctor.sex().name(), doctor.birth_date()))
            .update();
        
        return updated;
    }

    public int delete(Integer doctor_id){
        var updated = jdbcClient.sql("DELETE FROM doctorS WHERE doctor_id = :doctor_id")
            .param("doctor_id", doctor_id)
            .update();
        
        return updated;
    }

    public boolean update(DoctorInfo doctor){
        return jdbcClient.sql("UPDATE doctors SET first_name= ?, middle_name= ?, last_name= ?, email= ?, pass= ?, sex= ?, birth_date= ?, contact_number= ?, specialty= ?, affiliation= ?, schedule_from= ?, schedule_to= ? WHERE doctor_id= ?")
            .params(List.of(doctor.first_name(), doctor.middle_name(), doctor.last_name(), doctor.email(), doctor.pass(), doctor.sex().name(), doctor.birth_date(), doctor.contact_number(), doctor.specialty(), doctor.affiliation(), doctor.schedule_from(), doctor.schedule_to(), doctor.doctor_id()))
            .update() == 1; // returns 1 when it successfully updated
        
    }

    public boolean verify(String email, String pass){
        return jdbcClient.sql("SELECT COUNT(*) FROM doctors WHERE email = ? AND pass = ?")
            .params(List.of(email, pass))
            .query(Integer.class)
            .single() > 0;
    }
}
