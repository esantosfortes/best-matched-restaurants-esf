package org.eliassantosfortes.bestmatchedrestaurants2esf.controller;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.eliassantosfortes.bestmatchedrestaurants2esf.model.Restaurant;
import org.eliassantosfortes.bestmatchedrestaurants2esf.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class RestaurantController extends BaseController {

  private final FileService fileService;

  @Autowired
  public RestaurantController(FileService fileService) {
    this.fileService = fileService;
    this.fileService.readCSVFiles();
  }

  @GetMapping("/restaurants")
  public ResponseEntity<?> findRestaurants(
      @RequestParam(required = false) String restaurantName,
      @RequestParam(required = false) Integer customerRating,
      @RequestParam(required = false) Integer distance,
      @RequestParam(required = false) Double price,
      @RequestParam(required = false) String cuisineName) {

    ResponseEntity<ErrorResponse> validationResponse =
        validateParameters(restaurantName, customerRating, distance, price, cuisineName);

    if (validationResponse != null) {
      return validationResponse;
    }

    if (allParametersAreNull(restaurantName, customerRating, distance, price, cuisineName)) {
      return ResponseEntity.ok(List.of());
    }

    List<Restaurant> filteredRestaurants =
        fileService.getRestaurantList().stream()
            .filter(getRestaurantNamePredicate(restaurantName))
            .filter(getCustumerRatingPredicate(customerRating))
            .filter(getDistancePredicate(distance))
            .filter(getPricePredicate(price))
            .filter(getRestaurantPredicate(cuisineName))
            .sorted(RestaurantController::sortRestaurants)
            .limit(5)
            .collect(Collectors.toList());

    return ResponseEntity.ok(filteredRestaurants);
  }

  private Predicate<Restaurant> getRestaurantPredicate(String cuisineName) {
    return r ->
        cuisineName == null
            || fileService
                .getCuisineNameById(r.getCuisineId())
                .toLowerCase()
                .contains(cuisineName.toLowerCase());
  }

  private static Predicate<Restaurant> getPricePredicate(Double price) {
    return r -> price == null || r.getPrice() <= price;
  }

  private static Predicate<Restaurant> getDistancePredicate(Integer distance) {
    return r -> distance == null || r.getDistance() <= distance;
  }

  private static Predicate<Restaurant> getCustumerRatingPredicate(Integer customerRating) {
    return r -> customerRating == null || r.getCustomerRating() >= customerRating;
  }

  private static Predicate<Restaurant> getRestaurantNamePredicate(String restaurantName) {
    return r ->
        restaurantName == null || r.getName().toLowerCase().contains(restaurantName.toLowerCase());
  }

  private static int sortRestaurants(Restaurant restaurant1, Restaurant restaurant2) {
    // 1st priority - Distance ASC
    int distanceComparison = Integer.compare(restaurant1.getDistance(), restaurant2.getDistance());
    if (distanceComparison != 0) {
      return distanceComparison;
    }

    // 2nd priority - Rating DESC
    int customerRatingComparison =
        Integer.compare(restaurant2.getCustomerRating(), restaurant1.getCustomerRating());
    if (customerRatingComparison != 0) {
      return customerRatingComparison;
    }

    // 3rd priority - Price ASC
    return Double.compare(restaurant1.getPrice(), restaurant2.getPrice());
  }
}
