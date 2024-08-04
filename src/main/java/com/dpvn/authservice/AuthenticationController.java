package com.dpvn.authservice;

import com.dpvn.shared.dto.ChangePasswordRequest;
import com.dpvn.shared.dto.LoginRequest;
import com.dpvn.shared.dto.LoginResponse;
import com.dpvn.shared.util.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest request) {
    LoginResponse response =
        authenticationService.login(request.getUsername(), request.getPassword());
    if (StringUtil.isEmpty(response.getToken())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    return ResponseEntity.ok(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestHeader("Authorization") String tokenHeader) {
    authenticationService.logout(tokenHeader);
    return ResponseEntity.ok().body("Successfully logged out");
  }

  @PostMapping("/change-password")
  public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
    authenticationService.changePassword(
        request.getUsername(), request.getOldPassword(), request.getNewPassword());
    return ResponseEntity.ok().body("Password changed successfully");
  }
}
