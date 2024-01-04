package org.eliassantosfortes.bestmatchedrestaurants2esf.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eliassantosfortes.bestmatchedrestaurants2esf.model.Restaurant;
import org.eliassantosfortes.bestmatchedrestaurants2esf.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class RestaurantControllerTest {

  @Mock private FileService fileService;

  @InjectMocks private RestaurantController restaurantController;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  class Success {

    @Test
    public void testFindRestaurantsWithNoFilters() {
      // Arrange
      List<Restaurant> mockedRestaurants = Collections.emptyList();
      when(fileService.getRestaurantList()).thenReturn(mockedRestaurants);

      // Act
      ResponseEntity<?> responseEntity =
          restaurantController.findRestaurants(null, null, null, null, null);

      // Assert
      assertEquals(200, responseEntity.getStatusCodeValue());
      assertEquals(mockedRestaurants, responseEntity.getBody());
    }

    @Test
    public void testFindRestaurantsWithFiltersSingleResult() {
      // Arrange
      List<Restaurant> mockedRestaurants = createMockedRestaurants();
      when(fileService.getRestaurantList()).thenReturn(mockedRestaurants);
      when(fileService.getCuisineNameById(anyInt())).thenReturn("Chinese");

      String restaurantName = "Deliciouszilla";
      int customerRating = 4;
      int distance = 1;
      Double price = 15d;
      int cuisineId = 2;
      String cuisineName = "Chinese";

      // Act
      ResponseEntity<?> responseEntity =
          restaurantController.findRestaurants(
              restaurantName, customerRating, distance, price, cuisineName);

      // Assert
      assertEquals(200, responseEntity.getStatusCodeValue());

      List<Restaurant> filteredRestaurants = (List<Restaurant>) responseEntity.getBody();
      assertNotNull(filteredRestaurants);
      assertEquals(1, filteredRestaurants.size());
      assertEquals(
          new Restaurant(restaurantName, customerRating, distance, price, cuisineId, cuisineName),
          filteredRestaurants.getFirst());
    }

    @Test
    public void testFindRestaurantsWithFiltersMultipleResults() {
      // Arrange
      List<Restaurant> mockedRestaurants = createMockedRestaurants();
      when(fileService.getRestaurantList()).thenReturn(mockedRestaurants);
      when(fileService.getCuisineNameById(anyInt())).thenReturn("Chinese");

      String restaurantName = "Delicious";
      int customerRating = 4;

      // Act
      ResponseEntity<?> responseEntity =
          restaurantController.findRestaurants(restaurantName, customerRating, null, null, null);

      // Assert
      assertEquals(200, responseEntity.getStatusCodeValue());

      List<Restaurant> filteredRestaurants = (List<Restaurant>) responseEntity.getBody();
      assertNotNull(filteredRestaurants);
      assertEquals(2, filteredRestaurants.size());
      assertEquals("Deliciouszilla", filteredRestaurants.get(0).getName());
      assertEquals("Gusto Delicious", filteredRestaurants.get(1).getName());
    }

    @Test
    public void testFindRestaurantsWillReturnMaxFiveResults() {
      // Arrange
      List<Restaurant> mockedRestaurants = createMockedRestaurants();
      when(fileService.getRestaurantList()).thenReturn(mockedRestaurants);
      when(fileService.getCuisineNameById(anyInt())).thenReturn("Chinese");

      int customerRating = 1;

      // Act
      ResponseEntity<?> responseEntity =
          restaurantController.findRestaurants(null, customerRating, null, null, null);

      // Assert
      assertEquals(200, responseEntity.getStatusCodeValue());

      List<Restaurant> filteredRestaurants = (List<Restaurant>) responseEntity.getBody();
      assertNotNull(filteredRestaurants);
      assertEquals(5, filteredRestaurants.size());
    }
  }

  @Nested
  class BadRequest {
    @Test
    public void testWithNegativeIntegerParameterThrowsBadRequest() {
      // Arrange & Act
      ResponseEntity<?> responseEntity =
          restaurantController.findRestaurants(null, -40, null, null, null);

      // Assert
      assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testWithNegativeDoubleParameterThrowsBadRequest() {
      // Arrange & Act
      ResponseEntity<?> responseEntity =
          restaurantController.findRestaurants(null, null, null, -100d, null);

      // Assert
      assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testWithEmptyStringParameterThrowsBadRequest() {
      // Arrange & Act
      ResponseEntity<?> responseEntity =
          restaurantController.findRestaurants("", null, null, null, null);

      // Assert
      assertEquals(400, responseEntity.getStatusCodeValue());
    }
  }

  private List<Restaurant> createMockedRestaurants() {
    List<Restaurant> mockedRestaurants = new ArrayList<>();
    mockedRestaurants.add(new Restaurant("Deliciouszilla", 4, 1, 15d, 2, "Chinese"));
    mockedRestaurants.add(new Restaurant("Gusto Delicious", 5, 3, 50d, 2, "Chinese"));
    mockedRestaurants.add(new Restaurant("Deliciousoryx", 1, 5, 25d, 2, "Chinese"));
    mockedRestaurants.add(new Restaurant("McDonalds", 3, 9, 10d, 1, "American"));
    mockedRestaurants.add(new Restaurant("Burger King", 2, 4, 11d, 1, "American"));
    mockedRestaurants.add(new Restaurant("Outback", 4, 11, 8d, 19, "Other"));
    return mockedRestaurants;
  }
}
