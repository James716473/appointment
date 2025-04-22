package health.medikeep.appointment.users;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.simple.JdbcClient;




@Repository
public class UserRepository {
    private final JdbcClient jdbcClient;

    public UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<UserInfo> showUsers(){
        return jdbcClient.sql("select * from users")
            .query(UserInfo.class)
            .list();
    }

    public Optional<UserInfo> findById(Integer user_id){
        return jdbcClient.sql("select * from users where user_id = :user_id")
            .param("user_id", user_id)
            .query(UserInfo.class)  
            .optional();
    }

    public Optional<Integer> getUid(String email){
        return jdbcClient.sql("select user_id from users where email = :email")
            .param("email", email)
            .query(Integer.class)
            .optional();
    }

    public String findNameByEmail(String email){
        return jdbcClient.sql("select first_name from users where email = :email")
            .param("email", email)
            .query(String.class)
            .single();           
    }

    public int create(UserInfo user){
        var updated = jdbcClient.sql("INSERT INTO USERS(last_name, first_name, middle_name, email, pass, sex, birth_date) values(?, ?, ?, ?, ?, ?, ?)")
            .params(List.of(user.last_name(), user.first_name(), user.middle_name(), user.email(), user.pass(), user.sex().name(), user.birth_date()))
            .update();
        
        return updated;
    }

    public int delete(Integer user_id){
        var updated = jdbcClient.sql("DELETE FROM USERS WHERE user_id = :user_id")
            .param("user_id", user_id)
            .update();
        
        return updated;
    }

    public boolean verify(String email, String pass){
        System.out.println("Verifying email: " + email + " with password: " + pass); // Debugging line
        return jdbcClient.sql("SELECT COUNT(*) FROM users WHERE email = ? AND pass = ?")
            .params(List.of(email, pass))
            .query(Integer.class)
            .single() == 1;
    }

    public boolean update(UserInfo user){
        return jdbcClient.sql("UPDATE users SET first_name= ?, middle_name= ?, last_name= ?, email= ?, pass= ?, sex= ?, birth_date= ?, contact_number= ?, medical_history= ?, allergies= ?, family_medical_history= ? WHERE user_id= ?")
            .params(List.of(user.first_name(), user.middle_name(), user.last_name(), user.email(), user.pass(), user.sex().name(), user.birth_date(), user.contact_number(), user.medical_history(), user.allergies(), user.family_medical_history(), user.user_id()))
            .update() == 1; // returns 1 when it successfully updated
        
    }   

    

   

    
}
