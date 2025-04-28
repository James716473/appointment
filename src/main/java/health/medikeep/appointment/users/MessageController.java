package health.medikeep.appointment.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/messages")
public class MessageController {
    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MessageInfo message) {
        if (messageRepository.create(message)) {
            return ResponseEntity.ok("Message Created Successfully");
        } else {
            return ResponseEntity.badRequest().body("Message Creation Unsuccessful");
        }
    }

   
}
