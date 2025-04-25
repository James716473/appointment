package health.medikeep.appointment.users;

import java.security.Timestamp;

public record MessageInfo(Integer message_id, Integer sender_id, Integer reciever_id, String message_type, String message, Timestamp timestamp) {

}
