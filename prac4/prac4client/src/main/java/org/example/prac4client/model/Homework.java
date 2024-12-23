package org.example.prac4client.model;

import lombok.Data;

@Data
public class Homework {
  private Long homeworkId;
  private String subject;
  private String description;
  private String deadline;
  private String status;
}
