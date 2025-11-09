package com.legacy.migration.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

  private Long id;
  private String fullName;
  private String email;
  private String phoneNumber;
  private String segment;
  private LocalDateTime registeredAt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private boolean migrated;
}
