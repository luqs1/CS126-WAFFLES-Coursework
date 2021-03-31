# CS126 WAFFLES Coursework Report [2003231]
<!-- This document gives a brief overview about your solution.  -->
<!-- You should change number in the title to your university ID number.  -->
<!-- You should delete these comments  -->
<!-- And for the latter sections should delete and write your explanations in them. -->
<!-- # <-- Indicates heading, ## <-- Indicates subheading, and so on -->

## Data Structures
The most important data structure I've used in this coursework is my `AVLTree` implementation. I also had to build a `Node` implementation as a prerequisite.

### Why not a `HashMap`?

I was contemplating whether to use an `AVLTree` or a `HashMap`; whilst I thought a `HashMap` would be easier to implement giving me more time to work on the Stores, I thought it had some points that needed addressing.

1. Setting the initial size.
2. Rebuilding the internal array once it became too full.

Point 1: HashMaps work best when there's a prime number of array spaces and/ or when we have well defined hashing functions. A combination of both can lead to an amortised O(1) add/ delete which is desirable. However, it is hard to know how many elements will be in a `HashMap` (which is a major factor in deciding initial size) its even harder to calculate a prime number in that vicinity. The second point can be addressed by having a static array of many prime numbers but I still find this solution inelegant.

Point 2: When the HashMap inevitably has to resize, it is important to consider how costly an operation it might be if you want to spread out the previous elements in the new array. It's possible that one can come up with a simplification so that the hash doesn't need to be reapplied to every element but again I find this hasslesome.

### Benefits of `AVLTree`

The amount of human coding time that was required to build the `AVLTree` cannot be understated, and testing to see if it worked adequately was equally challenging. However, it is labor well spent. The `AVLTree` has O(logn) add/ delete and search. Most importantly however, it is consistent, requires no additional rebalancing/ resizing and works in various circumstances.

If I simply used a BST, I'd be gambling on the time complexity. The `AVLTree`'s _self-balancing_ means that it would take longer that if it didnt have to balance itself, but it gurantees performance of O(logn).

### My Implementation
The type of my `AVLTree` is `AVLTree<K extends Comparable<K>, V>`, it also requires a `Function<V, K>` at initialisation. To summarise, I can store non-comparable values with comparable keys, providing I give a function that instructs the Tree how to get the key `K` from the value `V`. With the restriction that `K` is unique relative to the rest of the tree.

For example: `MyAVLTree<Integer, String> customers = new MyAVLTree<>(Customer::getID())` would allow me to store Customers (not comparable) and use their IDs (comparable and unique) as keys. This provides the additional benefit that if I want a `Customer[]` in order of their IDs, i can simply do an `inorder` traversal.

### Sorter
The sorter is a tool to use binary sort on an array. I noticed once again that it would be wasteful to only work on `Comparable` elements or to even insist on only a select way of comparing two elements even if they were `Comparable`. So my sorter would ask for a function `Function<Pair<V>, Integer>` where a `Pair<V>` is just two elements of type `V` (the two being compared). This function works in the same spirit as a comparator and allows me to use comparators on different attributes of whatever I'm sorting. This proved very versatile and was of great aid in implementing the Stores.




## Every Store
### Overview

* Used AVLTree for store and blacklisted.
* Used Sorter for sorting.
### Space Complexity
Store         | Worst Case | Description
------------- | ---------- | -----------
Customer and Restaurant Store | O(n)       | I have 2 `AVLTrees` to store entities. <br> Where `n` is total entities added.
Favourite and Review Store | O(n)   | I have 5 `AVLTrees` to store entities. <br> Where `n` is total entities added.

### Time Complexity

Method                           | Average Case     | Description
-------------------------------- | ---------------- | -----------
addEntity(Entity c)              | O(logn)      | Tree add is log(n) time <br> `n` is the number of entities in store.
addEntity(Entity[] c)            | O(nlog(n+r)) | Add all entities <br>`n` is the length of the input array <br> `r` is the number of entities in store.
getEntity(Long id)               | O(log(n))    | Tree Search <br>`n` is total entities in the store
getEntities()                    | O(n)         | Tree in order traversal <br>`n` is total entities in the store.
getEntites(Entities[] c)         | O(nlogn)     | MergeSort <br>`n` is the length of the input array.
getCustomers/RestaurantsByName() | O(nlogn)     | MergeSort <br>`n` is total entities in the store.
getCustomers/RestaurantsByName(Entities[] c) | O(log(n)) | MergeSort <br>`n` is length of input array.
getCustomers/RestaurantsContaining(String s) | O(nlog(n) + k) | Searches all customers and filters. Time for string convert is based on length `k`.

<div style="page-break-after: always;"></div>

## FavouriteStore Specifically

### Time Complexity
Method                                                          | Average Case     | Description
--------------------------------------------------------------- | ---------------- | -----------
getFavouritesByCustomerID(Long id)                              | O(logn)          | Have an AVL of Customers <br>`n` is number of customers.
getFavouritesByRestaurantID(Long id)                            | O(n)             | No AVL of Restaurants, normal search <br>`n` is number of favourites.
getCommonFavouriteRestaurants(<br>&emsp; Long id1, Long id2)    | O(logn + r)      | Two customer AVL calls and search through all <br>`n` is number of customers, `r` is max length of both customer favourites arrays.
getMissingFavouriteRestaurants(<br>&emsp; Long id1, Long id2)   | As above         | As above
getNotCommonFavouriteRestaurants(<br>&emsp; Long id1, Long id2) | As above         | As above
getTopCustomersByFavouriteCount()                               | As above         | As above
getTopRestaurantsByFavouriteCount()                             | As above         | As above.

<div style="page-break-after: always;"></div>

## RestaurantStore Specifically
### Time Complexity
Method                                                                        | Average Case     | Description
----------------------------------------------------------------------------- | ---------------- | -----------
getRestaurantsByDateEstablished()                                             | O(logn)          | Description <br>`...` is ...
getRestaurantsByDateEstablished(Restaurant[] r)                    | O(nlogn)           | MergeSort <br>`n` is length of input array.
getRestaurantsByWarwickStars()                                                | O(nlogn)           | MergeSort <br>`n` is number of restaurants stored.
getRestaurantsByRating(Restaurant[] r)                                        | O(nlogn)           | Merge Sort <br>`n` is length of input array
getRestaurantsByDistanceFrom(float lat, float lon)                 | O(nlogn)           | MergeSort using distance as comparator <br>`n` is the number of restaurants. Getting distance is constant time so is ommited.
getRestaurantsByDistanceFrom(Restaurant[] r, float lat, float lon) | O(nlogn)           | As above but `n` is length of input array.


<div style="page-break-after: always;"></div>

## ReviewStore Specifically

### Time Complexity
Method                                     | Average Case     | Description
------------------------------------------ | ---------------- | -----------
getReviewsByDate()                         | O(nlogn)           | MergeSort <br>`n` is number of reviews.
getReviewsByRating()                       | As above          | As above.
getReviewsByRestaurantID(Long id)          | O(n)           | Linear Search traversal. `n` is number of reviews.
getReviewsByCustomerID(Long id)            | As above          | As above
getAverageCustomerReviewRating(Long id)    | O(n + k)           | Linear Search followed by Linear calculation. `n` is number of reviews, `k` is the number of customer's reviews.
getAverageRestaurantReviewRating(Long id)  | As above         | As above, but `k` is the number of restaurant's reviews.
getCustomerReviewHistogramCount(Long id)   | As above         | As above, `k` is the number of customer's reviews.
getRestaurantReviewHistogramCount(Long id) | As above          | As above, `k` is the number of restaurant's reviews.
getTopCustomersByReviewCount()             | O(nlogn)          | Linear and tree then merge sort, `n` is the number of reviews.
getTopRestaurantsByReviewCount()           | As above        | As above.
getTopRatedRestaurants()                   | As above        | As above.
getTopKeywordsForRestaurant(Long id)       | O(n +klogk)     | Due to getReviewsbyRestaurantID being O(n), `n` is number of reviews in store. Then `k` is the number of restaurant's reviews.
getReviewsContaining(String s)             | O(nlogn +k)        | Same as getReviewsByDate, `n` is number of reviews, Accent conversion is based on string length `k`.

<div style="page-break-after: always;"></div>

## Util
### Overview
* **ConvertToPlace** 
    * Used AVLTree of linked lists.
* **DataChecker**
    * Nothing special.
* **HaversineDC**
    * Just some float calculations.
* **KeywordChecker**
    * Used AVLTree based on hashCode.
* **StringFormatter**
    * Used AVLTree.

### Space Complexity
Util               | Worst Case | Description
-------------------| ---------- | -----------
ConvertToPlace     | O(n)     | Has a tree. `n` is number of places.
KeywordChecker     | O(n)     | Has a tree. `n` is number of keywords.
StringFormatter    | O(n)     | Has a tree. `n` is number of letter convert entires.

Rest are O(1) or N/A.

### Time Complexity
Util              | Method                                                                             | Average Case     | Description
----------------- | ---------------------------------------------------------------------------------- | ---------------- | -----------
ConvertToPlace    | convert(float lat, float lon)                                                      | O(logn + c)           | Uses a tree structure with linked lists. `c` is the number of places with same lat.
DataChecker       | extractTrueID(String[] repeatedID)                                                 | O(1)           | No dependent variables.
DataChecker       | isValid(Long id)                                                                   | O(1)           | Reads through ID. IDs have to be length 16 to be checked further.
DataChecker       | isValid(Customer customer)                                                         | O(1)           | Nothing here is variable dependent.
DataChecker       | isValid(Favourite favourite)                                                       | As above       | As above
DataChecker       | isValid(Restaurant restaurant)                                                     | As above        | As above
DataChecker       | isValid(Review review)                                                             | As above       | As above
HaversineDC       | inKilometres(<br>&emsp; float lat1, float lon1, <br>&emsp; float lat2, float lon2) | As above          | As above
HaversineDC       | inMiles(<br>&emsp; float lat1, float lon1, <br>&emsp; float lat2, float lon2)      | As above      | above
KeywordChecker    | isAKeyword(String s)                                                               | O(logn)         | Tree Search `n` is the number of keywords.
StringFormatter   | convertAccentsFaster(String s)                                                     | O(klog(n)        | Tree Search for each letter, `n` being number of entries. `k` being length of string.
