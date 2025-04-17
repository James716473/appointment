package health.medikeep.appointment.users;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import org.springframework.jdbc.core.simple.JdbcClient;




@Repository
public class UserRepository {
    private final JdbcClient jdbcClient;

    public UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<UserInfo> findAll(){
        return jdbcClient.sql("select * from users")
            .query(UserInfo.class)
            .list();
    }

    public Optional<UserInfo> findById(Integer id){
        return jdbcClient.sql("select * from users where id = :id")
            .param("id", id)
            .query(UserInfo.class)  
            .optional();
    }

    public Optional<UserInfo> showUsers(){
        return jdbcClient.sql("select * from users where role = :role")
        .param("role", "User")
        .query(UserInfo.class)
        .optional();
        
    }

    public Integer getUid(String email){
        return jdbcClient.sql("select id from users where email = :email")
            .param("email", email)
            .query(Integer.class)
            .single();
    }

    public String roleByEmail(String email){
        return jdbcClient.sql("select role from users where email = :email")
            .param("email", email)
            .query(String.class)
            .single();
    }

    public Optional<UserInfo> showDoctors(){
        return jdbcClient.sql("select * from users where role = :role")
        .param("role", "Doctor")
        .query(UserInfo.class)
        .optional();
        
    }

    public String findNameByEmail(String email){
        return jdbcClient.sql("select first_name from users where email = :email")
            .param("email", email)
            .query(String.class)
            .single();           
    }

    public void create(UserInfo user){
        var updated = jdbcClient.sql("INSERT INTO USERS(last_name, first_name, middle_name, email, password, sex, birth_date, role) values(?, ?, ?, ?, ?, ?, ?, ?)")
            .params(List.of(user.last_name(), user.first_name(), user.middle_name(), user.email(), user.password(), user.sex().name(), user.birth_date(), user.role().name()))
            .update();
        
        Assert.state(updated == 1, "Failed to insert user id " + user.id());
    }

    public void update(UserInfo user, Integer id){
        var updated = jdbcClient.sql("UPDATE USERS SET last_name = ?, first_name = ?, middle_name = ?, email = ?, password = ?, sex = ?, birth_date = ? WHERE id = ?")
            .params(List.of(user.last_name(), user.first_name(), user.middle_name(), user.email(), user.password(), user.sex(), user.birth_date(), id))
            .update();
        
        Assert.state(updated == 1, "Failed to update user id " + id);
    }

    public int delete(Integer id){
        var updated = jdbcClient.sql("DELETE FROM USERS WHERE id = :id")
            .param("id", id)
            .update();
        
        return updated;
    }

    public boolean verify(String email, String password){
        return jdbcClient.sql("SELECT COUNT(*) FROM users WHERE email = ? AND password = ?")
            .params(List.of(email, password))
            .query(Integer.class)
            .single() > 0;
    }

    public int count(){
        return jdbcClient.sql("SELECT COUNT(*) FROM users").query(Integer.class).single();

    }

    public void saveAll(List<UserInfo> users){
        users.stream().forEach(this::create);
    }

    

   

    
}
