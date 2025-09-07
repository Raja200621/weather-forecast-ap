package com.example.weather;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class App extends Application {

    private WeatherService service;

    @Override
    public void start(Stage stage) {
        service = new WeatherService(loadApiKey());

        TextField city = new TextField();
        city.setPromptText("Enter city (e.g., Kolkata)");
        Button search = new Button("Get Weather");

        Label temp = new Label("-- °C");
        temp.setStyle("-fx-font-size: 28;");
        Label desc = new Label("--");
        Label extra = new Label("");
        Label status = new Label();
        status.setStyle("-fx-text-fill: #cc0000;");

        ImageView icon = new ImageView();
        icon.setFitWidth(100); icon.setFitHeight(100); icon.setPreserveRatio(true);

        HBox top = new HBox(10, city, search);
        top.setAlignment(Pos.CENTER);

        VBox root = new VBox(12, top, icon, temp, desc, extra, status);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(18));

        search.setOnAction(e -> fetch(city.getText(), temp, desc, extra, icon, status));
        city.setOnAction(e -> search.fire());

        Scene scene = new Scene(root, 440, 360);
        stage.setTitle("Weather Forecast");
        stage.setScene(scene);
        stage.show();
    }

    private void fetch(String city,
                       Label temp, Label desc, Label extra,
                       ImageView icon, Label status) {

        if (city == null || city.isBlank()) {
            status.setText("Please enter a city name.");
            return;
        }

        status.setText("Loading...");
        CompletableFuture.supplyAsync(() -> {
            try {
                return service.getWeather(city);
            } catch (Exception ex) {
                throw new CompletionException(ex);
            }
        }).whenComplete((data, err) -> Platform.runLater(() -> {
            if (err != null) {
                status.setText(humanMessage(err));
                return;
            }
            status.setText("");
            if (data.weather != null && !data.weather.isEmpty()) {
                String iconCode = data.weather.get(0).icon;
                icon.setImage(new Image("https://openweathermap.org/img/wn/" +
                        iconCode + "@2x.png", true));
                desc.setText(capitalize(data.weather.get(0).description) + " — " +
                        data.name + ", " + (data.sys != null ? data.sys.country : ""));
            }
            temp.setText(String.format("%.1f °C", data.main.temp));
            extra.setText(String.format("Humidity: %d%%   |   Wind: %.1f m/s",
                    data.main.humidity,
                    data.wind != null ? data.wind.speed : 0.0));
        }));
    }

    private static String humanMessage(Throwable t) {
        Throwable cause = (t.getCause() != null) ? t.getCause() : t;
        String msg = cause.getMessage() == null ? cause.toString() : cause.getMessage();
        if (msg.contains("City not found")) return "City not found. Check spelling.";
        if (msg.contains("API error: 401")) return "Invalid API key. Check configuration.";
        if (msg.contains("API error: 429")) return "Rate limit reached. Try again later.";
        return "Network/API error: " + msg;
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static String loadApiKey() {
        String key = System.getenv("OWM_API_KEY");
        if (key != null && !key.isBlank()) return key;

        try (InputStream in = App.class.getResourceAsStream("/app.properties")) {
            if (in != null) {
                Properties p = new Properties();
                p.load(in);
                key = p.getProperty("apiKey");
            }
        } catch (Exception ignored) {}

        if (key == null || key.isBlank()) {
            System.err.println("Missing API key. Set OWM_API_KEY or resources/app.properties");
            key = "MISSING";
        }
        return key;
    }

    public static void main(String[] args) { launch(args); }
}

