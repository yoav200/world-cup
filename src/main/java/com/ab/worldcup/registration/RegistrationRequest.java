package com.ab.worldcup.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
  @NotBlank(message = "First Name is mandatory")
  private final String firstName;

  @NotBlank(message = "Last Name is mandatory")
  private final String lastName;

  @Email(message = "Invalid Email")
  @NotBlank(message = "Email is mandatory")
  private final String email;

  @NotBlank(message = "Password is mandatory")
  private final String password;

  private final String token;
}
