# Weather Forecast App

A JavaFX desktop application that fetches and displays real-time weather information using the OpenWeatherMap API.

##  Features
- Fetch real-time weather data for any city
- Display temperature, weather condition, humidity, and wind speed
- Show icons for weather conditions
- Error handling for invalid city names and API issues
- Simple and interactive JavaFX interface

##  Tools & Technologies
- Java
- JavaFX
- Gson (for JSON parsing)
- OpenWeatherMap API
- Maven (build tool)
- Visual Studio Code

##  Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/weather-forecast-app.git
cd weather-forecast-app
```

### 2. Add your API key
- Sign up at [OpenWeatherMap](https://openweathermap.org/api) and get a free API key.
- Open the `WeatherApp.java` file and replace `YOUR_API_KEY_HERE` with your actual key.

### 3. Build the project
```bash
mvn clean package
```

### 4. Run the application
```bash
mvn javafx:run
```
##  Deliverables
- GUI desktop application
- Source code
- Documentation (Project Report & README)

##  Future Improvements
- Add 5-day weather forecast
- Include sunrise/sunset and feels-like temperature
- Improve UI with charts/graphs
- Support for multiple languages
- Package as an installer

---
 Developed with using Java & JavaFX.
