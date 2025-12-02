package com.dpvn.authservice.authentication;

import com.dpvn.authservice.client.ReportCrudServiceClient;
import com.dpvn.authservice.dto.LoginResponse;
import com.dpvn.reportcrudservice.domain.dto.UserDto;
import com.dpvn.sharedcore.config.CacheService;
import com.dpvn.sharedcore.exception.BadRequestException;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  private final JwtTokenProvider tokenProvider;
  private final ReportCrudServiceClient reportCrudServiceClient;
  private final CacheService cacheService;

  @Autowired private OkHttpClient okHttpClient;

  @Autowired private feign.Client feignClient;

  public AuthenticationService(
      JwtTokenProvider tokenProvider,
      ReportCrudServiceClient reportCrudServiceClient,
      CacheService cacheService) {
    this.tokenProvider = tokenProvider;
    this.reportCrudServiceClient = reportCrudServiceClient;
    this.cacheService = cacheService;
  }

  public LoginResponse login(String username, String password) throws InterruptedException {
    LoginResponse response = new LoginResponse();
    UserDto userDto = reportCrudServiceClient.getUserByUserName(username);
    if (userDto == null) {
      response.setMessage("User not found");
      return response;
    }
    if (!userDto.getPassword().equals(password)) {
      response.setMessage("Invalid password");
      return response;
    }

    response.setMessage("Login successfully");
    response.setToken(tokenProvider.generateToken(userDto));
    response.setRefreshToken(tokenProvider.generateRefreshToken(userDto));
    return response;
  }

  public void logout(String tokenHeader) {
    String token = tokenHeader.substring(7); // Remove "Bearer " prefix
    long expirationTime = tokenProvider.getExpirationTime(token) - System.currentTimeMillis();
    cacheService.setValue(token, "blacklisted", (int) expirationTime / 1000);
  }

  public void changePassword(String username, String oldPassword, String newPassword) {
    UserDto userDto = reportCrudServiceClient.getUserByUserName(username);
    if (userDto == null) {
      throw new BadRequestException("User not found");
    }
    if (oldPassword == null || !oldPassword.equals(userDto.getPassword())) {
      throw new BadRequestException("Current password is incorrect");
    }
    userDto.setPassword(newPassword);
    reportCrudServiceClient.changePassword(userDto);
  }
}
