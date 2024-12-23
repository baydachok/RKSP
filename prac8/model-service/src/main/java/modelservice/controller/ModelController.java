package modelservice.controller;

import modelservice.dto.ModelGetDto;
import modelservice.dto.ModelPostDto;
import modelservice.service.ModelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/models")
public class ModelController {
    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @PostMapping
    public Long createModel(@RequestBody ModelPostDto modelPostDto) {
        return modelService.saveModel(modelPostDto);
    }

    @GetMapping("/{model}")
    public ModelGetDto getModelWithMessages(@PathVariable("model") String model) {
        return modelService.getModelWithMessages(model);
    }
}
