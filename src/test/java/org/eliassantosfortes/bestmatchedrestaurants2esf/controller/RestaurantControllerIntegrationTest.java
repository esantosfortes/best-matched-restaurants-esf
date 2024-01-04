package org.eliassantosfortes.bestmatchedrestaurants2esf.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private CommandLineRunner commandLineRunner;

  @Nested
  class Success {
    @Test
    public void testSearchRestaurantsEndpointWithoutParameters() throws Exception {
      mockMvc
          .perform(get("/search/restaurants").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$").isArray())
          .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testSearchRestaurantsEndpointWithParametersExpectingMultipleResults()
        throws Exception {
      mockMvc
          .perform(
              get("/search/restaurants?restaurantName=Delicious&cuisineName=Russian")
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$").isArray())
          .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testSearchRestaurantsEndpointWithParametersExpectingSingleResult()
        throws Exception {
      String restaurantName = "Bang Delicious";
      mockMvc
          .perform(
              get("/search/restaurants?restaurantName=" + restaurantName)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$").isArray())
          .andExpect(jsonPath("$", hasSize(1)))
          .andExpect(jsonPath("$[0].name").value(restaurantName));
    }
  }

  @Nested
  class BadRequest {
    @Test
    public void testSearchRestaurantsEndpointWillThrowExceptionWithEmptyString() throws Exception {
      String restaurantName = "";
      mockMvc
          .perform(
              get("/search/restaurants?restaurantName=" + restaurantName)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value("String parameter cannot be empty"));
    }

    @Test
    public void testSearchRestaurantsEndpointWillThrowExceptionWithInvalidInteger()
        throws Exception {
      String customerRating = "x100";
      mockMvc
          .perform(
              get("/search/restaurants?customerRating=" + customerRating)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.detail")
                  .value(
                      "Failed to convert 'customerRating' with value: '" + customerRating + "'"));
    }

    @Test
    public void testSearchRestaurantsEndpointWillThrowExceptionWithInvalidDouble()
        throws Exception {
      String price = "x99";
      mockMvc
          .perform(
              get("/search/restaurants?price=" + price).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(
              jsonPath("$.detail").value("Failed to convert 'price' with value: '" + price + "'"));
    }

    @Test
    public void testSearchRestaurantsEndpointWillThrowExceptionWithInvalidCustomerRating()
        throws Exception {
      int customerRating = 6;
      mockMvc
          .perform(
              get("/search/restaurants?customerRating=" + customerRating)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value("'customerRating' must be a integer between 1-5"));
    }
  }
}
