package com.app.authorization.server.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alonso
 */
@Entity
@Table(name = "\"user\"")
public class UserJpaEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private String username;
  
  private String password;
  
  private String fullName;
  
  private String email;
  
  private Boolean twoFactorEnabled = Boolean.FALSE;
  
  private String twoFactorSecret;
  
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "user_role",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<RoleJpaEntity> roles = new HashSet<>();
  
  public UserJpaEntity() {
  }
  
  public UserJpaEntity(Integer id, String username, String password, String fullName, String email, Boolean twoFactorEnabled, String twoFactorSecret) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.fullName = fullName;
    this.email = email;
    this.twoFactorEnabled = twoFactorEnabled;
    this.twoFactorSecret = twoFactorSecret;
  }
  
  public String getUsername() {
    return username;
  }
  
  public String getPassword() {
    return password;
  }
  
  public Integer getId() {
    return id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public String getFullName() {
    return fullName;
  }
  
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public Boolean getTwoFactorEnabled() {
    return twoFactorEnabled;
  }
  
  public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
    this.twoFactorEnabled = twoFactorEnabled;
  }
  
  public String getTwoFactorSecret() {
    return twoFactorSecret;
  }
  
  public void setTwoFactorSecret(String twoFactorSecret) {
    this.twoFactorSecret = twoFactorSecret;
  }
  
  public Set<RoleJpaEntity> getRoles() {
    return roles;
  }
  
  public void setRoles(
    Set<RoleJpaEntity> roles) {
    this.roles = roles;
  }
  
}
