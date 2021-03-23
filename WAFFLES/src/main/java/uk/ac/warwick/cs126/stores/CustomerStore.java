package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.ICustomerStore;
import uk.ac.warwick.cs126.models.Customer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        Function<Pair<Customer>, Integer> defaultComp =
                (Pair<Customer> pair) -> (pair.left.getID().compareTo(pair.right.getID()));
        sorter = new Sorter<>(defaultComp);
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
        if (!dataChecker.isValid(customer) || blacklisted.search(customer))
            return false;

        if (customerAVL.search(customer)) {
            blacklisted.insert(customer);
            customerAVL.remove(customer);
            return false;
        }

        customerAVL.insert(customer);
        return true;
    }

    public boolean addCustomer(Customer[] customers) {
        if (customers == null)
            return false;
        boolean allDone = true;
        for (Customer customer: customers) {
            allDone = allDone & addCustomer(customer);
        }
        return allDone;
    }

    private Customer[] intoCustomerArray(Object[] unCast) {
        if (unCast == null)
            return null;
        Customer[] arr = new Customer[unCast.length];
        for (int i=0;i< unCast.length;i++)
            arr[i] = (Customer) unCast[i];

        return arr;
    }

    private Customer[] getCopy(Customer[] customers) { // Important to prevent side effects to original array
        if (customers == null)
            return new Customer[0];
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
        return intoCustomerArray(customerAVL.inorder());
    }

    public Customer[] getCustomers(Customer[] customers) {
        if (customers == null)
            return new Customer[0];
        return sorter.sort(getCopy(customers));
    }

    private Integer nameComparison(Pair<Customer> pair) {
        Customer l = pair.left;
        Customer r = pair.right;
        int out = l.getLastName().toLowerCase().
                compareTo(r.getLastName().toLowerCase());
        if (out == 0)
            out = l.getFirstName().toLowerCase().
                compareTo(r.getFirstName().toLowerCase());
        if (out == 0)
            out = l.getID().compareTo(r.getID());
        return out;
    }

    public Customer[] getCustomersByName() {
        return getCustomersByName(getCustomers());
    }

    public Customer[] getCustomersByName(Customer[] customers) {
        if (customers == null)
            return new Customer[0];
        return sorter.sort(getCopy(customers),this::nameComparison);
    }

    public Customer[] getCustomersContaining(String searchTerm) {
        if (searchTerm == null || searchTerm.equals("")) {
            return new Customer[0];
        }
        String searchTermProcessed = StringFormatter.
                convertAccentsFaster(searchTerm).
                toLowerCase().
                replaceAll("[ ]{2,}", " "); // this replaces all 2 or more whitespaces with 1.
        Customer[] arr1 = getCustomersByName();
        MyArrayList<Customer> list = new MyArrayList<>();

        for (Customer customer: arr1) {
            boolean isInName = (customer.getLastName() + customer.getFirstName())
                    .toLowerCase()
                    .contains(searchTermProcessed);

            if (isInName)
                list.add(customer);
        }

        return intoCustomerArray(list.getArray());
    }

}
