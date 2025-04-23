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
@RequestMapping("/api/billings")
public class BillingController {
    private final BillingRepository billingRepository;

    public BillingController(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    @GetMapping("/")
    public List<BillingInfo> showBillings() {
        return billingRepository.showBillings();
    }

    
    @PostMapping("/")
    public ResponseEntity<?> createBilling(@RequestBody BillingInfo billing) {
        Long billing_id = billingRepository.create(billing);
        if (billing_id != 0) {
            return ResponseEntity.ok(billing_id);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Billing Creation Unsuccessful");
        }
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteBilling(@RequestBody BillingInfo billing) {
        if (billingRepository.delete(billing.billing_id())) {
            return ResponseEntity.ok("Billing Deleted Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Billing Deletion Unsuccessful");
        }
    }




}
