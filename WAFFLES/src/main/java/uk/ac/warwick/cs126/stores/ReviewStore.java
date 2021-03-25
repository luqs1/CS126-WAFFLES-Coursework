package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IReviewStore;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Review;

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

public class ReviewStore implements IReviewStore {

    private  MyAVLTree<Long, Review> reviewAVL;
    private MyAVLTree<Long, Review> blacklisted;
    private MyAVLTree<Long, MyArrayList<Review>> customersRevAVL;
    private MyAVLTree <Long, MyArrayList<Review>> bCustomersRevAVL;
    private MyAVLTree<Long, MyArrayList<Review>> olderCustomerRevAVL;
    private Sorter<Review> sorter;
    private final DataChecker dataChecker;

    public ReviewStore() {
        // Initialise variables here
        MyAVLTree<Long, Review> reviewAVL = new MyAVLTree<>(Review::getID);
        MyAVLTree<Long, Review> blacklisted = new MyAVLTree<>(Review::getID);

        Function<MyArrayList<Review>, Long> listAVLFunc = (MyArrayList<Review> list) -> list.get(0).getID();

        MyAVLTree<Long, MyArrayList<Review>> customersRevAVL = new MyAVLTree<>(listAVLFunc);
        MyAVLTree<Long, MyArrayList<Review>> bCustomersRevAVL = new MyAVLTree<>(listAVLFunc);
        MyAVLTree<Long, MyArrayList<Review>> OlderCustomerRevAVL = new MyAVLTree<>(listAVLFunc);

        Sorter<Review> sorter = new Sorter<>((Pair<Review> pair) -> pair.left.getID().compareTo(pair.right.getID()));

        dataChecker = new DataChecker();
    }

    public Review[] loadReviewDataToArray(InputStream resource) {
        Review[] reviewArray = new Review[0];

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

            Review[] loadedReviews = new Review[lineCount - 1];

            BufferedReader tsvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int reviewCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            tsvReader.readLine();
            while ((row = tsvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split("\t");
                    Review review = new Review(
                            Long.parseLong(data[0]),
                            Long.parseLong(data[1]),
                            Long.parseLong(data[2]),
                            formatter.parse(data[3]),
                            data[4],
                            Integer.parseInt(data[5]));
                    loadedReviews[reviewCount++] = review;
                }
            }
            tsvReader.close();

            reviewArray = loadedReviews;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return reviewArray;
    }

    private boolean addArrayAVL(Review review, MyAVLTree<Long,MyArrayList<Review>> arrayAVL) {
        MyArrayList<Review> list = arrayAVL.search(review.getCustomerID());
        if (list == null) {
            list = new MyArrayList<>();
            list.add(review);
            return arrayAVL.insert(list);
        }
        return list.add(review);
    }

    private boolean removeArrayAVL(Review review, MyAVLTree<Long,MyArrayList<Review>> arrayAVL){
        MyArrayList<Review> list = arrayAVL.search(review.getCustomerID());
        if (list == null)
            return false;
        if (list.size() == 1)
            arrayAVL.remove(review.getCustomerID());
        else
            list.remove(review);
        return true;
    }

    private boolean add(Review review) {
        if (review == null)
            return false;
        return reviewAVL.insert(review) & addArrayAVL(review, customersRevAVL);
    }

    private boolean remove(Review review){
        if (review == null)
            return false;
        return reviewAVL.remove(review) & removeArrayAVL(review, customersRevAVL);
    }

    private boolean bAdd(Review review) {
        if (review == null)
            return false;
        return blacklisted.insert(review) & addArrayAVL(review,bCustomersRevAVL);
    }

    private boolean bRemove(Review review) {
        if (review == null)
            return false;
        return blacklisted.remove(review) & removeArrayAVL(review, bCustomersRevAVL);
    }

    public boolean addReview(Review review) {
        // TODO

        if (!dataChecker.isValid(review) || blacklisted.search(review)) //Invalid
            return false;

        if (reviewAVL.search(review)) { //Needs to be blacklisted. Was inside favourites.
            bAdd(review) ;
            remove(review);

            /*Review[] bRevs =  intoReviewArray(bCustomersRevAVL.search(review.getCustomerID()).getArray());
            for (Review rev: bRevs) {
                if (rev.getRestaurantID().equals(review.getRestaurantID())) {
                    add(rev);
                    bRemove(rev);
                    return false; //Got blacklisted. Replacement found.
                }
            }

            Review[] oRevs = intoReviewArray(olderCustomerRevAVL.search(review.getCustomerID()).getArray());

            for (Review rev: oRevs) {
                if (rev.getRestaurantID().equals(review.getRestaurantID())) {
                    add(rev);
                    bRemove(rev);
                    return false; // Got blacklisted. Replacement found.
                }
            }*/

            return false; // No replacement found.


        }
        // Considering adding. Wasn't in favourites.

       MyArrayList<Review> fromCustomer = customersRevAVL.search(review.getCustomerID());
        /*if (fromCustomer == null) // Customer doesn't have any favourites stored.
            return add(review); // End by adding.

        for (Review rev: intoReviewArray(fromCustomer.getArray())) {
            if (rev.getRestaurantID().equals(review.getRestaurantID())) {// Colliding rev found.
                if (rev.getDateReviewed().compareTo(review.getDateReviewed()) < 0) { // The review is older
                    addArrayAVL(rev, olderCustomerRevAVL);
                    remove(rev);
                    add(review);
                    return true;
                }
                else { // The rev is older
                    addArrayAVL(review, olderCustomerRevAVL);
                    return false;
                }
            }
        }*/
        // No collisions found
        add(review);
        return true;
    }

    private Review[] intoReviewArray(Object[] unCast) {
        if (unCast == null)
            return new Review[0];
        Review[] arr = new Review[unCast.length];

        for (int i = 0; i < unCast.length; i++)
            arr[i] = (Review) unCast[i];

        return arr;
    }

    public boolean addReview(Review[] reviews) {
        if (reviews == null)
            return false;
        boolean allDone = true;

        for (Review review: reviews) {
            allDone = allDone & addReview(review);
        }

        return false;
    }

    public Review getReview(Long id) {
        // TODO
        if (id == null)
            return null;
        return reviewAVL.search(id);
    }

    public Review[] getReviews() {
        // TODO
        return intoReviewArray(reviewAVL.inorder());
    }

    private Integer dateComp(Pair<Review> pair) {
        int out = pair.right.getDateReviewed().compareTo(pair.left.getDateReviewed());
        if (out == 0)
            out = pair.left.getID().compareTo(pair.right.getID());
        return out;
    }

    public Review[] getReviewsByDate() {
        return sorter.sort(intoReviewArray(reviewAVL.inorder()), this::dateComp);
    }

    private Integer reviewComp(Pair<Review> pair) {
        int out = Integer.compare(pair.right.getRating(), pair.left.getRating());
        if (out == 0)
            out = dateComp(pair);
        return out;
    }

    public Review[] getReviewsByRating() {
        return sorter.sort(intoReviewArray(reviewAVL.inorder()),this::reviewComp);
    }

    public Review[] getReviewsByCustomerID(Long id) {
        // TODO
        if (id == null)
            return new Review[0];
        MyArrayList<Review> list = customersRevAVL.search(id);
        if (list == null)
            return new Review[0];
        return sorter.sort(intoReviewArray(list.getArray()), this::dateComp);
    }

    public Review[] getReviewsByRestaurantID(Long id) {
        if (id == null)
            return new Review[0];
        MyArrayList<Review> list = new MyArrayList<>();
        for (Review review : getReviews()) {
            if (review.getRestaurantID().equals(id))
                list.add(review);
        }

        Review[] out = intoReviewArray(list.getArray()) ;

        sorter.sort(out, this::dateComp);

        return out;
    }

    public float getAverageCustomerReviewRating(Long id) {
        // TODO
        return 0.0f;
    }

    public float getAverageRestaurantReviewRating(Long id) {
        // TODO
        return 0.0f;
    }

    public int[] getCustomerReviewHistogramCount(Long id) {
        // TODO
        return new int[5];
    }

    public int[] getRestaurantReviewHistogramCount(Long id) {
        // TODO
        return new int[5];
    }

    public Long[] getTopCustomersByReviewCount() {
        // TODO
        return new Long[20];
    }

    public Long[] getTopRestaurantsByReviewCount() {
        // TODO
        return new Long[20];
    }

    public Long[] getTopRatedRestaurants() {
        // TODO
        return new Long[20];
    }

    public String[] getTopKeywordsForRestaurant(Long id) {
        // TODO
        return new String[5];
    }

    public Review[] getReviewsContaining(String searchTerm) {
        // TODO
        // String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        // String searchTermConvertedFaster = stringFormatter.convertAccentsFaster(searchTerm);
        return new Review[0];
    }
}
