package messageservice.service;

import java.util.List;
import messageservice.entity.Message;
import messageservice.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message getMessageById(Long id) {
        Optional<Message> message = messageRepository.findById(id);
        return message.orElse(null);
    }

    public List<Message> getMessagesByModel(String model) {
        return messageRepository.findMessagesByModel(model);
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

}
