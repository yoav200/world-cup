package com.ab.worldcup.account;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");

  private final String authority;

  Role(String authority) {
    this.authority = authority;
  }

  @Override
  public String getAuthority() {
    return this.authority;
  }

  public static List<String> supportedRolesAsString() {
    return Stream.of(Role.values()).map(Role::getAuthority).collect(Collectors.toList());
  }
}
