package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IDataChecker;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Review;

import java.util.Date;

public class DataChecker implements IDataChecker {

    public DataChecker() {
        // Initialise things here
    }

    public static boolean allNotNull(Object[] objects) {
        for (Object a: objects) {
            if (a == null)
                return false;
        }
        return true;
    }

    public Long extractTrueID(String[] repeatedID) {
        // TODO
        return null;
    }

    public boolean isValid(Long inputID) {
        if (inputID == null)
            return false;
        String id = inputID.toString();

        if (id.length() != 16)
            return false;
        char[] digits = {'1','2','3','4','5','6','7','8','9'};
        int[] counts = new int[9];

        for (int i=0; i<id.length(); i++) {
            boolean found = false;
            for (int j=0; j<9; j++) {
                if (id.charAt(i) == digits[j]) {
                    found = true;
                    counts[j]++;
                    break;
                }
            }
            if (!found)
                return false;
        }
        for (int count: counts) {
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
        if (!allNotNull(entries))
            return false;
        return isValid(customer.getID());
    }

    public boolean isValid(Restaurant restaurant) {
        // TODO
        return false;
    }

    public boolean isValid(Favourite favourite) {
        // TODO
        return false;
    }

    public boolean isValid(Review review) {
        // TODO
        return false;
    }
}