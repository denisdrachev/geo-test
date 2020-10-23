package app.service;

import app.model.Feature;
import app.model.FeatureCollection;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaintService {

//    private Map<Integer, List<SubLine>> map = new HashMap<>();

    public void paint(FeatureCollection featureCollection) {
        Map<Integer, Boolean> colors = new HashMap<>();
        Map<Integer, List<SubLine>> map = new HashMap<>();

        for (int i = 0; i < featureCollection.getFeatures().size(); i++) {
            parseFeature(featureCollection.getFeatures().get(i), i, map);
            colors.put(i, true);
        }

        initColors(colors, map);

        initColor(featureCollection, colors);
    }

    private void parseFeature(Feature feature, Integer index, Map<Integer, List<SubLine>> map) {
        Double[] prev = null;
        List<SubLine> subLines = new ArrayList<>();

        for (Double[] coordinate : feature.getGeometry().getCoordinates()) {

            if (prev != null) {
                SubLine subLine = new SubLine(
                        new Vector2D(prev[0], prev[1]),
                        new Vector2D(coordinate[0], coordinate[1]), 0
                );
                subLines.add(subLine);
            } else {
                prev = new Double[2];
                prev[0] = coordinate[0];
                prev[1] = coordinate[1];
            }
        }
        map.put(index, subLines);
    }

    private void initColors(Map<Integer, Boolean> colors, Map<Integer, List<SubLine>> map) {
        map.forEach((outerIndex, outerSubLine) -> {
            if (!colors.get(outerIndex))
                return;

            for (SubLine subLine : outerSubLine) {
                boolean isIntersect = checkIntersectOneSubLine(subLine, outerIndex, colors, map);
                if (isIntersect)
                    break;
            }
        });
    }

    private boolean checkIntersectOneSubLine(SubLine subLineForCheck, Integer index,
                                             Map<Integer, Boolean> colors, Map<Integer, List<SubLine>> map) {

        for (Map.Entry<Integer, List<SubLine>> integerListEntry : map.entrySet()) {
            if (integerListEntry.getKey().equals(index))
                continue;

            for (SubLine subLine : integerListEntry.getValue()) {
                Vector2D intersection = subLineForCheck.intersection(subLine, true);
                if (intersection != null) {
                    colors.put(index, false);
                    colors.put(integerListEntry.getKey(), false);
                    return true;
                }
            }
        }
        return false;
    }

    private void initColor(FeatureCollection featureCollection, Map<Integer, Boolean> colors) {
        colors.forEach((index, isNotIntersect) -> {
            Map<String, String> map = new HashMap<>();
            map.put("color", isNotIntersect ? "blue" : "red");
            featureCollection.getFeatures().get(index).setProperties(map);
        });
    }
}
