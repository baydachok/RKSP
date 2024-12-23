package org.example.images.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@Table(
    name = "image",
    uniqueConstraints = @UniqueConstraint(columnNames = {"imageName"})
)
public class ImageEntity {

  @Id
  @NonNull
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  @NonNull
  private String imageName;

  @Lob
  private byte[] imageBytes;

  public ImageEntity(@NonNull String imageName, byte[] imageBytes) {
    this.imageName = imageName;
    this.imageBytes = imageBytes;
  }

}
