package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IReviewStore;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Review;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyAVLTree;
import uk.ac.warwick.cs126.structures.MyArrayList;

import uk.ac.warwick.cs126.structures.Pair;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.KeywordChecker;
import uk.ac.warwick.cs126.util.Sorter;
import uk.ac.warwick.cs126.util.StringFormatter;

public class ReviewStore implements IReviewStore {

    private  MyAVLTree<Long, Review> reviewAVL;
    private MyAVLTree<Long, Review> blacklisted;
    private MyAVLTree<Long, MyArrayList<Review>> customersRevAVL;
    private MyAVLTree <Long, MyArrayList<Review>> bCustomersRevAVL;
    private MyAVLTree<Long, MyArrayList<Review>> OlderCustomerRevAVL;
    private Sorter<Review> sorter;
    private final DataChecker dataChecker;
    private final Sorter<RLink> linkSorter;
    private final Sorter<RestaurantRating> rrSorter;
    private final Sorter<KeywordCounter> kwSorter;
    private final KeywordChecker keywordChecker;

    public ReviewStore() {
        // Initialise variables here
        reviewAVL = new MyAVLTree<>(Review::getID);
        blacklisted = new MyAVLTree<>(Review::getID);

        Function<MyArrayList<Review>, Long> listAVLFunc = (MyArrayList<Review> list) -> list.get(0).getID();

        customersRevAVL = new MyAVLTree<>(listAVLFunc);
        bCustomersRevAVL = new MyAVLTree<>(listAVLFunc);
        OlderCustomerRevAVL = new MyAVLTree<>(listAVLFunc);

        sorter = new Sorter<>((Pair<Review> pair) -> pair.left.getID().compareTo(pair.right.getID()));
        rrSorter = new Sorter<>((Pair<RestaurantRating> rrPair) -> {
            int out;
            if (rrPair.left.totalRating == 0 && rrPair.right.totalRating == 0)
                    return dateComp(new Pair<>(rrPair.left.latestReview, rrPair.right.latestReview));
            else if (rrPair.left.totalRating == 0)
                out = -1;
            else if (rrPair.right.totalRating == 0)
                out = 1;
            else {
                float av1 = rrPair.left.totalRating / (float) rrPair.left.count;
                float av2 = rrPair.right.totalRating/ (float) rrPair.right.count;
                out = Float.compare(av2,av1);
            }
            return out;
        }
        );
        linkSorter = new Sorter<>(this::topComp);
        kwSorter = new Sorter<>((Pair<KeywordCounter> pairKC) -> {
            int out = Integer.compare(pairKC.right.count ,pairKC.left.count);
            if (out == 0)
                out = pairKC.left.keyword.compareTo(pairKC.right.keyword);
            return out;
        });


        dataChecker = new DataChecker();
        keywordChecker = new KeywordChecker();

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
        Review[] all = getReviewsByCustomerID(id);
        if (all.length == 0)
            return 0.0f;
        float ratingSum = 0f;
        for (Review review: all)
            ratingSum += review.getRating();
        return ratingSum/ all.length;
    }

    public float getAverageRestaurantReviewRating(Long id) {
        Review[] all = getReviewsByRestaurantID(id);
        if (all.length == 0)
            return 0.0f;
        float ratingSum = 0f;
        for (Review review: all)
            ratingSum += review.getRating();
        return ratingSum / all.length;
    }

    public int[] getCustomerReviewHistogramCount(Long id) {
        Review[] all = getReviewsByCustomerID(id);
        int[] counts = new int[5];
        for (Review review: all)
            counts[review.getRating()]++;
        return counts;
    }

    public int[] getRestaurantReviewHistogramCount(Long id) {
        Review[] all = getReviewsByRestaurantID(id);
        int[] counts = new int[5];
        for (Review review: all)
            counts[review.getRating()]++;
        return counts;
    }

    public Long[] getTopCustomersByReviewCount() {
        return getTop(Review::getCustomerID);
    }

    public Long[] getTopRestaurantsByReviewCount() {
        return getTop(Review::getRestaurantID);
    }

    private Integer topComp(Pair<RLink> linkPair) {
        int out = linkPair.right.count.compareTo(linkPair.left.count);
        if (out == 0)
            out = dateComp(new Pair<>(linkPair.left.rev, linkPair.right.rev));
        return out;
    }

    private Long[] getTop(Function<Review, Long> which) {
        MyAVLTree<Long, RLink> linkTree =
                new MyAVLTree<>((RLink link) ->
                        (link.id));

        for (Review review : getReviews()) {
            RLink link = linkTree.search(which.apply(review));
            if (link == null)
                linkTree.insert(new RLink(which.apply(review), review, 1));
            else {
                if (review.getDateReviewed().compareTo(link.rev.getDateReviewed()) > 0)
                    link.rev = review;
                link.count++;
            }
        }
        Object[] unCast = linkTree.inorder();
        RLink[] arr = new RLink[unCast.length];

        for (int i = 0; i < unCast.length; i++)
            arr[i] = (RLink) unCast[i];

        linkSorter.sort(arr);

        Long[] top = new Long[20];

        int z = 20;
        if (arr.length < 20)
            z = arr.length;

        for (int i = 0; i < z; i++)
            top[i] = arr[i].id;
        return top;
    }

    static class RestaurantRating {
        private final Long restaurantID;
        protected int count;
        protected int totalRating;
        protected Review latestReview;


        public RestaurantRating(Long restaurantID) {
            this.restaurantID = restaurantID;
            this.count = 0;
            this.totalRating = 0;
        }

        public void addReview(Review review) {
            this.count++;
            this.totalRating += review.getRating();
            if (this.latestReview == null)
                latestReview = review;
            else if (latestReview.getDateReviewed().before(review.getDateReviewed()))
                latestReview = review;
        }
    }

    public Long[] getTopRatedRestaurants() {
        MyAVLTree<Long, RestaurantRating> rrTree = new MyAVLTree<>((RestaurantRating RR) -> (RR.restaurantID));

        for (Review review: getReviews()) {
            RestaurantRating RR = rrTree.search(review.getRestaurantID());
            if (RR == null) {
                RR = new RestaurantRating(review.getRestaurantID());
                rrTree.insert(RR);
            }
            RR.addReview(review);
        }

        Object[] unCast = rrTree.inorder();
        RestaurantRating[] arr = new RestaurantRating[unCast.length];

        for (int i = 0; i < unCast.length; i++)
            arr[i] = (RestaurantRating) unCast[i];

        rrSorter.sort(arr);

        Long[] top = new Long[20];

        for (int i=0; i<20;i++)
            top[i] = arr[i].restaurantID;
        return top;

    }

    static class KeywordCounter {
        private String keyword;
        protected int count;
        public KeywordCounter(String keyword) {
            this.keyword = keyword;
            this.count = 0;
        }

        public String getKeyword() {
            return keyword;
        }
    }

    public String[] getTopKeywordsForRestaurant(Long id) {
        Review[] all = getReviewsByRestaurantID(id);
        if (all.length == 0)
            return new String[5];
        MyAVLTree<String, KeywordCounter> keywordAVL = new MyAVLTree<>(KeywordCounter::getKeyword);

        for (Review review: all) {
            String[] words = review.getReview().split(" ");
            for (String word: words) {
                if (!keywordChecker.enhancedCheck(word))
                    continue;
                KeywordCounter KC = keywordAVL.search(word);
                if (KC == null) {
                    KC = new KeywordCounter(word);
                    keywordAVL.insert(KC);
                }
                KC.count++;
            }
        }

        Object[] unCast = keywordAVL.inorder();
        KeywordCounter[] arr = new KeywordCounter[unCast.length];

        for (int i=0;i<unCast.length;i++)
            arr[i] =  (KeywordCounter) unCast[i];

        kwSorter.sort(arr);

        String[] top = new String[5];

        for (int i =0;i<5;i++)
            top[i] = arr[i].keyword;

        return top;

    }

    public Review[] getReviewsContaining(String searchTerm) {

        if (searchTerm == null || searchTerm.equals("")) {
            return new Review[0];
        }
        String searchTermProcessed = StringFormatter.
                convertAccentsFaster(searchTerm).
                toLowerCase().
                replaceAll("[ ]{2,}", " "); // this replaces all 2 or more whitespaces with 1.
        Review[] arr1 = getReviewsByDate();
        MyArrayList<Review> list = new MyArrayList<>();

        for (Review review : arr1) {
            if (review.getReview().contains(searchTermProcessed))
                list.add(review);
        }

        return intoReviewArray(list.getArray());
    }
}
