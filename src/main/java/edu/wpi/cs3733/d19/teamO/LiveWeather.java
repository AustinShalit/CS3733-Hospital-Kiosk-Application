package edu.wpi.cs3733.d19.teamO;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.image.Image;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import systems.uom.common.USCustomary;
import tec.units.indriya.quantity.Quantities;
import tec.units.indriya.unit.Units;

import static java.nio.charset.StandardCharsets.UTF_8;

public class LiveWeather {

  private static final Logger logger = Logger.getLogger(LiveWeather.class.getName());

  private static final String API_KEY = "c2711050ed24651e99a523ce6d08ad73";
  private static final String CITY = "Boston";
  private static final OWM.Country COUNTRY = OWM.Country.UNITED_STATES;

  private final ReadOnlyObjectWrapper<CurrentWeather> currentWeather
      = new ReadOnlyObjectWrapper<>();
  private final ReadOnlyDoubleWrapper minTemp = new ReadOnlyDoubleWrapper(0.0);
  private final ReadOnlyDoubleWrapper maxTemp = new ReadOnlyDoubleWrapper(0.0);
  private final ReadOnlyStringWrapper description = new ReadOnlyStringWrapper("");
  private final ReadOnlyObjectWrapper<Image> image = new ReadOnlyObjectWrapper<>();

  private final ScheduledExecutorService scheduler
      = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
  });
  private final OWM openWeatherMap;

  public LiveWeather() {
    openWeatherMap = new OWM(API_KEY);

    currentWeather.addListener(((observable, oldValue, newValue) -> {
      minTemp.set(Quantities.getQuantity(newValue.getMainData().getTempMin(), Units.KELVIN)
          .to(USCustomary.FAHRENHEIT).getValue().doubleValue());
      maxTemp.set(Quantities.getQuantity(newValue.getMainData().getTempMax(), Units.KELVIN)
          .to(USCustomary.FAHRENHEIT).getValue().doubleValue());
    }));
  }

  public void updateNow() {
    scheduler.execute(this::update);
  }

  public void start() {
    scheduler.scheduleAtFixedRate(this::update, 0, 15, TimeUnit.MINUTES);
  }

  public void shutdown() {
    scheduler.shutdown();
  }

  public CurrentWeather getCurrentWeather() {
    return currentWeather.get();
  }

  public ReadOnlyObjectProperty<CurrentWeather> currentWeatherProperty() {
    return currentWeather.getReadOnlyProperty();
  }

  public double getMinTemp() {
    return minTemp.get();
  }

  public ReadOnlyDoubleProperty minTempProperty() {
    return minTemp;
  }

  public double getMaxTemp() {
    return maxTemp.get();
  }

  public ReadOnlyDoubleProperty maxTempProperty() {
    return maxTemp;
  }

  public String getDescription() {
    return description.get();
  }

  public ReadOnlyStringProperty descriptionProperty() {
    return description;
  }

  public Image getImage() {
    return image.get();
  }

  public ReadOnlyObjectProperty<Image> imageProperty() {
    return image;
  }

  private void update() {
    try {
      currentWeather.set(openWeatherMap.currentWeatherByCityName(CITY, COUNTRY));

      String surl = "http://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&APPID="
          + API_KEY;
      URL url = new URL(surl);
      URLConnection request = url.openConnection();
      request.connect();

      // Convert to a JSON object  using GSON
      JsonParser jp = new JsonParser();
      JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent(), UTF_8));
      JsonObject rootobj = root.getAsJsonObject();
      String icon = rootobj.get("weather")
          .getAsJsonArray()
          .get(0)
          .getAsJsonObject()
          .get("icon")
          .getAsString(); // Get the icon
      String discrp = rootobj.get("weather")
          .getAsJsonArray()
          .get(0)
          .getAsJsonObject()
          .get("description")
          .getAsString(); // Get the description

      description.set(discrp.toUpperCase());
      image.set(new Image(getClass().getResource("weather/" + icon + ".png")
          .openStream()));
    } catch (APIException | IOException ex) {
      logger.log(Level.WARNING, "Unable to update weather", ex);
    }
  }
}
