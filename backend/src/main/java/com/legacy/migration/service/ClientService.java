package com.legacy.migration.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.legacy.migration.model.Client;

import jakarta.annotation.PostConstruct;

@Service
public class ClientService {

  private static final Logger log = LoggerFactory.getLogger(ClientService.class);

  private final ConcurrentMap<Long, Client> legacyClients = new ConcurrentHashMap<>();
  private final ConcurrentMap<Long, Client> migratedClients = new ConcurrentHashMap<>();

  @PostConstruct
  public void loadInitialClients() {
    LocalDateTime now = LocalDateTime.now();
    List<Client> seedClients = List.of(
        new Client(1L, "Alice Johnson", "alice.johnson@legacycrm.test", "+1-555-0101", "Enterprise",
            now.minusYears(5), now.minusYears(5), now, false),
        new Client(2L, "Benjamin Carter", "benjamin.carter@legacycrm.test", "+1-555-0102", "SMB",
            now.minusYears(3), now.minusYears(3), now, false),
        new Client(3L, "Carla Mendes", "carla.mendes@legacycrm.test", "+55-11-5555-0103", "Mid-Market",
            now.minusYears(2), now.minusYears(2), now, false),
        new Client(4L, "Derrick Woods", "derrick.woods@legacycrm.test", "+1-555-0104", "Enterprise",
            now.minusMonths(18), now.minusMonths(18), now, false));

    seedClients.forEach(client -> legacyClients.put(client.getId(), client));
  }

  public Collection<Client> getLegacyClients() {
    return legacyClients.values();
  }

  public Collection<Client> getMigratedClients() {
    return migratedClients.values();
  }

  public Optional<Client> findLegacyClient(Long clientId) {
    return Optional.ofNullable(legacyClients.get(clientId));
  }

  public void saveMigratedClient(Client client) {
    migratedClients.put(client.getId(), client);
  }

  public Client migrateClient(Long clientId) {
    Client client = legacyClients.get(clientId);
    if (client == null) {
      throw new NoSuchElementException("Client %s not found in legacy records".formatted(clientId));
    }
    if (client.isMigrated()) {
      throw new IllegalStateException("Client %s already migrated".formatted(clientId));
    }

    client.setMigrated(true);
    client.setUpdatedAt(LocalDateTime.now());
    migratedClients.put(client.getId(), client);
    log.info("Migrated client {} successfully", clientId);
    return client;
  }
}
