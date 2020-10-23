package app.controller;

import app.model.FeatureCollection;
import app.service.FeatureManipulatorService;
import app.service.PaintService;
import app.validate.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class Controller {

    private final Validator validator;
    private final PaintService paintService;
    private final FeatureManipulatorService featureManipulatorService;

    @PostMapping(value = "/first")
    public FeatureCollection first(@RequestParam("file") MultipartFile file) throws IOException {

        log.info("[Request incoming: /first] [file name: {}] [file size: {}]", file.getOriginalFilename(), file.getSize());
        ObjectMapper objectMapper = new ObjectMapper();
        FeatureCollection featureCollection = objectMapper.readValue(new String(file.getBytes()), FeatureCollection.class);

        validator.validate(featureCollection);
        paintService.paint(featureCollection);
        featureManipulatorService.saveFeatures(featureCollection);

        return featureCollection;
    }

    @GetMapping(value = "/second")
    public FeatureCollection second(@RequestParam("param") boolean param) {
        log.info("[Request incoming: /second] [param: {}]", param);
        return featureManipulatorService.getFeatureCollectionWithColor(param);
    }
}
