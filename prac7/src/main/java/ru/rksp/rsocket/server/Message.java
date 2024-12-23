package ru.rksp.rsocket.server;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;


@Table("message_sr")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    @Id
    Long id;
    @Column("text")
    String text;
    @Column("createdAt")
    LocalDateTime createdAt;
    @Column("author")
    String author;
}
