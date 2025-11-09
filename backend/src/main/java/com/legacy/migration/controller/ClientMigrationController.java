package com.legacy.migration.controller;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.legacy.migration.model.Client;
import com.legacy.migration.service.ClientService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@Tag(name = "Client Migration", description = "Operations for migrating clients from Legacy CRM to NewConnect")
@CrossOrigin(origins = "*")
public class ClientMigrationController {

  private final ClientService clientService;

  public ClientMigrationController(ClientService clientService) {
    this.clientService = clientService;
  }

  @GetMapping("/legacy/clients")
  public Collection<Client> getLegacyClients() {
    return clientService.getLegacyClients();
  }

  @GetMapping("/new/clients")
  public Collection<Client> getMigratedClients() {
    return clientService.getMigratedClients();
  }

  @PostMapping("/migrate/{id}")
  public ResponseEntity<?> migrateClient(@PathVariable("id") Long clientId, HttpServletRequest request) {
    try {
      Client migrated = clientService.migrateClient(clientId);
      return ResponseEntity.ok(migrated);
    } catch (NoSuchElementException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI()));
    } catch (IllegalStateException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(buildError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI()));
    }
  }

  private ApiError buildError(HttpStatus status, String message, String path) {
    return new ApiError(status.value(), status.getReasonPhrase(), message, path, OffsetDateTime.now());
  }

  private record ApiError(int status, String error, String message, String path, OffsetDateTime timestamp) {
  }
}
