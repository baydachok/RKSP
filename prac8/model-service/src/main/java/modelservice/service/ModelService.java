package modelservice.service;


import java.util.List;
import modelservice.client.MessageDto;
import modelservice.client.MessagesClient;
import modelservice.dto.ModelGetDto;
import modelservice.dto.ModelPostDto;
import modelservice.entity.Model;
import modelservice.repository.ModelRepository;
import org.springframework.stereotype.Service;

@Service
public class ModelService {
    private final ModelRepository modelRepository;
    private final MessagesClient messagesClient;

    public ModelService(ModelRepository modelRepository, MessagesClient messagesClient) {
        this.modelRepository = modelRepository;
        this.messagesClient = messagesClient;
    }

    public Long saveModel(ModelPostDto modelPostDto) {
        Model model = new Model(modelPostDto.getName());
        modelRepository.save(model);
        return model.getId();
    }

    public ModelGetDto getModelWithMessages(String model) {
        List<MessageDto> messages = messagesClient.getMessagesByModel(model);
        return new ModelGetDto(model, messages);
    }
}
