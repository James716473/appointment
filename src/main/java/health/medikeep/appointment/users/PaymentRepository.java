package health.medikeep.appointment.users;

import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class PaymentRepository {
    private final JdbcClient jdbcClient;
    private final UserRepository userRepository;
    
    public PaymentRepository(JdbcClient jdbcClient, UserRepository userRepository) {
        this.jdbcClient = jdbcClient;
        this.userRepository = userRepository;
    }

    public void create(PaymentInfo paymentInfo){
        var updated = jdbcClient.sql("INSERT INTO payments (amount, type, payment_date, user_id, doctor_id, appointment_id) values (?, ?, ?, ?, ?, ?)")
        .params(paymentInfo.amount(), paymentInfo.type(), paymentInfo.payment_date(), paymentInfo.user_id(), paymentInfo.doctor_id(), paymentInfo.appointment_id())
        .update();

        Assert.state(updated == 1, "Payment Failed");
    }
    // So, dito find by email sya pero nangyayari jan is hahanapin ung id by email then gagamittin ung nakuhang id para makuha ung payments
    // i know na complicated sya pero naka foreign key kasi dun sa table ng payments is ung user id HAHAHAHAHAH
    public Optional<PaymentInfo> findUserPayments(String email){
        return jdbcClient.sql("select * from payments where id = :id")
            .param("id", userRepository.getUid(email))
            .query(PaymentInfo.class)  
            .optional();
    }

    //to do: find payments by date

    
}
