package com.dpvn.authservice.client;

import com.dpvn.crm.crudservice.domain.dto.CrmUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "crm-crud-service", contextId = "crm-crud-service-client")
public interface CrmCrudServiceClient {
  @GetMapping("/crmcrud/user/username/{username}")
  CrmUserDto getCrmUserByUserName(@PathVariable String username);

  @PostMapping("/crmcrud/user")
  CrmUserDto createCrmUser(CrmUserDto user);
}
