package com.yassine.web.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPojo {
  private String message;
  private String email;
  private String fullName;
  private Double rating;

}
