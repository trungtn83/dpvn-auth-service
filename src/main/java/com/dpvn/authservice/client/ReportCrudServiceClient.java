package com.dpvn.authservice.client;

import com.dpvn.reportcrudservice.domain.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "report-crud-service", contextId = "report-crud-service-client")
public interface ReportCrudServiceClient {
  @GetMapping("/user/username/{username}")
  UserDto getUserByUserName(@PathVariable String username);

  @PostMapping("/user")
  UserDto createUser(String userDto);

  @PostMapping("/user/change-password")
  void changePassword(@RequestBody UserDto userDto);
}
