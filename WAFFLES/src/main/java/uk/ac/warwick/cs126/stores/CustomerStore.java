package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.ICustomerStore;
import uk.ac.warwick.cs126.models.Customer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyAVLTree;
import uk.ac.warwick.cs126.structures.MyArrayList;

import uk.ac.warwick.cs126.structures.Pair;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.Sorter;
import uk.ac.warwick.cs126.util.StringFormatter;

public class CustomerStore implements ICustomerStore {

    private final MyAVLTree<Long, Customer> customerAVL;
    private final MyAVLTree<Long, Customer> blacklisted;
    private final DataChecker dataChecker;
    private final Sorter<Customer> sorter;

    public CustomerStore() {
        // Initialise variables here
        customerAVL = new MyAVLTree<>(Customer::getID);
        blacklisted = new MyAVLTree<>(Customer:: getID);
        dataChecker = new DataChecker();
        Function<Pair<Customer>, Integer> comp =
                (Pair<Customer> pair) -> (pair.left.getID().compareTo(pair.right.getID()));
        sorter = new Sorter<>(comp);
    }

    public Customer[] loadCustomerDataToArray(InputStream resource) {
        Customer[] customerArray = new Customer[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line=lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Customer[] loadedCustomers = new Customer[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int customerCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Customer customer = (new Customer(
                            Long.parseLong(data[0]),
                            data[1],
                            data[2],
                            formatter.parse(data[3]),
                            Float.parseFloat(data[4]),
                            Float.parseFloat(data[5])));

                    loadedCustomers[customerCount++] = customer;
                }
            }
            csvReader.close();

            customerArray = loadedCustomers;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return customerArray;
    }

    public boolean addCustomer(Customer customer) {
        if (dataChecker.isValid(customer) && !blacklisted.search(customer)) {
            if (customerAVL.search(customer)) {
                blacklisted.insert(customer);
                return false;
            }
            customerAVL.insert(customer);
            return true;
        }
        else
            return false;
    }

    public boolean addCustomer(Customer[] customers) {
        if (customers == null)
            return false;
        boolean allDone = true;
        for (Customer customer: customers) {
            allDone = allDone && addCustomer(customer);
        }
        return allDone;
    }

    private Customer[] getCopy(Customer[] customers) { // Important to prevent side effects to original array
        Customer[] copy = new Customer[customers.length];
        System.arraycopy(customers,0,copy,0,customers.length);
        return copy;
    }

    public Customer getCustomer(Long id) {
        if (id == null)
            return null;
        return customerAVL.search(id);
    }

    public Customer[] getCustomers() { // Didn't use method below because this is a feature of AVLTree.
        Object[] unCast = customerAVL.inorder();
        Customer[] out = new Customer[unCast.length]; // Issues with generic arrays forced this.
        for (int i=0; i<unCast.length; i++) {
            out[i] = (Customer) unCast[i];
        }
        return out;
    }

    public Customer[] getCustomers(Customer[] customers) {
        return sorter.sort(getCopy(customers));
    }

    private Integer nameComparison(Pair<Customer> pair) {
        Customer l = pair.left;
        Customer r = pair.right;
        int out = l.getLastName().toLowerCase().
                compareTo(r.getLastName().toLowerCase());
        if (out != 0)
            return out;
        out = l.getFirstName().toLowerCase().
                compareTo(r.getFirstName().toLowerCase());
        if (out != 0)
            return out;
        return l.getID().compareTo(r.getID());
    }

    public Customer[] getCustomersByName() {
        return getCustomersByName(getCustomers());
    }

    public Customer[] getCustomersByName(Customer[] customers) {
        return sorter.sort(getCopy(customers),this::nameComparison);
    }

    public Customer[] getCustomersContaining(String searchTerm) {
        // TODO
        // String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        // String searchTermConvertedFaster = stringFormatter.convertAccentsFaster(searchTerm);
        return new Customer[0];
    }

}
