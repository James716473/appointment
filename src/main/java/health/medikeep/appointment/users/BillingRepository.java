package health.medikeep.appointment.users;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class BillingRepository {
    private final JdbcClient jdbcClient;

    public BillingRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public long create(BillingInfo billing) {

        KeyHolder kh = new GeneratedKeyHolder();
        int rows = jdbcClient.sql("INSERT INTO BILLINGS(date_issued) values(?)")
            .params(List.of(billing.date_issued()))
            .update(kh);  
        
        if (rows != 1) return 0;                    // or throw

        // MySQL returns it as Number
        Number key = kh.getKey();
        return key != null ? key.longValue() : 0;
              
    }

    public List<BillingInfo> showBillings() {
        return jdbcClient.sql("select * from billings")
            .query(BillingInfo.class)
            .list();
    }

    public boolean delete(Integer billing_id) {
        return jdbcClient.sql("DELETE FROM BILLINGS WHERE billing_id = :billing_id")
            .param("billing_id", billing_id)
            .update() == 1;    
    }
    
    public Optional<BillingInfo> findById(Integer billing_id) {
        return jdbcClient.sql("SELECT * FROM BILLING WHERE billing_id = :billing_id")
            .param("billing_id", billing_id)
            .query(BillingInfo.class)
            .optional();
    }

    public BillingInfo findByAppointmentId(Integer appointment_id) {
        return jdbcClient.sql("SELECT * FROM BILLINGS WHERE billing_id = (SELECT billing_id FROM APPOINTMENTS WHERE appointment_id = :appointment_id)")
            .param("appointment_id", appointment_id)
            .query(BillingInfo.class)
            .single();
    }
}
