package com.yassine.web.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressForm {
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String city;
  private Integer zipCode;
  private String addressLine1;
  private String addressLine2;
}
