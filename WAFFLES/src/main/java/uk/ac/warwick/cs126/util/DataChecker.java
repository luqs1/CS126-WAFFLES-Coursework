package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IDataChecker;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Review;

public class DataChecker implements IDataChecker {

    public DataChecker() {
        // Initialise things here
    }

    public static boolean anyNull(Object[] objects) {
        for (Object a : objects) {
            if (a == null)
                return true;
        }
        return false;
    }

    public Long extractTrueID(String[] repeatedID) {
        if (repeatedID.length != 3)
            return null;
        String a = repeatedID[0];
        String b = repeatedID[1];
        String c = repeatedID[2];

        if (a.compareTo(b) != 0 && a.compareTo(c) != 0 && b.compareTo(c) != 0)
            return null;

        if (a.compareTo(b) == 0 || a.compareTo(c) == 0)
            return Long.parseLong(a);

        return Long.parseLong(b);
    }

    public boolean isValid(Long inputID) {
        if (inputID == null)
            return false;
        String id = inputID.toString();

        if (id.length() != 16)
            return false;
        char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int[] counts = new int[9];

        for (int i = 0; i < id.length(); i++) {
            boolean found = false;
            for (int j = 0; j < 9; j++) {
                if (id.charAt(i) == digits[j]) {
                    found = true;
                    counts[j]++;
                    break;
                }
            }
            if (!found)
                return false;
        }
        for (int count : counts) {
            if (count > 3)
                return false;
        }
        return true;
    }

    public boolean isValid(Customer customer) {
        if (customer == null)
            return false;
        Object[] entries = {
                customer.getID(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getDateJoined(),
                customer.getLatitude(),
                customer.getLongitude(),
        };
        if (anyNull(entries))
            return false;
        return isValid(customer.getID());
    }

    public boolean isValid(Restaurant restaurant) {
        if (restaurant == null)
            return false;
        Object[] entries = {
                restaurant.getRepeatedID(),
                restaurant.getID(),
                restaurant.getName(),
                restaurant.getOwnerFirstName(),
                restaurant.getOwnerLastName(),
                restaurant.getCuisine(),
                restaurant.getEstablishmentType(),
                restaurant.getPriceRange(),
                restaurant.getDateEstablished(),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                restaurant.getVegetarianOptions(),
                restaurant.getVeganOptions(),
                restaurant.getGlutenFreeOptions(),
                restaurant.getNutFreeOptions(),
                restaurant.getLactoseFreeOptions(),
                restaurant.getHalalOptions(),
                restaurant.getLastInspectedDate(),
                restaurant.getFoodInspectionRating(),
                restaurant.getWarwickStars(),
                restaurant.getCustomerRating(),
        };
        if (anyNull(entries))
            return false;

        if (restaurant.getDateEstablished().compareTo(restaurant.getLastInspectedDate()) > 0)
            return false;

        boolean validIRating = false;
        for (int i : new int[]{0, 1, 2, 3, 4, 5}) {
            if (i == restaurant.getFoodInspectionRating()) {
                validIRating = true;
                break;
            }
        }
        if (!validIRating)
            return false;


        boolean validStars = false;
        for (int i : new int[]{0, 1, 2, 3})
            if (i == restaurant.getWarwickStars()) {
                validStars = true;
                break;
            }
        if (!validStars)
            return false;

        if (restaurant.getCustomerRating() != 0.0f
                || (restaurant.getCustomerRating() >= 1.0f && restaurant.getCustomerRating() <= 5.0f))
            return false;

        restaurant.setID(extractTrueID(restaurant.getRepeatedID()));
        return isValid(restaurant.getID());
    }

    public boolean isValid(Favourite favourite) {
        if (favourite == null)
            return false;
        Object[] entries = {
                favourite.getID(),
                favourite.getCustomerID(),
                favourite.getRestaurantID(),
                favourite.getDateFavourited(),
        };
        if (anyNull(entries))
            return false;
        return isValid(favourite.getID()) || isValid(favourite.getCustomerID()) || isValid(favourite.getRestaurantID());
    }

    public boolean isValid(Review review) {
        if (review == null)
            return false;
        Object[] entries = {
                review.getID(),
                review.getCustomerID(),
                review.getRestaurantID(),
                review.getDateReviewed(),
                review.getReview(),
                review.getRating(),
        };

        if (anyNull(entries))
            return false;

        return isValid(review.getID()) || isValid(review.getCustomerID()) || isValid(review.getRestaurantID());
    }
}