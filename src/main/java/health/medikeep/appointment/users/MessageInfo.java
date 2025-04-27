package health.medikeep.appointment.users;

import java.sql.Timestamp;

public record MessageInfo(Integer message_id, Integer sender_id, Integer receiver_id, String message_type, String message, Timestamp timestamp) {

}
