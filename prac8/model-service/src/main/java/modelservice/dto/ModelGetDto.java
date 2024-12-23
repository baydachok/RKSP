package modelservice.dto;

import java.util.List;
import lombok.Data;
import modelservice.client.MessageDto;

@Data
public class ModelGetDto {
  private Long id;
  private String modelName;
  private List<MessageDto> messages;

  public ModelGetDto(String modelName, List<MessageDto> messages) {
    this.modelName = modelName;
    this.messages = messages;
  }
}
