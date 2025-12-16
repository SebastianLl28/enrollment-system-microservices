package com.app.authorization.server.infrastructure.adapter.out.persistence.entity;

/**
 * @author Alonso
 */
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
public class RoleJpaEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @Column(nullable = false, length = 50, unique = true)
  private String name;
  
  @Column(length = 255)
  private String description;
  
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "role_permission",
    joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "permission_id")
  )
  private Set<PermissionJpaEntity> permissions = new HashSet<>();
  
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "role_view",
    joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "view_code")
  )
  private Set<UIViewJpaEntity> views = new HashSet<>();
  
  protected RoleJpaEntity() {
  }
  
  public RoleJpaEntity(Integer id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }
  
  public Integer getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public Set<PermissionJpaEntity> getPermissions() {
    return permissions;
  }
  
  public Set<UIViewJpaEntity> getViews() {
    return views;
  }
  
  // helpers opcionales
  
  public void addPermission(PermissionJpaEntity permission) {
    this.permissions.add(permission);
  }
  
  public void removePermission(PermissionJpaEntity permission) {
    this.permissions.remove(permission);
  }
  
  public void addView(UIViewJpaEntity view) {
    this.views.add(view);
  }
  
  public void removeView(UIViewJpaEntity view) {
    this.views.remove(view);
  }
}
