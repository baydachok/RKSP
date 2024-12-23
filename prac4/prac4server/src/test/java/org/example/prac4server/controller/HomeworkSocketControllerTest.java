package org.example.prac4server.controller;

import java.time.Duration;
import java.util.Objects;
import org.example.prac4server.Prac4serverApplicationTests;
import org.example.prac4server.model.Homework;
import org.example.prac4server.repository.HomeworkRepository;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class HomeworkSocketControllerTest extends Prac4serverApplicationTests {

  @Autowired
  private HomeworkRepository homeworkRepository;
  private RSocketRequester requester;

  @BeforeEach
  public void setup() {
    requester = RSocketRequester.builder()
        .rsocketStrategies(builder -> builder.decoder(new Jackson2JsonDecoder()))
        .rsocketStrategies(builder -> builder.encoder(new Jackson2JsonEncoder()))
        .rsocketConnector(connector -> connector
            .reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
        .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
        .tcp("localhost", 5200);
  }

  @AfterEach
  public void cleanup() {
    requester.dispose();
  }

  @Test
  public void testGetHomework() {
    Homework homework = new Homework();
    homework.setSubject("RKSP");
    homework.setDescription("RSocket");
    homework.setDeadline("2024-10-21");
    Homework homeworkEntity = homeworkRepository.save(homework);
    Mono<Homework> result = requester.route("getHomework")
        .data(homeworkEntity.getHomeworkId())
        .retrieveMono(Homework.class);
    assertNotNull(result.block());
  }

  @Test
  public void testAddHomework() {
    Homework homework = new Homework();
    homework.setSubject("RKSP");
    homework.setDescription("RSocket");
    homework.setDeadline("2024-10-21");
    Mono<Homework> result = requester.route("addHomework")
        .data(homework)
        .retrieveMono(Homework.class);
    Homework savedHomework = result.block();
    assertNotNull(savedHomework);
    assertNotNull(savedHomework.getHomeworkId());
    assertTrue(savedHomework.getHomeworkId() > 0);
  }

  @Test
  public void testGetHomeworks() {
    Flux<Homework> result = requester.route("getHomeworks")
        .retrieveFlux(Homework.class);
    assertNotNull(result.blockFirst());
  }

  @Test
  public void testDeleteHomework() {
    Homework homework = new Homework();
    homework.setSubject("RKSP");
    homework.setDescription("RSocket");
    homework.setDeadline("2024-10-21");
    Homework savedHomework = homeworkRepository.save(homework);
    Mono<Void> result = requester.route("deleteHomework")
        .data(savedHomework.getHomeworkId())
        .send();
    result.block();
    Homework deletedHomework = homeworkRepository.findById(savedHomework.getHomeworkId()).orElse(null);
    assertNotSame(deletedHomework, savedHomework);
  }

  @Test
  public void testHomeworkChannel() {
    Homework homework = new Homework();
    homework.setSubject("RKSP");
    homework.setDescription("RSocket");
    homework.setDeadline("2024-10-21");
    Flux<Homework> homeworks = Flux.just(homework);
    Flux<Homework> result = requester.route("homeworkChannel")
        .data(homeworks)
        .retrieveFlux(Homework.class);
    assertNotNull(result.blockFirst());
    assertEquals(
        Objects.requireNonNull(homeworks.blockFirst()).getSubject(),
        Objects.requireNonNull(result.blockFirst()).getSubject()
    );
  }
}
