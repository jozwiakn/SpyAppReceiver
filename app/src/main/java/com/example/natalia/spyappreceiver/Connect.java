package com.example.natalia.spyappreceiver;

/**
 * Created by Natalia on 01.12.2016.
 */
public class Connect {
    public int id;
    public String number;
    public String start_time;
    public String time;
    public String log;

    @Override
    public String toString() {
        return number + " " + start_time + " " + time;
    }

    public int getId() {
        return id;
    }
}
