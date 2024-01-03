package org.eliassantosfortes.bestmatchedrestaurants2esf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.eliassantosfortes.bestmatchedrestaurants2esf.model.Restaurant;
import org.eliassantosfortes.bestmatchedrestaurants2esf.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

  @Operation(
      summary = "Find Restaurants",
      description = "Returns a list of restaurants based on criteria")
  @ApiResponse(
      responseCode = "200",
      description = "Successful retrieval of restaurants",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = Restaurant.class)),
              examples =
                  @ExampleObject(
                      name = "Restaurants Example",
                      value =
                          "[{\"name\":\"Deliciouszilla\",\"customerRating\":4,\"distance\":1,\"price\":15,\"cuisineId\":2,\"cuisineName\":\"Chinese\"}, "
                              + "{\"name\":\"Gusto Delicious\",\"customerRating\":5,\"distance\":3,\"price\":50,\"cuisineId\":2,\"cuisineName\":\"Chinese\"}, "
                              + "{\"name\":\"Deliciousoryx\",\"customerRating\":1,\"distance\":5,\"price\":25,\"cuisineId\":2,\"cuisineName\":\"Chinese\"}]")))
  @ApiResponse(
      responseCode = "400",
      description = "Bad Request",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorResponse.class),
              examples =
                  @ExampleObject(
                      name = "BadRequest Example",
                      value =
                          "{\n"
                              + "    \"type\": \"about:blank\",\n"
                              + "    \"title\": \"Bad Request\",\n"
                              + "    \"status\": 400,\n"
                              + "    \"detail\": \"Failed to convert 'price' with value: 'x40'\",\n"
                              + "    \"instance\": \"/search/restaurants\"\n"
                              + "}")))
  @GetMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
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
