package org.example.prac4client.contorller;

import org.example.prac4client.model.Homework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/homeworks")
public class RequestResponseController {

  private final RSocketRequester rSocketRequester;

  @Autowired
  public RequestResponseController(RSocketRequester rSocketRequester) {
    this.rSocketRequester = rSocketRequester;
  }

  @GetMapping("/{id}")
  public Mono<Homework> getHomework(@PathVariable Long id) {
    return rSocketRequester
        .route("getHomework")
        .data(id)
        .retrieveMono(Homework.class);
  }

  @PostMapping
  public Mono<Homework> addHomework(@RequestBody Homework homework) {
    return rSocketRequester
        .route("addHomework")
        .data(homework)
        .retrieveMono(Homework.class);
  }
}
