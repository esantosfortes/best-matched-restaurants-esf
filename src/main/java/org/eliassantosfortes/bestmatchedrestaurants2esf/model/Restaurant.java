package org.eliassantosfortes.bestmatchedrestaurants2esf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
  private String name;
  private int customerRating;
  private int distance;
  private Double price;
  private int cuisineId;
  private String cuisineName;
}
