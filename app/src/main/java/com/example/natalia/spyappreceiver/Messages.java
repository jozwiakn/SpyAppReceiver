package com.example.natalia.spyappreceiver;

/**
 * Created by Natalia on 01.12.2016.
 */
public class Messages {
    public int id;
    public String number;
    public String start_time;
    public String text;
    public String log;

    @Override
    public String toString() {
        return number + " " + start_time + " " + text;
    }

    public int getId() {
        return id;
    }
}
