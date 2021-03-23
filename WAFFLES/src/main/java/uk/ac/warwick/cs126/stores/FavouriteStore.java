package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Favourite;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.structures.MyAVLTree;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.Pair;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.Sorter;

public class FavouriteStore implements IFavouriteStore {

    private final MyAVLTree<Long, Favourite> favouriteAVL;
    private final MyAVLTree<Long, Favourite> blacklisted;
    private final MyAVLTree<Long, Favourite> youngerFavourites;
    private final DataChecker dataChecker;
    private final Sorter<Favourite> sorter;
    private final Sorter<Link> linkSorter;

    public FavouriteStore() {
        // Initialise variables here
        favouriteAVL = new MyAVLTree<>(Favourite::getID);
        blacklisted = new MyAVLTree<>(Favourite::getID);
        youngerFavourites = new MyAVLTree<>(Favourite::getID);
        dataChecker = new DataChecker();

        Function<Pair<Favourite>, Integer> defaultComp = (Pair<Favourite> pair)
                -> (pair.left.getID().compareTo(pair.right.getID()));

        sorter = new Sorter<>(defaultComp);
        linkSorter = new Sorter<>(this::topComp);
    }

    public Favourite[] loadFavouriteDataToArray(InputStream resource) {
        Favourite[] favouriteArray = new Favourite[0];

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

            Favourite[] loadedFavourites = new Favourite[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int favouriteCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");
                    Favourite favourite = new Favourite(
                            Long.parseLong(data[0]),
                            Long.parseLong(data[1]),
                            Long.parseLong(data[2]),
                            formatter.parse(data[3]));
                    loadedFavourites[favouriteCount++] = favourite;
                }
            }
            csvReader.close();

            favouriteArray = loadedFavourites;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return favouriteArray;
    }

    public boolean addFavourite(Favourite favourite) {
        if (!dataChecker.isValid(favourite) || blacklisted.search(favourite))
            return false;

        if (favouriteAVL.search(favourite)) {
            blacklisted.insert(favourite);
            favouriteAVL.remove(favourite);

            Favourite[] a = intoFavouriteArray(youngerFavourites.inorder());

            Favourite[] b = intoFavouriteArray(blacklisted.inorder());

            Favourite[] potentials = new Favourite[a.length + b.length];
            System.arraycopy(a, 0, potentials, 0, a.length);
            System.arraycopy(b, 0, potentials, a.length, b.length);

            for (Favourite f : potentials) {
                if (f.getRestaurantID().equals(favourite.getRestaurantID())
                        && f.getCustomerID().equals(favourite.getCustomerID())) {
                    favouriteAVL.insert(f);
                    blacklisted.remove(f);
                    youngerFavourites.remove(f);
                }
            }

            return false;
        }


        Favourite[] customersFaves = getFavouritesByCustomerID(favourite.getCustomerID());
        boolean inside = false;
        boolean insideNewer = false;
        Favourite other = null;
        for (Favourite f : customersFaves) {
            if (f.getCustomerID().equals(favourite.getCustomerID()) && //Better to short circuit the customer.
                    f.getRestaurantID().equals(favourite.getRestaurantID())) {
                inside = true;
                if (f.getDateFavourited().compareTo(favourite.getDateFavourited()) > 0) {
                    insideNewer = true;
                    other = f;
                }
            }
        }

        if (!inside) {
            favouriteAVL.insert(favourite);
            return true;
        }

        if (insideNewer) {
            favouriteAVL.remove(other);
            blacklisted.insert(other);
            favouriteAVL.insert(favourite);
            return true;
        }

        youngerFavourites.insert(favourite);
        return false;
    }

    public boolean addFavourite(Favourite[] favourites) {
        if (favourites == null)
            return false;
        boolean allPassed = true;
        for (Favourite favourite : favourites)
            allPassed = allPassed & addFavourite(favourite); // Important that this doesn't short circuit.
        return allPassed;
    }

    private Favourite[] intoFavouriteArray(Object[] unCast) {
        Favourite[] arr = new Favourite[unCast.length];

        for (int i = 0; i < unCast.length; i++)
            arr[i] = (Favourite) unCast[i];

        return arr;
    }

    public Favourite getFavourite(Long id) {
        if (id == null)
            return null;
        return favouriteAVL.search(id);
    }

    public Favourite[] getFavourites() {
        return intoFavouriteArray(favouriteAVL.inorder());
    }

    private Integer dateComp(Pair<Favourite> pair) {
        int out = pair.right.getDateFavourited().compareTo(pair.left.getDateFavourited());
        if (out == 0)
            out = pair.left.getID().compareTo(pair.right.getID());
        return out;
    }

    public Favourite[] getFavouritesByCustomerID(Long id) {
        if (id == null)
            return new Favourite[0];
        MyArrayList<Favourite> list = new MyArrayList<>();
        for (Favourite f : getFavourites()) {
            if (f.getCustomerID().equals(id))
                list.add(f);
        }

        Favourite[] out = intoFavouriteArray(list.getArray());

        sorter.sort(out, this::dateComp);

        return out;
    }

    public Favourite[] getFavouritesByRestaurantID(Long id) {
        if (id == null)
            return new Favourite[0];
        MyArrayList<Favourite> list = new MyArrayList<>();
        for (Favourite f : getFavourites()) {
            if (f.getRestaurantID().equals(id))
                list.add(f);
        }

        Favourite[] out = intoFavouriteArray(list.getArray());

        sorter.sort(out, this::dateComp);

        return out;
    }

    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if (customer1ID == null || customer2ID == null)
            return new Long[0];
        MyAVLTree<Long, Favourite> c1Fav = new MyAVLTree<>(Favourite::getRestaurantID);
        MyAVLTree<Long, Favourite> c2Fav = new MyAVLTree<>(Favourite::getRestaurantID);
        MyArrayList<Favourite> inCommon = new MyArrayList<>();

        for (Favourite f : getFavouritesByCustomerID(customer1ID)) {
            c1Fav.insert(f); // By nature of how faves are stored, this shouldn't ever cause a conflict.
        }

        for (Favourite f : getFavouritesByCustomerID(customer2ID)) {
            c2Fav.insert(f);
        }

        for (Favourite f : intoFavouriteArray(c1Fav.inorder())) {
            Long rId = f.getRestaurantID();
            Favourite a = c1Fav.search(rId);
            Favourite b = c2Fav.search(rId);

            if (a == null || b == null)
                continue;
            if (a.getDateFavourited().compareTo(b.getDateFavourited()) > 0)
                inCommon.add(a);
            else
                inCommon.add(b);
        }

        Favourite[] commonArray = intoFavouriteArray(inCommon.getArray());

        sorter.sort(commonArray, this::dateComp);

        Long[] ids = new Long[commonArray.length];

        for (int i = 0; i < commonArray.length; i++) {
            ids[i] = commonArray[i].getRestaurantID();
        }

        return ids;
    }

    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if (customer1ID == null || customer2ID == null)
            return new Long[0];
        MyAVLTree<Long, Favourite> c1Fav = new MyAVLTree<>(Favourite::getRestaurantID);
        MyAVLTree<Long, Favourite> c2Fav = new MyAVLTree<>(Favourite::getRestaurantID);
        MyArrayList<Favourite> ADiffB = new MyArrayList<>();

        for (Favourite f : getFavouritesByCustomerID(customer1ID)) {
            c1Fav.insert(f); // By nature of how faves are stored, this shouldn't ever cause a conflict.
        }

        for (Favourite f : getFavouritesByCustomerID(customer2ID)) {
            c2Fav.insert(f);
        }

        for (Favourite f : intoFavouriteArray(c1Fav.inorder())) {
            Long rId = f.getRestaurantID();
            Favourite a = c1Fav.search(rId);
            Favourite b = c2Fav.search(rId);

            if (b == null)
                ADiffB.add(a);
        }

        Favourite[] diffArray = intoFavouriteArray(ADiffB.getArray());

        sorter.sort(diffArray, this::dateComp);

        Long[] ids = new Long[diffArray.length];

        for (int i = 0; i < diffArray.length; i++) {
            ids[i] = diffArray[i].getRestaurantID();
        }

        return ids;
    }

    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        if (customer1ID == null || customer2ID == null)
            return new Long[0];
        MyAVLTree<Long, Favourite> c1Fav = new MyAVLTree<>(Favourite::getRestaurantID);
        MyAVLTree<Long, Favourite> c2Fav = new MyAVLTree<>(Favourite::getRestaurantID);
        MyArrayList<Favourite> notABCommon = new MyArrayList<>();

        for (Favourite f : getFavouritesByCustomerID(customer1ID)) {
            c1Fav.insert(f); // By nature of how faves are stored, this shouldn't ever cause a conflict.
        }

        for (Favourite f : getFavouritesByCustomerID(customer2ID)) {
            c2Fav.insert(f);
        }

        for (Favourite f : intoFavouriteArray(c1Fav.inorder())) {
            Long rId = f.getRestaurantID();
            Favourite a = c1Fav.search(rId);
            Favourite b = c2Fav.search(rId);

            if (b == null)
                notABCommon.add(a);
        }

        for (Favourite f : intoFavouriteArray(c2Fav.inorder())) {
            Long rId = f.getRestaurantID();
            Favourite a = c1Fav.search(rId);
            Favourite b = c2Fav.search(rId);

            if (a == null)
                notABCommon.add(b);

        }

        Favourite[] diffArray = intoFavouriteArray(notABCommon.getArray());

        sorter.sort(diffArray, this::dateComp);

        Long[] ids = new Long[diffArray.length];

        for (int i = 0; i < diffArray.length; i++) {
            ids[i] = diffArray[i].getRestaurantID();
        }

        return ids;
    }

    private Integer topComp(Pair<Link> linkPair) {
        int out = linkPair.right.count.compareTo(linkPair.left.count);
        if (out == 0)
            out = linkPair.left.fave.getDateFavourited().compareTo(
                    linkPair.right.fave.getDateFavourited()
            );
        if (out == 0)
            out = linkPair.left.id.compareTo(linkPair.right.id);
        return out;
    }

    private Long[] getTop(Function<Favourite, Long> which) {
        {
            MyAVLTree<Long, Link> linkTree =
                    new MyAVLTree<>((Link link) ->
                            (link.id));

            for (Favourite f : getFavourites()) {
                Link link = linkTree.search(which.apply(f));
                if (link == null)
                    linkTree.insert(new Link(which.apply(f), f, 1));
                else {
                    if (f.getDateFavourited().compareTo(link.fave.getDateFavourited()) > 0)
                        link.fave = f;
                    link.count++;
                }
            }
            Object[] unCast = linkTree.inorder();
            Link[] arr = new Link[unCast.length];

            for (int i = 0; i < unCast.length; i++)
                arr[i] = (Link) unCast[i];

            linkSorter.sort(arr);

            Long[] top = new Long[20];

            int z = 20;
            if (arr.length < 20)
                z = arr.length;

            for (int i = 0; i < z; i++)
                top[i] = arr[i].id;
            return top;
        }
    }

    public Long[] getTopCustomersByFavouriteCount() {
        return getTop(Favourite::getCustomerID);
    }

    public Long[] getTopRestaurantsByFavouriteCount() {
        return getTop(Favourite::getRestaurantID);
    }
}
