package com.example.weather;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class WeatherService {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private final String apiKey;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public WeatherService(String apiKey) {
        this.apiKey = Objects.requireNonNull(apiKey, "API key is required");
    }

    public WeatherApiResponse getWeather(String city)
            throws IOException, InterruptedException, ApiException {

        String q = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String url = String.format("%s?q=%s&appid=%s&units=metric", BASE_URL, q, apiKey);

        HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .header("Accept", "application/json").GET().build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() == 404) throw new ApiException("City not found.");
        if (res.statusCode() >= 400) throw new ApiException("API error: " + res.statusCode());

        return gson.fromJson(res.body(), WeatherApiResponse.class);
    }

    public static class ApiException extends Exception {
        public ApiException(String message) { super(message); }
    }
}

