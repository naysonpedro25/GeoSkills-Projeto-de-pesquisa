package br.com.geoskills.repository;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import br.com.geoskills.model.ColoredCoordinate;
import br.com.geoskills.model.Coordinate;

public class GameCoordinateRepository {
    private final static int numLevels = 6;
    private static List<Coordinate[]> originalCoordinates;

    public static List<Coordinate[]> getListCoordinates() {
        ArrayList<Coordinate[]> coordinates = new ArrayList<>();
        Random random = new Random();
        Set<String> uniqueCoordinates = new HashSet<>();

        for (int i = 0; i < numLevels; i++) {
            Coordinate[] coordArray = new Coordinate[numLevels];
            for (int j = 0; j < numLevels; j++) {
                int latitude;
                int longitude;
                String coordString;

                do {
                    latitude = random.nextInt(10) * 10;
                    longitude = random.nextInt(9) * 20 + 20;

                    coordString = "latitude_" + latitude + "_longitude_" + longitude;


                } while (uniqueCoordinates.contains(coordString));

                uniqueCoordinates.add(coordString);
                coordArray[j] = new Coordinate("latitude_" + latitude, "longitude_" + longitude);
            }
            coordinates.add(coordArray);
        }
        originalCoordinates = coordinates;

        return coordinates;
    }

    public static List<ColoredCoordinate[]> getListCoordinatesWithColors() {
        ArrayList<ColoredCoordinate[]> coordinates = new ArrayList<>();

        String[] colors = {"red", "green", "blue", "orange", "yellow", "purple"};
        if (originalCoordinates == null) {
            getListCoordinates();
        }

        int i = 0;
        for (Coordinate[] coordArray : originalCoordinates) {
            ColoredCoordinate[] coordColoredArray = new ColoredCoordinate[numLevels];
            for (int j = 0; j < coordArray.length; j++) {
                Coordinate coord = coordArray[j];
                String color = colors[i % colors.length];
                coordColoredArray[j] = new ColoredCoordinate(coord.getLatitude(), coord.getLongitude(), color);
                i++;
            }
            coordinates.add(coordColoredArray);
        }

        return coordinates;
    }


}
