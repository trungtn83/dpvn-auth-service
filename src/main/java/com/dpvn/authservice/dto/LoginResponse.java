package com.dpvn.authservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
  private String message;
  private String token;
  private String refreshToken;

  public LoginResponse() {}

  public LoginResponse(String message) {
    this.message = message;
  }

  public LoginResponse(String message, String token, String refreshToken) {
    this.message = message;
    this.token = token;
    this.refreshToken = refreshToken;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
