package uk.ac.warwick.cs126.util;

public class HaversineDistanceCalculator {

    private final static float R = 6372.8f;
    private final static float kilometresInAMile = 1.609344f;

    public static float inKilometres(float lat1, float lon1, float lat2, float lon2) {
        double la1 = Math.toRadians(lat1);
        double lo1 = Math.toRadians(lon1);
        double la2 = Math.toRadians(lat2);
        double lo2 = Math.toRadians(lon2);

        double ap1 = Math.sin((la2-la1)/2);
        double ap2 = Math.sin((lo2-lo1)/2);
        double a = Math.pow(ap1,2) + (Math.pow(ap2,2) * Math.cos(lo1) * Math.cos(lo2));
        double c = 2*Math.asin(Math.pow(a, 0.5f));
        return Math.round(10*(float)(R * c))/10f;
    }

    public static float inMiles(float lat1, float lon1, float lat2, float lon2) {
        double la1 = Math.toRadians(lat1);
        double lo1 = Math.toRadians(lon1);
        double la2 = Math.toRadians(lat2);
        double lo2 = Math.toRadians(lon2);

        double ap1 = Math.sin((la2-la1)/2);
        double ap2 = Math.sin((lo2-lo1)/2);
        double a = Math.pow(ap1,2) + Math.pow(ap2,2) * Math.cos(lo1) * Math.cos(lo2);
        double c = 2*Math.asin(Math.pow(a,0.5));
        return Math.round(10*(float)(R *  c / kilometresInAMile))/10f;
    }

}