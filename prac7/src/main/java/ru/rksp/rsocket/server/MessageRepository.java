package ru.rksp.rsocket.server;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends R2dbcRepository<Message, Long> {
}
