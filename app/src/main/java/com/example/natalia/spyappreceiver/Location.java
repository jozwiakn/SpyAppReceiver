package com.example.natalia.spyappreceiver;

/**
 * Created by Natalia on 01.12.2016.
 */
public class Location {
    public int id;
    public String city;
    public String street;
    public String country;
    public String time;
    public String longitude;
    public String latitude;
    public String log;

    @Override
    public String toString() {
        return city + " " + street + " " + country + " " + time;
    }

    public int getId() {
        return id;
    }
}
