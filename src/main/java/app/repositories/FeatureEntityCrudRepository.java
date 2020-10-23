package app.repositories;

import app.model.FeatureEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FeatureEntityCrudRepository extends CrudRepository<FeatureEntity, Long> {
    List<FeatureEntity> findAllByColor(String color);
}
