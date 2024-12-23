package org.example.prac4client.contorller;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/homeworks")
public class FireAndForgetController {

  private final RSocketRequester rSocketRequester;

  @Autowired
  public FireAndForgetController(RSocketRequester rSocketRequester) {
    this.rSocketRequester = rSocketRequester;
  }

  @DeleteMapping("/{id}")
  public Publisher<Void> deleteCat(@PathVariable Long id){
    return rSocketRequester
        .route("deleteHomework")
        .data(id)
        .send();
  }

}
