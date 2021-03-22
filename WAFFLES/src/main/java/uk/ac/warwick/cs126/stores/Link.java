package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.models.Favourite;

public class Link {
    public Long id;
    public Favourite fave;
    public Integer count;

    public Link(Long left, Favourite centre, Integer right) {
        this.id = left;
        this.fave = centre;
        this.count = right;
    }
}
