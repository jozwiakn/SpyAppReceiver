package com.example.natalia.spyappreceiver;

/**
 * Created by Natalia on 01.12.2016.
 */
public class Messages {
    public int id;
    String number;
    String start_time;
    public String text;
    String log;

    @Override
    public String toString() {
        return number + " " + start_time + " " + text;
    }

    public int getId() {
        return id;
    }
}
