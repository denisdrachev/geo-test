package app.validate;

import app.exception.IncorrectInputException;
import app.model.Feature;
import app.model.FeatureCollection;
import org.springframework.stereotype.Service;

@Service
public class Validator {

    public void validate(FeatureCollection featureCollection) throws IncorrectInputException {

        if (featureCollection == null
                || featureCollection.getFeatures() == null
                || featureCollection.getFeatures().size() == 0)
            throw new IncorrectInputException();

        for (Feature feature : featureCollection.getFeatures()) {
            if (feature.getGeometry() == null || feature.getGeometry().getCoordinates() == null)
                throw new IncorrectInputException();
            for (Double[] coordinate : feature.getGeometry().getCoordinates()) {
                if (coordinate.length != 2)
                    throw new IncorrectInputException();
            }
        }
    }
}
