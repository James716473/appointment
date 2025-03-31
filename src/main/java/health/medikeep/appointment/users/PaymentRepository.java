package health.medikeep.appointment.users;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class PaymentRepository {
    private final JdbcClient jdbcClient;
    
    public PaymentRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public void create(PaymentInfo paymentInfo){
        var updated = jdbcClient.sql("INSERT INTO payments (amount, type, payment_date, user_id, doctor_id, appointment_id) values (?, ?, ?, ?, ?, ?)")
        .params(paymentInfo.amount(), paymentInfo.type(), paymentInfo.payment_date(), paymentInfo.user_id(), paymentInfo.doctor_id(), paymentInfo.appointment_id())
        .update();

        Assert.state(updated == 1, "Payment Failed");
    }
}
