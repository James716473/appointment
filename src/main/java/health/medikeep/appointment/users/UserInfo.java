package health.medikeep.appointment.users;

import java.time.LocalDate;

public record UserInfo(Integer id, String last_name, String first_name, String middle_name, String email, String password, Sex sex, LocalDate birth_date, Role role) {

}


