package health.medikeep.appointment.users;

import java.util.List;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class AffiliateRepository {
    private final JdbcClient jdbcClient;

    public AffiliateRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public boolean create(AffiliateInfo affiliate) {
        return jdbcClient.sql("INSERT INTO AFFILIATES(name, address, description) values(?, ?, ?)")
            .params(List.of(affiliate.name(), affiliate.address(), affiliate.description()))
            .update() == 1;    
    }

    public boolean delete(Integer affiliate_id) {
        return jdbcClient.sql("DELETE FROM AFFILIATES WHERE affiliate_id = :affiliate_id")
            .param("affiliate_id", affiliate_id)
            .update() == 1;    
    }

    public List<AffiliateInfo> showAffiliates() {
        return jdbcClient.sql("select * from affiliates")
            .query(AffiliateInfo.class)
            .list();
    }


}
