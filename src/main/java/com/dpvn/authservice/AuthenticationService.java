package com.dpvn.authservice;

import com.dpvn.authservice.client.CrmCrudServiceClient;
import com.dpvn.authservice.dto.LoginResponse;
import com.dpvn.crmcrudservice.domain.dto.UserDto;
import com.dpvn.shared.config.CacheService;
import com.dpvn.shared.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  private final JwtTokenProvider tokenProvider;
  private final CrmCrudServiceClient crmCrudServiceClient;
  private final CacheService cacheService;

  public AuthenticationService(
      JwtTokenProvider tokenProvider,
      CrmCrudServiceClient crmCrudServiceClient,
      CacheService cacheService) {
    this.tokenProvider = tokenProvider;
    this.crmCrudServiceClient = crmCrudServiceClient;
    this.cacheService = cacheService;
  }

  public LoginResponse login(String username, String password) {
    LoginResponse response = new LoginResponse();

    UserDto user = crmCrudServiceClient.getCrmUserByUserName(username);
    if (user == null) {
      response.setMessage("User not found");
      return response;
    }
    if (!user.getPassword().equals(password)) {
      response.setMessage("Invalid password");
      return response;
    }

    response.setMessage("Login successfully");
    response.setToken(tokenProvider.generateToken(user));
    response.setRefreshToken(tokenProvider.generateRefreshToken(user));
    return response;
  }

  public void logout(String tokenHeader) {
    String token = tokenHeader.substring(7); // Remove "Bearer " prefix
    long expirationTime = tokenProvider.getExpirationTime(token) - System.currentTimeMillis();
    cacheService.setValue(token, "blacklisted", (int) expirationTime / 1000);
  }

  public void changePassword(String username, String oldPassword, String newPassword) {
    UserDto user = crmCrudServiceClient.getCrmUserByUserName(username);
    if (user == null) {
      throw new BadRequestException("User not found");
    }
    if (oldPassword == null || !oldPassword.equals(user.getPassword())) {
      throw new BadRequestException("Current password is incorrect");
    }
    user.setPassword(newPassword);
    crmCrudServiceClient.changePassword(user);
  }
}
