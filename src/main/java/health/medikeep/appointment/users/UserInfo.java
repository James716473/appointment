package health.medikeep.appointment.users;

import java.time.LocalDate;

public record UserInfo(Integer user_id, String first_name, String middle_name, String last_name, 
                        String email, String pass, Sex sex, LocalDate birth_date, String contact_number, 
                        String medical_history, String allergies, String family_medical_history) {

}


