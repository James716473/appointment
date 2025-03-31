package health.medikeep.appointment.users;

import java.util.Date;

public record RecordInfo(Integer record_id, String type, Date record_date, String image_url, String description, Integer user_id) {
    
}
