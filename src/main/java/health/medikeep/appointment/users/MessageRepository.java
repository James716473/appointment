package health.medikeep.appointment.users;

import java.util.List;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository {
    private final JdbcClient jdbcClient;

    public MessageRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public boolean create(MessageInfo message) {
        return jdbcClient.sql("INSERT INTO MESSAGES(sender_id, receiver_id, message, message_type) values(?, ?, ?, ?)")
            .params(List.of(message.sender_id(), message.receiver_id(), message.message(), message.message_type()))
            .update() == 1;   
    }

    public List<MessageInfo> showMessages() {
        return jdbcClient.sql("select * from messages")
            .query(MessageInfo.class)
            .list();
    }

    public List<MessageInfo> showUserSentMessage(Integer user_id) {
        return jdbcClient.sql("select * from messages where sender_id = :sender_id and message_type='u-d'")
            .param("sender_id", user_id)
            .query(MessageInfo.class)
            .list();
    }

    public List<MessageInfo> showUserRecievedMessage(Integer user_id) {
        return jdbcClient.sql("select * from messages where receiver_id = :receiver_id and message_type='d-u'")
            .param("receiver_id", user_id)
            .query(MessageInfo.class)
            .list();
    }

    public List<MessageInfo> showUserMessages(Integer user_id) {
        // This method is used to show messages between user and doctor
        return jdbcClient.sql("select * from messages where (sender_id = ? and message_type='u-d') or (receiver_id = ? and message_type='d-u') order by timestamp asc")
            .params(List.of(user_id, user_id))
            .query(MessageInfo.class)
            .list();
    }

    public List<MessageInfo> showDoctorMessages(Integer doctor_id) {
        // This method is used to show messages between doctor and user
        return jdbcClient.sql("select * from messages where (sender_id = ? and message_type='d-u') or (receiver_id = ? and message_type='u-d') order by timestamp asc")
            .params(List.of(doctor_id, doctor_id))
            .query(MessageInfo.class)
            .list();
    }

    public List<MessageInfo> showDoctorSentMessage(Integer doctor_id) {
        return jdbcClient.sql("select * from messages where sender_id = :sender_id and message_type='d-u'")
            .param("sender_id", doctor_id)
            .query(MessageInfo.class)
            .list();
    }

    public List<MessageInfo> showDoctorRecievedMessage(Integer doctor_id) {
        return jdbcClient.sql("select * from messages where receiver_id = :receiver_id and message_type='u-d'")
            .param("reciever_id", doctor_id)
            .query(MessageInfo.class)
            .list();
    }

}
