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

    public void create(UserInfo user){
        var updated = jdbcClient.sql("INSERT INTO USERS(name, email, password) values(?, ?, ?)")
            .params(List.of(user.name(), user.email(), user.password()))
            .update();
        
        Assert.state(updated == 1, "Failed to insert user id " + user.id());
    }

    public void update(UserInfo user, Integer id){
        var updated = jdbcClient.sql("UPDATE USERS SET name = ?, email = ?, password = ? WHERE id = ?")
            .params(List.of(user.name(), user.email(), user.password(), id))
            .update();
        
        Assert.state(updated == 1, "Failed to update user id " + id);
    }

    public void delete(Integer id){
        var updated = jdbcClient.sql("DELETE FROM USERS WHERE id = :id")
            .param("id", id)
            .update();
        
        Assert.state(updated == 1, "Failed to delete user id " + id);
    }

    public int count(){
        return jdbcClient.sql("SELECT COUNT(*) FROM users").query(Integer.class).single();

    }

    public void saveAll(List<UserInfo> users){
        users.stream().forEach(this::create);
    }

    

   

    
}
