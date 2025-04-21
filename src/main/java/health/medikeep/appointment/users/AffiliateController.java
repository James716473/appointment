package health.medikeep.appointment.users;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/affiliates")
public class AffiliateController {
    private final AffiliateRepository affiliateRepository;

    public AffiliateController(AffiliateRepository affiliateRepository) {
        this.affiliateRepository = affiliateRepository;
    }

    @GetMapping("/")
    public List<AffiliateInfo> showAffiliates() {
        return affiliateRepository.showAffiliates();
    }

    @PostMapping("/")
    public ResponseEntity<?> createAffiliate(@RequestBody AffiliateInfo affiliate) {
        if (affiliateRepository.create(affiliate)) {
            return ResponseEntity.ok("Affiliate Created Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Affiliate Creation Unsuccessful");
        }
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteAffiliate(@RequestBody AffiliateInfo affiliate) {
        if (affiliateRepository.delete(affiliate.affiliate_id())) {
            return ResponseEntity.ok("Affiliate Deleted Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Affiliate Deletion Unsuccessful");
        }
    }
}
