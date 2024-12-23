package org.example.prac4server.controller;

import org.example.prac4server.model.Homework;
import org.example.prac4server.repository.HomeworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class HomeworkSocketController {

  private final HomeworkRepository homeworkRepository;

  @Autowired
  public HomeworkSocketController(HomeworkRepository homeworkRepository) {
    this.homeworkRepository = homeworkRepository;
  }

  @MessageMapping("getHomework")
  public Mono<Homework> getHomework(Long id) {
    return Mono.justOrEmpty(homeworkRepository.findById(id));
  }

  @MessageMapping("addHomework")
  public Mono<Homework> addHomework(Homework homework) {
    return Mono.justOrEmpty(homeworkRepository.save(homework));
  }

  @MessageMapping("getHomeworks")
  public Flux<Homework> getHomeworks() {
    return Flux.fromIterable(homeworkRepository.findAll());
  }

  @MessageMapping("deleteHomework")
  public Mono<Void> deleteHomework(Long id){
    homeworkRepository.findById(id).ifPresent(homeworkRepository::delete);
    return Mono.empty();
  }

  @MessageMapping("homeworkChannel")
  public Flux<Homework> homeworkChannel(Flux<Homework> homeworks) {
    return homeworks.flatMap(homework -> Mono.fromCallable(() ->
            homeworkRepository.save(homework)))
        .collectList()
        .flatMapMany(Flux::fromIterable);
  }

}
