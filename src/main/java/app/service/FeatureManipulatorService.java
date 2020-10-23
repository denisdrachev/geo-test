package app.service;

import app.model.Feature;
import app.model.FeatureCollection;
import app.model.FeatureEntity;
import app.repositories.FeatureEntityCrudRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FeatureManipulatorService {

    private final FeatureEntityCrudRepository featureEntityCrudRepository;

    @Transactional
    public void saveFeatures(FeatureCollection featureCollection) {

        List<FeatureEntity> collect = featureCollection.getFeatures().stream()
                .map(this::transformFeatureToFeatureEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        featureEntityCrudRepository.saveAll(collect);
        log.info("Collection of FeatureEntity saved. Saved collection size: {}", collect.size());
    }

    @Transactional
    public FeatureCollection getFeatureCollectionWithColor(boolean isRedFlag) {

        List<FeatureEntity> allFeatureEntityByColor = featureEntityCrudRepository.findAllByColor(isRedFlag ? "red" : "blue");
        log.info("Find FeatureEntities with color: {}. Elements count: {}",
                isRedFlag ? "red" : "blue", allFeatureEntityByColor.size());

        List<Feature> collect = allFeatureEntityByColor.stream()
                .map(this::transformFeatureEntityToFeature)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new FeatureCollection("FeatureCollection", collect);
    }

    private FeatureEntity transformFeatureToFeatureEntity(Feature feature) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FeatureEntity featureEntity = new FeatureEntity();
            featureEntity.setColor(feature.getProperties().get("color"));
            String featureString = objectMapper.writeValueAsString(feature);
            featureEntity.setContent(featureString);
            return featureEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Feature transformFeatureEntityToFeature(FeatureEntity featureEntity) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(featureEntity.getContent(), Feature.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
