package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IConvertToPlace;
import uk.ac.warwick.cs126.models.Place;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import uk.ac.warwick.cs126.structures.LLElement;
import uk.ac.warwick.cs126.structures.MyAVLTree;
import uk.ac.warwick.cs126.structures.MyLinkedList;
import uk.ac.warwick.cs126.structures.Pair;

public class ConvertToPlace implements IConvertToPlace {
    private MyAVLTree<Float, MyLinkedList<Place>> places;

    public ConvertToPlace() {
        places = new MyAVLTree<>((MyLinkedList<Place> placeList) -> (placeList.get(0).getLatitude()));
        for (Place place:getPlacesArray()) {
            MyLinkedList<Place> thatList =  places.search(place.getLatitude());
            if (thatList == null) {
                thatList = new MyLinkedList<>();
                thatList.add(place);
                places.insert(thatList);
            }
            thatList.add(place);
        }
    }

    public Place convert(float latitude, float longitude) {
        MyLinkedList<Place> thesePlaces = places.search(latitude);
        LLElement<Place> head = thesePlaces.getHead();
        while (head.getNext() != null) {
            if (head.getVal().getLongitude() == longitude)
                return head.getVal();
            head = head.getNext();
        }
        return new Place("", "", 0.0f, 0.0f);
    }

    public Place[] getPlacesArray() {
        Place[] placeArray = new Place[0];

        try {
            InputStream resource = ConvertToPlace.class.getResourceAsStream("/data/placeData.tsv");
            if (resource == null) {
                String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
                String resourcePath = Paths.get(currentPath, "data", "placeData.tsv").toString();
                File resourceFile = new File(resourcePath);
                resource = new FileInputStream(resourceFile);
            }

            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Place[] loadedPlaces = new Place[lineCount - 1];

            BufferedReader tsvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int placeCount = 0;
            String row;

            tsvReader.readLine();
            while ((row = tsvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split("\t");
                    Place place = new Place(
                            data[0],
                            data[1],
                            Float.parseFloat(data[2]),
                            Float.parseFloat(data[3]));
                    loadedPlaces[placeCount++] = place;
                }
            }
            tsvReader.close();

            placeArray = loadedPlaces;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return placeArray;
    }
}

