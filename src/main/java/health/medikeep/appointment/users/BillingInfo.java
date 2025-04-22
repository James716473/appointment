package health.medikeep.appointment.users;

import java.time.LocalDate;

public record BillingInfo(Integer billing_id, float price, Status status, LocalDate date_issued, LocalDate date_paid) {

}
