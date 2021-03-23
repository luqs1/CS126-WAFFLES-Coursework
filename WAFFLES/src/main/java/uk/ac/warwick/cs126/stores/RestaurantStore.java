package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IRestaurantStore;
import uk.ac.warwick.cs126.models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyAVLTree;
import uk.ac.warwick.cs126.structures.MyArrayList;

import uk.ac.warwick.cs126.structures.Pair;
import uk.ac.warwick.cs126.util.*;

public class RestaurantStore implements IRestaurantStore {

    private final MyAVLTree<Long, Restaurant> restaurantAVL;
    private final MyAVLTree<Long, Restaurant> blacklisted;
    private final ConvertToPlace convertToPlace;
    private final DataChecker dataChecker;
    private final Sorter<Restaurant> sorter;

    public RestaurantStore() {
        // Initialise variables here
        restaurantAVL = new MyAVLTree<>(Restaurant::getID);
        blacklisted = new MyAVLTree<>(Restaurant::getID);
        dataChecker = new DataChecker();
        convertToPlace = new ConvertToPlace();
        sorter = new Sorter<>((Pair<Restaurant> pair) ->
                (pair.left.getID().compareTo(pair.right.getID())));
    }

    public Restaurant[] loadRestaurantDataToArray(InputStream resource) {
        Restaurant[] restaurantArray = new Restaurant[0];

        try {
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

            Restaurant[] loadedRestaurants = new Restaurant[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            String row;
            int restaurantCount = 0;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Restaurant restaurant = new Restaurant(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            Cuisine.valueOf(data[4]),
                            EstablishmentType.valueOf(data[5]),
                            PriceRange.valueOf(data[6]),
                            formatter.parse(data[7]),
                            Float.parseFloat(data[8]),
                            Float.parseFloat(data[9]),
                            Boolean.parseBoolean(data[10]),
                            Boolean.parseBoolean(data[11]),
                            Boolean.parseBoolean(data[12]),
                            Boolean.parseBoolean(data[13]),
                            Boolean.parseBoolean(data[14]),
                            Boolean.parseBoolean(data[15]),
                            formatter.parse(data[16]),
                            Integer.parseInt(data[17]),
                            Integer.parseInt(data[18]));

                    loadedRestaurants[restaurantCount++] = restaurant;
                }
            }
            csvReader.close();

            restaurantArray = loadedRestaurants;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return restaurantArray;
    }

    public boolean addRestaurant(Restaurant restaurant) {
        if (!dataChecker.isValid(restaurant) | blacklisted.search(restaurant)) // This automatically sets the restaurants ID. So can't lazy OR.
            return false;

        if (restaurantAVL.search(restaurant)) {
            blacklisted.insert(restaurant);
            restaurantAVL.remove(restaurant);
            return false;
        }

        restaurantAVL.insert(restaurant);
        return true;
    }

    public boolean addRestaurant(Restaurant[] restaurants) {
        if (restaurants == null)
            return false;

        boolean allDone = true;

        for (Restaurant restaurant : restaurants) {
            allDone = allDone & addRestaurant(restaurant); //Needs to eval, can't short.
        }

        return allDone;
    }

    private Restaurant[] intoRestaurantArray(Object[] unCast) {
        if (unCast == null)
            return null;
        Restaurant[] arr = new Restaurant[unCast.length];
        for (int i = 0; i < unCast.length; i++)
            arr[i] = (Restaurant) unCast[i];

        return arr;
    }

    private Restaurant[] getCopy(Restaurant[] favourites) { // Important to prevent side effects to original array
        if (favourites == null)
            return new Restaurant[0];
        Restaurant[] copy = new Restaurant[favourites.length];
        System.arraycopy(favourites, 0, copy, 0, favourites.length);
        return copy;
    }

    public Restaurant getRestaurant(Long id) {
        if (id == null)
            return null;

        return restaurantAVL.search(id);
    }

    public Restaurant[] getRestaurants() {
        return intoRestaurantArray(restaurantAVL.inorder());
    }

    public Restaurant[] getRestaurants(Restaurant[] restaurants) {
        if (restaurants == null)
            return new Restaurant[0];

        return sorter.sort(getCopy(restaurants));
    }

    private Integer nameComp(Pair<Restaurant> pair) {
        int out = pair.left.getName().toLowerCase()
                .compareTo(pair.right.getName().toLowerCase());
        if (out == 0)
            out = pair.left.getID().compareTo(pair.right.getID());

        return out;
    }

    public Restaurant[] getRestaurantsByName() {
        return sorter.sort(getRestaurants(), this::nameComp);
    }

    private Integer dateComp(Pair<Restaurant> pair) {
        int out = pair.left.getDateEstablished()
                .compareTo(pair.right.getDateEstablished()
                );

        if (out == 0)
            out = nameComp(pair);
        return out;
    }

    public Restaurant[] getRestaurantsByDateEstablished() {
        return getRestaurantsByDateEstablished(getRestaurants());
    }

    public Restaurant[] getRestaurantsByDateEstablished(Restaurant[] restaurants) {
        if (restaurants == null)
            return new Restaurant[0];
        return sorter.sort(getCopy(restaurants), this::dateComp);
    }

    private Integer starComp(Pair<Restaurant> pair) {
        int out = Integer.compare(pair.right.getWarwickStars(),
                (pair.left.getWarwickStars()));
        if (out == 0)
            out = nameComp(pair);

        return out;
    }

    public Restaurant[] getRestaurantsByWarwickStars() {
        MyArrayList<Restaurant> filtered = new MyArrayList<>();
        for (Restaurant restaurant : getRestaurants()) {
            if (restaurant.getWarwickStars() != 0)
                filtered.add(restaurant);
        }

        return sorter.sort(intoRestaurantArray(filtered.getArray()), this::starComp);
    }

    private Integer ratingComp(Pair<Restaurant> pair) {
        int out = Float.compare(pair.right.getCustomerRating(),
                pair.left.getCustomerRating());

        if (out == 0)
            out = nameComp(pair);

        return out;
    }

    public Restaurant[] getRestaurantsByRating(Restaurant[] restaurants) {
        if (restaurants == null)
            return new Restaurant[0];
        return sorter.sort(getCopy(restaurants), this::ratingComp);
    }

    public RestaurantDistance[] getRestaurantsByDistanceFrom(float latitude, float longitude) {
        return getRestaurantsByDistanceFrom(getRestaurants(), latitude, longitude);
    }

    public RestaurantDistance[] getRestaurantsByDistanceFrom(Restaurant[] restaurants, float latitude, float longitude) {
        if (restaurants == null)
            return new RestaurantDistance[0];
        Function<Pair<Restaurant>, Integer> distComp = (Pair<Restaurant> pair) -> {
            Restaurant l = pair.left;
            Restaurant r = pair.right;

            float dist1 = HaversineDistanceCalculator.inKilometres(l.getLatitude(), l.getLongitude(),
                    latitude, longitude);

            float dist2 = HaversineDistanceCalculator.inKilometres(r.getLatitude(), r.getLongitude(),
                    latitude, longitude);

            int out = Float.compare(dist1, dist2);
            if (out == 0)
                out = l.getID().compareTo(r.getID());

            return out;
        }; // I had to write this function inside the outer func because it makes use of the given lat and long.

        Restaurant[] sorted = sorter.sort(getCopy(restaurants), distComp);

        RestaurantDistance[] out = new RestaurantDistance[sorted.length];

        for (int i = 0; i < sorted.length; i++)
            out[i] = new RestaurantDistance(sorted[i], HaversineDistanceCalculator.inKilometres(
                    sorted[i].getLatitude(), sorted[i].getLongitude(),
                    latitude, longitude));
        return out;
    }

    public Restaurant[] getRestaurantsContaining(String searchTerm) {
        if (searchTerm == null || searchTerm.equals("")) {
            return new Restaurant[0];
        }
        String searchTermProcessed = StringFormatter.
                convertAccentsFaster(searchTerm).
                toLowerCase().
                replaceAll("[ ]{2,}", " "); // this replaces all 2 or more whitespaces with 1.
        Restaurant[] arr1 = getRestaurantsByName();
        MyArrayList<Restaurant> list = new MyArrayList<>();

        for (Restaurant restaurant : arr1) {
            boolean isInName = restaurant.getName().contains(searchTermProcessed)
                    || restaurant.getCuisine().name().contains(searchTermProcessed)
                    || convertToPlace.
                    convert(restaurant.getLatitude(), restaurant.getLongitude()).
                    getName().contains(searchTermProcessed);
            if (isInName)
                list.add(restaurant);
        }

        return intoRestaurantArray(list.getArray());
    }
}
