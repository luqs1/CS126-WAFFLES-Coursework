package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.models.Favourite;

public class FLink {
    public Long id;
    public Favourite fave;
    public Integer count;

    public FLink(Long left, Favourite centre, Integer right) {
        this.id = left;
        this.fave = centre;
        this.count = right;
    }
}
