package org.eliassantosfortes.bestmatchedrestaurants2esf.service;

import java.util.List;
import org.eliassantosfortes.bestmatchedrestaurants2esf.model.Restaurant;

public interface FileService {
  void readCSVFiles();

  String getCuisineNameById(int cuisineId);

  List<Restaurant> getRestaurantList();
}
