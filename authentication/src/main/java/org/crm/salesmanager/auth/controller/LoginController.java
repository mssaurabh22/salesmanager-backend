package org.crm.salesmanager.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health", description = "Basic application test APIs")
public class LoginController {

  @GetMapping("/api/test")
  @Operation(summary = "Test API", description = "Returns a simple health-check response")
  public ResponseEntity<String> testAPi () {
    return ResponseEntity.ok("Hello World");
  }
}
