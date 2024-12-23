package org.example.prac4server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Homework {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long homeworkId;
  private String subject;
  private String description;
  private String deadline;
  private String status;

}
