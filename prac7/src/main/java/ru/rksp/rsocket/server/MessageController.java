package ru.rksp.rsocket.server;

import java.time.LocalDateTime;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class MessageController {
    // Байдакчок Данила Михайлович ИКБО-01-21
    private final MessageRepository messageRepository;
    @Autowired
    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Message>> getMessageById(@PathVariable Long id) {
        return messageRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Message> getAllMessages(@RequestParam(name = "timestampFrom", required = false) LocalDateTime timestampFrom) {
        Flux<Message> messages = messageRepository.findAll();

        if(timestampFrom != null) {
            messages = messages.filter(message -> message.createdAt.isAfter(timestampFrom));
        }

        return messages
                .sort(Comparator.comparing(Message::getCreatedAt))
                .onErrorResume(e -> Flux.error(new CustomException("Failed to fetch messages", e)))
                .onBackpressureBuffer();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Message> createMessage(@RequestBody Message message) {
        return messageRepository.save(setCreatedAt(message));
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<Message>> updateMessage(
        @PathVariable Long id,
        @RequestBody Message updatedMessage
    ) {
        return messageRepository.findById(id)
                .flatMap(existingMessage -> {
                    existingMessage.text = updatedMessage.text;
                    existingMessage.author = updatedMessage.author;
                    return messageRepository.save(existingMessage);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteMessage(@PathVariable Long id) {
        return messageRepository.findById(id)
                .flatMap(existingMessage ->
                    messageRepository.delete(existingMessage)
                            .then(Mono.just(
                                    new ResponseEntity<Void>(HttpStatus.NO_CONTENT)
                            )))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private Message setCreatedAt(Message message) {
        message.setCreatedAt(LocalDateTime.now());
        return message;
    }
}