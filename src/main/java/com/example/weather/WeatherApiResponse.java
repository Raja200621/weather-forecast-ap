package com.example.weather;
import java.util.List;

public class WeatherApiResponse {
    public List<Weather> weather;
    public Main main;
    public Wind wind;
    public Sys sys;
    public String name;

    public static class Weather {
        public String main;
        public String description;
        public String icon;
    }

    public static class Main {
        public double temp;
        public double feels_like;
        public int humidity;
    }

    public static class Wind {
        public double speed;
    }

    public static class Sys {
        public String country;
    }
}
