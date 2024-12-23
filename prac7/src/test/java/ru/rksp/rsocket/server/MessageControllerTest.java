package ru.rksp.rsocket.server;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MessageControllerTest {
    @Test
    public void testGetMessageById() {
        Message message = new Message();
        message.id = 1L;
        message.text = "Message1";

        MessageRepository messageRepository = Mockito.mock(MessageRepository.class);
        Mockito.when(messageRepository.findById(1L)).thenReturn(Mono.just(message));

        MessageController messageController = new MessageController(messageRepository);

        ResponseEntity<Message> response = messageController.getMessageById(1L).block();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(message, response.getBody());
    }

    @Test
    public void testGetAllMessages() {
        Message message1 = new Message();
        message1.id = 1L;
        message1.text = "Message1";
        message1.setCreatedAt(LocalDateTime.MIN);

        Message message2 = new Message();
        message2.id = 1L;
        message2.text = "Message2";
        message2.setCreatedAt(LocalDateTime.MAX);

        MessageRepository messageRepository = Mockito.mock(MessageRepository.class);
        Mockito.when(messageRepository.findAll()).thenReturn(Flux.just(message1, message2));

        MessageController messageController = new MessageController(messageRepository);

        Flux<Message> response = messageController.getAllMessages(null);
        Assertions.assertEquals(2, response.collectList().block().size());
    }

    @Test
    public void testCreateMessage() {
        Message message = new Message();
        message.id = 1L;
        message.text = "Message1";

        MessageRepository messageRepository = Mockito.mock(MessageRepository.class);
        Mockito.when(messageRepository.save(message)).thenReturn(Mono.just(message));

        MessageController messageController = new MessageController(messageRepository);

        Mono<Message> response = messageController.createMessage(message);
        Assertions.assertEquals(message, response.block());
    }

    @Test
    public void testUpdateMessage() {
        Message existingMessage = new Message();
        existingMessage.id = 1L;
        existingMessage.text = "Message1";

        Message updatedMessage = new Message();
        updatedMessage.id = 1L;
        updatedMessage.text = "Message1_updated";

        MessageRepository messageRepository = Mockito.mock(MessageRepository.class);
        Mockito.when(messageRepository.findById(1L)).thenReturn(Mono.just(existingMessage));
        Mockito.when(messageRepository.save(existingMessage)).thenReturn(Mono.just(updatedMessage));

        MessageController messageController = new MessageController(messageRepository);

        ResponseEntity<Message> response = messageController.updateMessage(1L, updatedMessage).block();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(updatedMessage, response.getBody());
    }

    @Test
    public void testDeleteMessage() {
        Message message = new Message();
        message.id = 1L;
        message.text = "Message1";

        MessageRepository messageRepository = Mockito.mock(MessageRepository.class);
        Mockito.when(messageRepository.findById(1L)).thenReturn(Mono.just(message));
        Mockito.when(messageRepository.delete(message)).thenReturn(Mono.empty());

        MessageController messageController = new MessageController(messageRepository);

        ResponseEntity<Void> response = messageController.deleteMessage(1L).block();
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
