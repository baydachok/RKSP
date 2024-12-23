package org.example.prac4client.contorller;

import org.example.prac4client.model.Homework;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/homeworks")
public class RequestStreamController {

  private final RSocketRequester rSocketRequester;
  @Autowired
  public RequestStreamController(RSocketRequester rSocketRequester) {
    this.rSocketRequester = rSocketRequester;
  }
  @GetMapping
  public Publisher<Homework> getHomeworks() {
    return rSocketRequester
        .route("getHomeworks")
        .data(new Homework())
        .retrieveFlux(Homework.class);
  }

}
