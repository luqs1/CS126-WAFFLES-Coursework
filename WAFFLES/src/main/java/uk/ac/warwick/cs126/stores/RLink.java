package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.models.Review;

public class RLink {
    public Long id;
    public Review rev;
    public Integer count;

    public RLink(Long left, Review centre, Integer right) {
        this.id = left;
        this.rev = centre;
        this.count = right;
    }
}
