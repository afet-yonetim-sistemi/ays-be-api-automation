package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.utility.AysRandomUtil;

@Getter
@Setter
public class Location {

    private double latitude;
    private double longitude;

    public static Location generate(double latitude, double longitude){
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }
    public static Location generateLocation(double minLat, double maxLat, double minLon, double maxLon) {

        double latitude = AysRandomUtil.generateLatitude(minLat,maxLat);
        double longitude = AysRandomUtil.generateLongitude(minLon,maxLon);
        return Location.generate(latitude,longitude);
    }

    public static Location generateLocationTR() {
        double latitude = AysRandomUtil.generateLatitude();
        double longitude = AysRandomUtil.generateLongitude();
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

}
