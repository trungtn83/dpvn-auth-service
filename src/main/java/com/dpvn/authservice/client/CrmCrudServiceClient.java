package com.dpvn.authservice.client;

import com.dpvn.crmcrudservice.domain.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "crm-crud-service", contextId = "crm-crud-service-client")
public interface CrmCrudServiceClient {
  @GetMapping("/user/username/{username}")
  UserDto getCrmUserByUserName(@PathVariable String username);

  @PostMapping("/user")
  UserDto createCrmUser(UserDto userDto);

  @PostMapping("/user/change-password")
  void changePassword(@RequestBody UserDto userDto);
}
