package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author Alonso
 */
@Entity
@Table(name = "faculty")
public class FacultyJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "faculty_id")
  private Integer facultyId;

  @Column(unique = true, nullable = false, length = 100)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(length = 100)
  private String location;

  @Column(length = 100)
  private String dean;
  
  @CreationTimestamp
  @Column(name = "registration_date", nullable = false, updatable = false)
  private LocalDateTime registrationDate;

  @Column(nullable = false)
  private Boolean active;

  @OneToMany(mappedBy = "faculty")
  private List<CareerJpaEntity> careers = new ArrayList<>();

  public FacultyJpaEntity() {
  }
  
  public Integer getFacultyId() {
    return facultyId;
  }
  
  public void setFacultyId(Integer facultyId) {
    this.facultyId = facultyId;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getLocation() {
    return location;
  }
  
  public void setLocation(String location) {
    this.location = location;
  }
  
  public String getDean() {
    return dean;
  }
  
  public void setDean(String dean) {
    this.dean = dean;
  }
  
  public LocalDateTime getRegistrationDate() {
    return registrationDate;
  }
  
  public void setRegistrationDate(LocalDateTime registrationDate) {
    this.registrationDate = registrationDate;
  }
  
  public Boolean getActive() {
    return active;
  }
  
  public void setActive(Boolean active) {
    this.active = active;
  }
  
  public List<CareerJpaEntity> getCareers() {
    return careers;
  }
  
  public void setCareers(
    List<CareerJpaEntity> careers) {
    this.careers = careers;
  }
}
