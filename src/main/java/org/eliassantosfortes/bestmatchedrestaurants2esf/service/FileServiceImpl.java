package org.eliassantosfortes.bestmatchedrestaurants2esf.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eliassantosfortes.bestmatchedrestaurants2esf.model.Cuisine;
import org.eliassantosfortes.bestmatchedrestaurants2esf.model.Restaurant;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {
  private final ResourceLoader resourceLoader;
  private final List<Restaurant> restaurantList = new ArrayList<>();
  private final List<Cuisine> cuisineList = new ArrayList<>();

  public FileServiceImpl(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public void readCSVFiles() {
    readCuisines();
    readRestaurants();
    mapCuisineNamesToRestaurants();
  }

  private void readCuisines() {
    Resource cuisinesResource = resourceLoader.getResource("classpath:data/cuisines.csv");
    try (BufferedReader cuisinesReader =
        new BufferedReader(new InputStreamReader(cuisinesResource.getInputStream()))) {
      cuisinesReader
          .lines()
          .skip(1)
          .map(line -> line.split(","))
          .forEach(data -> cuisineList.add(new Cuisine(Integer.parseInt(data[0]), data[1])));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readRestaurants() {
    Resource restaurantsResource = resourceLoader.getResource("classpath:data/restaurants.csv");
    try (BufferedReader restaurantsReader =
        new BufferedReader(new InputStreamReader(restaurantsResource.getInputStream()))) {
      restaurantsReader
          .lines()
          .skip(1)
          .map(line -> line.split(","))
          .forEach(
              data ->
                  restaurantList.add(
                      new Restaurant(
                          data[0],
                          Integer.parseInt(data[1]),
                          Integer.parseInt(data[2]),
                          Integer.parseInt(data[3]),
                          Integer.parseInt(data[4]),
                          "")));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void mapCuisineNamesToRestaurants() {
    restaurantList.forEach(
        restaurant -> restaurant.setCuisineName(getCuisineNameById(restaurant.getCuisineId())));
  }

  public String getCuisineNameById(int cuisineId) {
    return cuisineList.stream()
        .filter(c -> c.getId() == cuisineId)
        .findFirst()
        .map(Cuisine::getName)
        .orElse("");
  }

  public List<Restaurant> getRestaurantList() {
    return Collections.unmodifiableList(restaurantList);
  }
}
