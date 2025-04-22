package health.medikeep.appointment.users;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class BillingRepository {
    private final JdbcClient jdbcClient;

    public BillingRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public boolean create(BillingInfo billing) {
        return jdbcClient.sql("INSERT INTO BILLING(billing_id, date_issued) values(?, ?)")
            .params(List.of(billing.billing_id(), billing.price(), billing.date_issued()))
            .update() == 1;    
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
}
