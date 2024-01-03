package org.eliassantosfortes.bestmatchedrestaurants2esf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BestMatchedRestaurants2EsfApplication {

  public static void main(String[] args) {
    SpringApplication.run(BestMatchedRestaurants2EsfApplication.class, args);
  }

  @Bean
  public CommandLineRunner customRunner(RestTemplate restTemplate) {
    return args -> {
      Terminal terminal = TerminalBuilder.builder().build();
      LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

      boolean continueSearching = true;

      while (continueSearching) {

        String restaurantName = null;
        Integer customerRating = null;
        Integer distance = null;
        Double price = null;
        String cuisineName = null;

        restaurantName =
            (String)
                buildPrompt(
                    reader,
                    "Do you want to search for restaurant name? (y/n) ",
                    restaurantName,
                    "Enter restaurant name: ");

        customerRating =
            (Integer)
                buildPrompt(
                    reader,
                    "Do you want to search for customer rating? (y/n) ",
                    customerRating,
                    "Enter customer rating (1-5): ");

        distance =
            (Integer)
                buildPrompt(
                    reader,
                    "Do you want to search for the Distance? (y/n) ",
                    distance,
                    "Enter distance (miles): ");

        price =
            (Double)
                buildPrompt(
                    reader,
                    "Do you want to search for the Price? (y/n) ",
                    price,
                    "Enter the price ($): ");

        cuisineName =
            (String)
                buildPrompt(
                    reader,
                    "Do you want to search for cuisine name? (y/n) ",
                    cuisineName,
                    "Enter cuisine type: ");

        consumeEndpoint(restTemplate, restaurantName, customerRating, distance, price, cuisineName);

        String continueSearch =
            reader.readLine("\n\nDo you want to perform another search? (y/n) ").trim();

        continueSearching = continueSearch.equalsIgnoreCase("y");
      }

      System.out.println("Exiting app. Goodbye!");
    };
  }

  private static Object buildPrompt(
      LineReader reader, String searchedField, Object paramValue, String promptMessage) {
    boolean isSearchField = reader.readLine(searchedField).trim().equalsIgnoreCase("y");

    if (isSearchField) {
      paramValue = reader.readLine(promptMessage);
    }
    return paramValue;
  }

  public void consumeEndpoint(
      RestTemplate restTemplate,
      String restaurantName,
      Integer customerRating,
      Integer distance,
      Double price,
      String cuisineName) {
    String baseUrl = "http://localhost:8080";
    Map<String, Object> params = new HashMap<>();

    String endpointUrl =
        buildEndpointUrl(
            restaurantName, customerRating, distance, price, cuisineName, params, baseUrl);

    ResponseEntity<String> responseEntity =
        restTemplate.getForEntity(
            endpointUrl,
            String.class,
            restaurantName,
            customerRating,
            distance,
            price,
            cuisineName);

    printResponseForCommandLine(responseEntity);
  }

  private static void printResponseForCommandLine(ResponseEntity<String> responseEntity) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    if (responseEntity.getStatusCode().is2xxSuccessful()) {
      String responseBody = responseEntity.getBody();
      String prettyJson = gson.toJson(JsonParser.parseString(responseBody));
      System.out.println("Received response: \n\n" + prettyJson);
    } else if (responseEntity.getStatusCode().is4xxClientError()) {
      String responseBody = responseEntity.getBody();
      String prettyJson = gson.toJson(JsonParser.parseString(responseBody));
      System.out.println("Failed to fetch data: \n\n" + prettyJson);
    } else {
      System.out.println("Failed to fetch data. Status code: " + responseEntity.getStatusCode());
    }
  }

  private String buildEndpointUrl(
      String restaurantName,
      Integer customerRating,
      Integer distance,
      Double price,
      String cuisineName,
      Map<String, Object> params,
      String baseUrl) {
    buildParameters(restaurantName, customerRating, distance, price, cuisineName, params);

    String queryString = mapToQueryString(params);
    return baseUrl + "/search/restaurants" + queryString;
  }

  private static void buildParameters(
      String restaurantName,
      Integer customerRating,
      Integer distance,
      Double price,
      String cuisineName,
      Map<String, Object> params) {
    if (restaurantName != null) {
      params.put("restaurantName", restaurantName);
    }
    if (customerRating != null) {
      params.put("customerRating", customerRating);
    }
    if (distance != null) {
      params.put("distance", distance);
    }
    if (price != null) {
      params.put("price", price);
    }
    if (cuisineName != null) {
      params.put("cuisineName", cuisineName);
    }
  }

  private String mapToQueryString(Map<String, ?> params) {
    String queryString =
        params.entrySet().stream()
            .map(
                entry -> {
                  String valueAsString =
                      entry.getValue() != null ? entry.getValue().toString() : "";
                  return URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8)
                      + "="
                      + URLEncoder.encode(valueAsString, StandardCharsets.UTF_8);
                })
            .collect(Collectors.joining("&"));

    return queryString.isEmpty() ? "" : "?" + queryString;
  }
}
