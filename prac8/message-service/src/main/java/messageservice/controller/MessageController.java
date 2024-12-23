package messageservice.controller;

import java.util.List;
import messageservice.entity.Message;
import messageservice.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/byId/{id}")
    public Message getMessage(@PathVariable("id") Long id) {
        return messageService.getMessageById(id);
    }

    @GetMapping("/byModel/{model}")
    public List<Message> getMessages(@PathVariable("model") String model) {
        return messageService.getMessagesByModel(model);
    }

    @PostMapping
    public Message createMessage(@RequestBody Message message) {
        return messageService.saveMessage(message);
    }
}
