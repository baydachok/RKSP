package org.example.prac4client.contorller;

import java.util.List;
import org.example.prac4client.model.Homework;
import org.example.prac4client.model.HomeworkListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/homeworks")
public class ChannelController {

  private final RSocketRequester rSocketRequester;
  @Autowired
  public ChannelController(RSocketRequester rSocketRequester) {
    this.rSocketRequester = rSocketRequester;
  }

  @PostMapping("/channel")
  public Flux<Homework> addCatsMultiple(@RequestBody HomeworkListWrapper homeworkListWrapper){
    List<Homework> homeworkList = homeworkListWrapper.getHomeworks();
    Flux<Homework> homeworks = Flux.fromIterable(homeworkList);
    return rSocketRequester
        .route("homeworkChannel")
        .data(homeworks)
        .retrieveFlux(Homework.class);
  }

}
