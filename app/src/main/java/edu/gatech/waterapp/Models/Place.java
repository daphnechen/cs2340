package edu.gatech.waterapp.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Derian on 2/27/2017.
 */

public class Place {

    private LatLng location;
    private String address;
    private String name;

    public Place() {}

    public Place(com.google.android.gms.location.places.Place place) {
        location = place.getLatLng();
        address = place.getAddress().toString();
        name = place.getName().toString();
    }

    public LatLng getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Creates a Map representation of the Place
     * @return a HashMap representation of the Place
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        List<Double> latlng = new ArrayList<>(Arrays.asList(location.latitude, location.longitude));
        map.put("location", latlng);
        map.put("address", address);
        return map;
    }
}
